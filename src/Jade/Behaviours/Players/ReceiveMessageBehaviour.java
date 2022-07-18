package Jade.Behaviours.Players;

import Jade.Grid;
import Jade.Agents.Player;
import Jade.Messages.GridMessage;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class ReceiveMessageBehaviour extends Behaviour {

    private boolean messageReceived = false;

    /**
     * When a message of a move is received, the agents starts the PlayBehaviour,
     * updating his grid with the new move from the opponent.
     */
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage msg = getAgent().receive(mt);
        if (msg != null && !msg.getContent().equals("END")) {
            GridMessage content;
            try {
                messageReceived = true;
                content = (GridMessage) msg.getContentObject();

                System.out.println("Agent " + getAgent().getAID().getName()
                        + " ha ricevuto il messaggio della mossa dell'avversario.");
                Grid receivedGrid = content.getGrid();
                ((Player) getAgent()).setGrid(receivedGrid);
                if (!(receivedGrid.isFull() || receivedGrid.isWinner())) {
                    if (((Player) getAgent()).getStupid()) {
                        getAgent().addBehaviour(new PlayBehaviour());
                    } else {
                        getAgent().addBehaviour(new IntelligentPlayBehaviour());
                    }
                }
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean done() {
        if (messageReceived) {
            getAgent().removeBehaviour(this);
        }
        return messageReceived;
    }
}