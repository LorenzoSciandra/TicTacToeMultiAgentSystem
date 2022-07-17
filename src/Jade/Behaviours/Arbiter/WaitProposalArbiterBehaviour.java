package Jade.Behaviours.Arbiter;

import jade.core.behaviours.*;
import Jade.Messages.ProposalToArbiter;
import Jade.Messages.ProposalToPlayer;

import Jade.Agents.ArbiterAgent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitProposalArbiterBehaviour extends Behaviour {

    private MessageTemplate mt;
    private boolean proposal = false;

    @Override
    public void action() {
        mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
        ACLMessage msg = getAgent().receive(mt);
        if (msg != null) {
            // CFP Message received. Process it
            proposal = true;
            ProposalToArbiter proposal;
            try {
                proposal = (ProposalToArbiter) msg.getContentObject();
                ((ArbiterAgent) getAgent()).setFirstPlayer(proposal.getFirstPlayer());
                ((ArbiterAgent) getAgent()).setSecondPlayer(proposal.getSecondPlayer());
                ((ArbiterAgent) getAgent()).setNumRound(proposal.getRound());
                ((ArbiterAgent) getAgent()).setMasterArbiter(msg.getSender());
                ((ArbiterAgent) getAgent()).setTotalRounds(proposal.getTotalRounds());

                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                reply.setContent("OK, I accept to check the game between "
                        + ((ArbiterAgent) getAgent()).getFirstPlayer().getName() + " and "
                        + ((ArbiterAgent) getAgent()).getSecondPlayer().getName() + ".");
                getAgent().send(reply);

                ACLMessage firstPlayerMsg = new ACLMessage(ACLMessage.PROPOSE);
                firstPlayerMsg.setContentObject(new ProposalToPlayer(proposal.getSecondPlayer(), true, "X", proposal.getTotalRounds()));
                ACLMessage secondPlayerMsg = new ACLMessage(ACLMessage.PROPOSE);
                secondPlayerMsg.setContentObject(new ProposalToPlayer(proposal.getFirstPlayer(), false, "O", proposal.getTotalRounds()));
                firstPlayerMsg.addReceiver(proposal.getFirstPlayer());
                secondPlayerMsg.addReceiver(proposal.getSecondPlayer());
                System.out.println("I GIOCATORI SARANNO: " + proposal.getFirstPlayer().getName() + " e "
                        + proposal.getSecondPlayer().getName());
                getAgent().send(firstPlayerMsg);
                getAgent().send(secondPlayerMsg);
                getAgent().addBehaviour(new GameToCheckBehaviour());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean done() {
        if (proposal) {
            getAgent().removeBehaviour(this);
        }
        return proposal;
    }
}