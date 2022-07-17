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
        GridMessage content = new GridMessage(((StupidPlayerAgent) getAgent()).getGrid());
        try {
            decideMove();
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
        ArrayList<Integer> freeCells = (ArrayList<Integer>) ((StupidPlayerAgent) getAgent())
                .getGrid().getFreeCells();
        int randomIndex = (int) (Math.random() * freeCells.size());
        ((StupidPlayerAgent) getAgent()).getGrid().setCell(freeCells.get(randomIndex),
                ((StupidPlayerAgent) getAgent()).getSymbol());
    }

    @Override
    public boolean done() {
        if (done) {
            getAgent().removeBehaviour(this);
        }
        return done;
    }
}