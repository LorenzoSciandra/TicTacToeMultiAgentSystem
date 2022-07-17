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

public class PlayGameBehaviour extends Behaviour {

    private AID[] arbiterAgents;
    private AID[] playerAgents;
    private int numPlayers;
    private int numArbiters;
    private int numRounds;
    private int numRoundsPlayed;
    private List<List<AID>> winnersRound;
    private int step;
    private MessageTemplate mt;

    public PlayGameBehaviour(AID[] arbiterAgents, AID[] playerAgents) {
        this.arbiterAgents = arbiterAgents;
        this.playerAgents = playerAgents;
        this.numPlayers = playerAgents.length;
        this.numArbiters = arbiterAgents.length;
        this.numRounds = (int) Math.ceil(this.numPlayers / 2);
        this.numRoundsPlayed = 0;
        this.winnersRound = new ArrayList<List<AID>>();
        for (int i = 0; i < this.numRounds; i++) {
            this.winnersRound.add(new ArrayList<AID>());
        }
        this.step = 0;
    }

    @Override
    public void action() {
        if (numPlayers % 2 == 0 && numArbiters >= numRounds) {
            if (step == 0) {
                System.out.println("Assegno i giocatori e gli arbitri per il round: " + numRoundsPlayed);
                int currentPlayer = 0;
                for (int i = 0; i < arbiterAgents.length; ++i) {
                    ProposalToArbiter game = new ProposalToArbiter(playerAgents[currentPlayer],
                            playerAgents[currentPlayer + 1], numRoundsPlayed);
                    ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
                    try {
                        msg.setContentObject(game);
                        msg.addReceiver(arbiterAgents[i]);
                        currentPlayer += 2;
                        getAgent().send(msg);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
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
                        winnersRound.get(numRoundsPlayed).add(winner);
                        if (winnersRound.get(numRoundsPlayed).size() == numPlayers) {
                            playerAgents = new AID[numPlayers];
                            for (int i = 0; i < numPlayers; ++i) {
                                playerAgents[i] = winnersRound.get(numRoundsPlayed).get(i);
                            }
                            winnersRound.add(new ArrayList<AID>());
                            step--;
                            // aspettiamo un attimo prima di far inziare il round successivo
                            getAgent().doWait(300);
                            System.out.println(
                                    "Procediamo con la fase successiva! Si giocherà a breve il round: "
                                            + numRoundsPlayed);
                        } numRoundsPlayed++;
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
            getAgent().removeBehaviour(this);
            getAgent().doDelete();
        }
    }

    @Override
    public boolean done() {
        boolean cond = numRoundsPlayed == numRounds;
        if (cond) {
            System.out.println("Fine del gioco!");
            ((MasterArbiterAgent) myAgent).setWinner(winnersRound.get(numRoundsPlayed - 1).get(0));
            
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
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

}
