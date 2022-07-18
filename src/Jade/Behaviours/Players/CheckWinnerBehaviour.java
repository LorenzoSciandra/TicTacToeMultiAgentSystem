package Jade.Behaviours.Players;

import Jade.Grid;
import Jade.Agents.Player;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class CheckWinnerBehaviour extends CyclicBehaviour {

    /**
     * Cyclic behaviour that checks if the player has won, lost or tied the game.
     * The message is received from the Arbiter agent.
     */
    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM_IF);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            switch (msg.getContent()) {
                case "WIN":
                    victory();
                    break;
                case "LOSE":
                    System.out.println("Agent " + getAgent().getAID().getName() + " ha perso.");
                    break;
                case "TIE":
                    tie();
                    break;
                default:
                    break;

            }
        } else {
            block();
        }
    }

    /**
     * Method that is called when the player has won the game.
     * If the tournament is not done, the player sits and waits for the next opponent
     * with the ReceiveOpponentBehaviour.
     */
    private void victory() {
        System.out.println("Agent " + getAgent().getAID().getName() + " ha vinto.");
        if (((Player) getAgent()).getRound() < ((Player) getAgent()).getTotalRounds() - 1) {
            ((Player) getAgent()).setGrid(new Grid());
            System.out.println("HO VINTO E ASPETTO IL PROSSIMO ROUND " + getAgent().getName());
            getAgent().addBehaviour(new ReceiveOpponentBehaviour(((Player) getAgent()).getStupid()));
        }
    }
    /**
     * Method that is called when the player has tied the game.
     * The tournament is not done, so he patiently waits to restart the game.
     */
    private void tie() {
        System.out.println("Agent " + getAgent().getAID().getName() + " ha pareggiato.");
        ((Player) getAgent()).setGrid(new Grid());
        if (((Player) getAgent()).getStart()) {
            getAgent().doWait(500);
            if (((Player) getAgent()).getStupid()) {
                getAgent().addBehaviour(new PlayBehaviour());
            } else {
                getAgent().addBehaviour(new IntelligentPlayBehaviour());
            }
        } else {
            getAgent().addBehaviour(new ReceiveMessageBehaviour());
        }
    }

}
