package Jade.Behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class EndGameBehaviour extends CyclicBehaviour {

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null && msg.getContent().equals("END")) {
            myAgent.doDelete();
        } else {
            block();
        }
    }

}
