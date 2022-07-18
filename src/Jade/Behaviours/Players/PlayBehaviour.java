package Jade.Behaviours.Players;

import java.io.IOException;
import java.util.ArrayList;

import Jade.Agents.StupidPlayerAgent;
import Jade.Messages.GridMessage;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class PlayBehaviour extends Behaviour {

    private boolean done = false;

    /**
     * Behaviour of the stupid player.
     * When a message is received from the arbiter (at any turn in the game)
     * the player decides its move, then it sends it to the arbiter.
     */
    public void action() {
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
            e.printStackTrace();
        }
    }

    /**
     * Returns a random free position from the 3x3 grid (0,1,2) x (0,1,2)
     */
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