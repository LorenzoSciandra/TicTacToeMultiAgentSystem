package Jade.Behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class EndGameBehaviour extends CyclicBehaviour {

    /**
     * This behaviour is called when a game has to end.
     * It waits for an END message from the Master Arbiter and terminates the agent
     * that has this behaviour accordingly.
     */
    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CONFIRM);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null && msg.getContent().equals("END")) {
            myAgent.doDelete();
        } else {
            block();
        }
    }

}
