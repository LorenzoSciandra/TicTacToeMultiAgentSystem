package Jade;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jason.stdlib.queue.add;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.function.Consumer;

public class StupidPlayer extends Agent {
    private Grid myGrid;
    private AID myArbiter;
    private AID myOpponent;
    private String mySymbol;
    private MessageTemplate mt;

    protected void setup() {
        myGrid = new Grid();

        initializeThisPlayer();
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("StupidPlayer Agent " + getAID().getName() + " terminating.");
    }

    private void initializeThisPlayer() {
        addBehaviour(new RegisterBehaviour("stupid-player", "Stupid Player"));
        addBehaviour(new ReceiveOpponentBehaviour());
    }

    public AID getPlayerAID() {
        return getAID();
    }

    private class PlayBehaviour extends CyclicBehaviour {
        public void action() {
            // Get a random free position from the 3x3 grid (0,1,2) x (0,1,2)
            int row = (int) (Math.random() * 3);
            int col = (int) (Math.random() * 3);
            while (!myGrid.setCell(row, col, mySymbol)) {
                row = (int) (Math.random() * 3);
                col = (int) (Math.random() * 3);
            }
            // Send the message to the arbiter
            ACLMessage msg = new MyMessage(ACLMessage.INFORM, myGrid);
            msg.addReceiver(myArbiter);
            send(msg);
        }
    }

    private class ReceiveMessageBehaviour extends CyclicBehaviour {
        // TODO: metterla in un file a parte
        // TODO: fare sì che implementi la logica dell'agente
    
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
              // TODO: abbiamo appena ricevuto una mossa da parte di un altro giocatore
            } else {
              block();
            }
          }
    }
    
    private class ReceiveOpponentBehaviour extends OneShotBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ProposalToPlayer msg = (ProposalToPlayer) myAgent.receive(mt);
            if (msg != null) {
                myOpponent = msg.getOpponent();
                myArbiter = msg.getSender();
                System.out.println("StupidPlayer Agent " + getAID().getName() + " received the opponent " + myOpponent.getName());
                addBehaviour(new PlayBehaviour());
                addBehaviour(new ReceiveMessageBehaviour());
            } else {
                block();
            }
        }
    }
}


