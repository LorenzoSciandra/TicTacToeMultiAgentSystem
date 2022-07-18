package Jade.Behaviours.Players;

import Jade.*;
import jade.core.behaviours.*;
import Jade.Messages.*;
import Jade.Agents.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class ReceiveMessageBehaviour extends Behaviour {

    private boolean messageReceived = false;

    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage msg = getAgent().receive(mt);
        if (msg != null && !msg.getContent().equals("END")) {
            GridMessage content;
            try {
                messageReceived = true;
                content = (GridMessage) msg.getContentObject();

                System.out.println("Agent " + getAgent().getAID().getName() + " ha ricevuto il messaggio e sta facendo la mossa...");
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
                // TODO Auto-generated catch block
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