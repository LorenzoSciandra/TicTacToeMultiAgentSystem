package Jade.Behaviours.MasterArbiter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Jade.Messages.*;
import Jade.Agents.MasterArbiterAgent;
import Jade.Messages.InformWin;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.util.*;

public class PlayGameBehaviour extends Behaviour {

    private AID[] arbiterAgents;
    private AID[] playerAgents;
    private List<AID> currentPlayers;
    private int numPlayers;
    private int numArbiters;
    private int numRounds;
    private int numRoundsPlayed;
    private int step;
    private MessageTemplate mt;

    public PlayGameBehaviour(AID[] arbiterAgents, AID[] playerAgents) {
        this.arbiterAgents = arbiterAgents;
        this.playerAgents = playerAgents;
        this.numPlayers = playerAgents.length;
        this.numArbiters = arbiterAgents.length;
        this.numRounds = (int) Math.ceil(Math.log(numPlayers));
        this.numRoundsPlayed = 0;
        this.currentPlayers = new ArrayList<AID>();
        for (int i = 0; i < playerAgents.length; i++) {
            this.currentPlayers.add(playerAgents[i]);
        }
        this.step = 0;
    }

    @Override
    public void action() {
        if (numPlayers % 2 == 0 && numArbiters >= numRounds) {
            if (step == 0) {
                System.out.println("Assegno i giocatori e gli arbitri per il round: " + (numRoundsPlayed+1));
                assign_players_and_arbiters();
                step++;
            } else {
                mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage msg = getAgent().receive(mt);
                InformWin content;
                if (msg != null) {
                    // CFP Message received. Process it
                    try {
                        content = (InformWin) msg.getContentObject();
                        AID winner = content.getWinner();
                        currentPlayers.add(winner);
                        if (currentPlayers.size() == numPlayers / 2) {
                            numRoundsPlayed++;
                            numPlayers = currentPlayers.size();
                            step--;
                            if (numRoundsPlayed < numRounds) {
                                System.out.println("Procediamo con la fase successiva! Si giocherà a breve il round: " + (numRoundsPlayed+1));
                                System.out.println("Vincitori di questa fase: " + currentPlayers);
                            }
                        }
                    } catch (UnreadableException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    block();
                }
            }
        } else {
            System.out.println("NUMERO NON CORRETTO DI GIOCATORI O ARBITRI");
            ((MasterArbiterAgent) myAgent).setWinner(currentPlayers.get(0));

            ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
            msg.setContent("END");
            for (AID aAID : playerAgents) {
                msg.addReceiver(aAID);
            }
            for (AID aAID : arbiterAgents) {
                msg.addReceiver(aAID);
            }
            getAgent().send(msg);
            getAgent().doDelete();
        }
    }

    @Override
    public boolean done() {
        boolean cond = numRoundsPlayed == numRounds;
        if (cond) {
            System.out.println("Fine del gioco!");
            ((MasterArbiterAgent) myAgent).setWinner(currentPlayers.get(0));

            ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
            msg.setContent("END");
            for (AID aAID : playerAgents) {
                msg.addReceiver(aAID);
            }
            for (AID aAID : arbiterAgents) {
                msg.addReceiver(aAID);
            }
            getAgent().send(msg);
            getAgent().doDelete();
        }
        return cond;
    }

    private void assign_players_and_arbiters() {
        Collections.shuffle(currentPlayers);
        for (int i = 0; i < numPlayers - 1; i = i + 2) {
            ProposalToArbiter game = new ProposalToArbiter(currentPlayers.get(i),
                    currentPlayers.get(i + 1), numRoundsPlayed, numRounds);
            ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
            try {
                msg.setContentObject(game);
                msg.addReceiver(arbiterAgents[i/2]);
                getAgent().send(msg);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        currentPlayers = new ArrayList<AID>();
    }

}
