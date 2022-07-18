package Jade.Behaviours.Arbiter;

import Jade.Agents.ArbiterAgent;
import Jade.Messages.ProposalToArbiter;
import Jade.Messages.ProposalToPlayer;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitProposalArbiterBehaviour extends Behaviour {

    private MessageTemplate mt;
    private boolean proposal = false;

    /**
     * When the arbiter receives the message from the Master Arbiter regarding a
     * game proposal (ACLMessage.PROPOSE), it can now start to arbiter the game.
     * First of all, it sends a PROPOSE message to the two players, asking them to
     * start a game, giving them their symbols and opponents information.
     * He then waits for the two players to respond to the proposal and to the
     * moves with the GameToCheckBehaviour.
     */
    @Override
    public void action() {
        mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
        ACLMessage msg = getAgent().receive(mt);
        if (msg != null) {
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
                reply.setContent("OK, Accetto di arbitrare il gioco tra "
                        + ((ArbiterAgent) getAgent()).getFirstPlayer().getName() + " e "
                        + ((ArbiterAgent) getAgent()).getSecondPlayer().getName() + ".");
                getAgent().send(reply);

                ACLMessage firstPlayerMsg = new ACLMessage(ACLMessage.PROPOSE);
                firstPlayerMsg.setContentObject(new ProposalToPlayer(proposal.getSecondPlayer(), true, "X",
                        proposal.getTotalRounds(), proposal.getRound()));
                ACLMessage secondPlayerMsg = new ACLMessage(ACLMessage.PROPOSE);
                secondPlayerMsg.setContentObject(new ProposalToPlayer(proposal.getFirstPlayer(), false, "O",
                        proposal.getTotalRounds(), proposal.getRound()));
                firstPlayerMsg.addReceiver(proposal.getFirstPlayer());
                secondPlayerMsg.addReceiver(proposal.getSecondPlayer());

                getAgent().send(firstPlayerMsg);
                getAgent().send(secondPlayerMsg);
                getAgent().addBehaviour(new GameToCheckBehaviour());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns true if the proposal has been received, false otherwise
     * @return boolean
     */
    @Override
    public boolean done() {
        if (proposal) {
            getAgent().removeBehaviour(this);
        }
        return proposal;
    }
}