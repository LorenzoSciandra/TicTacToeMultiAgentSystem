package Jade.Behaviours.Players;

import jade.core.behaviours.*;

import java.io.IOException;
import java.util.ArrayList;

import Jade.Agents.*;
import Jade.Messages.*;
import jade.lang.acl.ACLMessage;

public class PlayBehaviour extends Behaviour {

    private boolean done = false;

    public void action() {
        // Get a random free position from the 3x3 grid (0,1,2) x (0,1,2)
        // Send the message to the arbiter
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        try {
            decideMove();
            System.out.println("Agent " + getAgent().getAID().getName() + " ha scelto la mossa");
            GridMessage content = new GridMessage(((StupidPlayerAgent) getAgent()).getGrid());
            done = true;
            msg.setContentObject(content);
            msg.addReceiver(((StupidPlayerAgent) getAgent()).getArbiterAID());
            getAgent().send(msg);
            getAgent().addBehaviour(new ReceiveMessageBehaviour());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void decideMove() {
        StupidPlayerAgent a = ((StupidPlayerAgent) getAgent());
        ArrayList<Integer> freeCells = (ArrayList<Integer>) a.getGrid().getFreeCells();
        int randomIndex = (int) (Math.random() * freeCells.size());
        a.getGrid().setCell(freeCells.get(randomIndex), a.getSymbol());
    }

    @Override
    public boolean done() {
        if (done) {
            getAgent().removeBehaviour(this);
        }
        return done;
    }
}