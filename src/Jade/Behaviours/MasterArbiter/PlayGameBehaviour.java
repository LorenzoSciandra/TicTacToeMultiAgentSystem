package Jade.Behaviours.MasterArbiter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Jade.Agents.MasterArbiterAgent;
import Jade.Messages.InformWin;
import Jade.Messages.ProposalToArbiter;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

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
        this.numRounds = (int) Math.ceil(log2(numPlayers));
        this.numRoundsPlayed = 0;
        this.currentPlayers = new ArrayList<AID>();
        for (int i = 0; i < playerAgents.length; i++) {
            this.currentPlayers.add(playerAgents[i]);
        }
        this.step = 0;
    }

    /**
     * This behaviour is varies depending on the step of the game:
     * - If the step is 0, the Master Arbiter assigns the arbiters to the players
     * - If the step is > 0, the Master Arbiter waits that every player has finished
     * the game to start the next round
     * 
     * If the number of arbiters or player are not enough to play the tournament
     * correctly, the Master Arbiter will end
     * the tournament and destroy the agents.
     */
    @Override
    public void action() {
        double log = log2(numPlayers);
        double ceiling = Math.ceil(log);
        if (log - ceiling == 0 && numArbiters >= numRounds && numPlayers!=1) {
            if (step == 0) {
                System.out.println("Assegno i giocatori e gli arbitri per il round: " + (numRoundsPlayed + 1));
                assign_players_and_arbiters();
                step++;
            } else {
                mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage msg = getAgent().receive(mt);
                InformWin content;
                if (msg != null) {
                    try {
                        content = (InformWin) msg.getContentObject();
                        AID winner = content.getWinner();
                        currentPlayers.add(winner);
                        if (currentPlayers.size() == numPlayers / 2) {
                            numRoundsPlayed++;
                            numPlayers = currentPlayers.size();
                            step--;
                            if (numRoundsPlayed < numRounds) {
                                System.out.println("Procediamo con la fase successiva! Si giocher?? a breve il round: "
                                        + (numRoundsPlayed + 1));
                                System.out.println("Vincitori di questa fase: " + currentPlayers);
                            }
                        }
                    } catch (UnreadableException e) {
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

    /**
     * The game has ended: the Master Arbiter announces the winner and destroys the
     * agents.
     * 
     * @return boolean
     */
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

    /**
     * Assigns the players to the arbiters.
     * - Every arbiter has two players
     * - There is only one Master Arbiter for the game
     * - Every player has one arbiter only
     */
    private void assign_players_and_arbiters() {
        Collections.shuffle(currentPlayers);
        for (int i = 0; i < numPlayers - 1; i = i + 2) {
            ProposalToArbiter game = new ProposalToArbiter(currentPlayers.get(i),
                    currentPlayers.get(i + 1), numRoundsPlayed, numRounds);
            ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
            try {
                msg.setContentObject(game);
                msg.addReceiver(arbiterAgents[i / 2]);
                getAgent().send(msg);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        currentPlayers = new ArrayList<AID>();
    }

    private static double log2(int x) {
        return Math.log(x) / Math.log(2);
    }

}
