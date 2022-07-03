package Jade.Behaviours.Arbiter;
import jade.core.behaviours.*;
import Jade.Messages.ProposalToArbiter;
import Jade.Messages.ProposalToPlayer;
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
            ((ArbiterAgent) getAgent()).setNumRound(msg.getRound());
            ((ArbiterAgent) getAgent()).setMasterArbiter(msg.getSender());

            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
			reply.setContent("OK, I accept to check the game between " + ((ArbiterAgent) getAgent()).getFirstPlayer().getName() + " and " + ((ArbiterAgent) getAgent()).getSecondPlayer().getName() + ".");
            getAgent().send(reply);

            ProposalToPlayer firstPlayerMsg = new ProposalToPlayer(ACLMessage.PROPOSE, msg.getSecondPlayer(), true, "X");
            ProposalToPlayer secondPlayerMsg = new ProposalToPlayer(ACLMessage.PROPOSE, msg.getFirstPlayer(), false, "O");
            firstPlayerMsg.addReceiver(msg.getFirstPlayer());
            secondPlayerMsg.addReceiver(msg.getSecondPlayer());
            getAgent().send(firstPlayerMsg);
            getAgent().send(secondPlayerMsg);
            getAgent().addBehaviour(new GameToCheckBehaviour());
        }
        else {
            block();
        }        
    }
}