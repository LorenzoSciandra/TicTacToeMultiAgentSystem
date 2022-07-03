package Jade;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

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
        this.step = 0;
    }

    @Override
    public void action() {
        if (numPlayers % 2 == 0 && numArbiters >= numRounds) {
            if (step == 0) {
                int currentPlayer = 0;
                for (int i = 0; i < arbiterAgents.length; ++i) {
                    ACLMessage cfp = new ACLMessage(ACLMessage.PROPOSE);
                    cfp.addReceiver(arbiterAgents[i]);
                    String message = playerAgents[currentPlayer] + "," + playerAgents[currentPlayer + 1];
                    currentPlayer += 2;
                    cfp.setContent(message);
                    cfp.setConversationId("round " + numRoundsPlayed);
                }
                step++;
            } else {
                mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage msg = getAgent().receive(mt);
                if (msg != null) {
                    // CFP Message received. Process it
                    String vincitore = msg.getContent();
                    AID winner = new AID(vincitore);
                    winnersRound.get(numRoundsPlayed).add(winner);
                    if (winnersRound.get(numRoundsPlayed).size() == numPlayers) {
                        numRoundsPlayed++;
                        winnersRound.add(new ArrayList<AID>());
                        step--;
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
        if (numRoundsPlayed == numRounds && winnersRound.get(numRoundsPlayed).size() == numPlayers) {
            ((MasterArbiterAgent) getAgent()).setWinner(winnersRound.get(numRoundsPlayed).get(0));
            return true;
        } else {
            return false;
        }
    }

}
