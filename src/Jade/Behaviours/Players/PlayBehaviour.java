package Jade.Behaviours.Players;

import jade.core.behaviours.*;
import Jade.Agents.*;
import Jade.Messages.*;
import jade.lang.acl.ACLMessage;

public class PlayBehaviour extends OneShotBehaviour {
    public void action() {
        // Get a random free position from the 3x3 grid (0,1,2) x (0,1,2)
        int row = (int) (Math.random() * 3);
        int col = (int) (Math.random() * 3);
        while (!((StupidPlayerAgent) getAgent()).getGrid().setCell(row, col,
                ((StupidPlayerAgent) getAgent()).getSymbol())) {
            row = (int) (Math.random() * 3);
            col = (int) (Math.random() * 3);
        }
        // Send the message to the arbiter
        ACLMessage msg = new GridMessage(ACLMessage.INFORM, ((StupidPlayerAgent) getAgent()).getGrid());
        msg.addReceiver(((StupidPlayerAgent) getAgent()).getArbiterAID());
        getAgent().send(msg);
        getAgent().addBehaviour(new ReceiveMessageBehaviour());
    }
}