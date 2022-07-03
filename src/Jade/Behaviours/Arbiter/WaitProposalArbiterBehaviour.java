package Jade.Behaviours.Arbiter;
import jade.core.behaviours.*;
import Jade.Messages.ProposalToArbiter;
import Jade.Agents.ArbiterAgent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class WaitProposalArbiterBehaviour extends OneShotBehaviour {

    private MessageTemplate mt;

	@Override
    public void action() {
        mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
        ProposalToArbiter msg = (ProposalToArbiter) getAgent().receive(mt);
        if (msg != null) {
            // CFP Message received. Process it
            ((ArbiterAgent) getAgent()).setFirstPlayer(msg.getFirstPlayer());
            ((ArbiterAgent) getAgent()).setSecondPlayer(msg.getSecondPlayer());
            ((ArbiterAgent) getAgent()).setNumRounds(msg.getRound());
            getAgent().addBehaviour(new GameToCheckBehaviour());
        }
        else {
            block();
        }        
    }
}