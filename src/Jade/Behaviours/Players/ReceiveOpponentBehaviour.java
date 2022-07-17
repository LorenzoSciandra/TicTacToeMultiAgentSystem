package Jade.Behaviours.Players;

import jade.core.behaviours.*;
import Jade.Messages.*;
import Jade.Agents.Player;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class ReceiveOpponentBehaviour extends CyclicBehaviour {
    private boolean stupid;

    public ReceiveOpponentBehaviour(boolean stupid) {
        this.stupid = stupid;
    }

    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            ProposalToPlayer content;
            try {
                content = (ProposalToPlayer) msg.getContentObject();
                ((Player) getAgent()).setOpponent(content.getOpponent());
                ((Player) getAgent()).setArbiter(msg.getSender());
                ((Player) getAgent()).setSymbol(content.getSymbol());
                ((Player) getAgent()).setStart(content.isFirstToPlay());
                ((Player) getAgent()).setTotalRounds(content.getTotalRounds());

                System.out.println("Agent " + getAgent().getAID().getName() + " received the opponent "
                        + ((Player) getAgent()).getOpponentAID().getName() + " and the arbiter "
                        + ((Player) getAgent()).getArbiterAID().getName() + ".");
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                reply.setContent("OK, I accept to play the game against "
                        + ((Player) getAgent()).getOpponentAID().getName() + " with arbiter "
                        + ((Player) getAgent()).getArbiterAID().getName() + ".");
                getAgent().send(reply);
                if (((Player) getAgent()).getStart()) {
                    if (stupid) {
                        getAgent().addBehaviour(new PlayBehaviour());
                    } else {
                        getAgent().addBehaviour(new IntelligentPlayBehaviour());
                    }
                } else {
                    getAgent().addBehaviour(new ReceiveMessageBehaviour());
                }
            } catch (UnreadableException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            getAgent().removeBehaviour(this);
        } else {
            block();
        }
    }
}