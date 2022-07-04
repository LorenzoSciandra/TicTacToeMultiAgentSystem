package Jade.Behaviours.Players;

import jade.core.behaviours.*;
import Jade.Messages.*;
import Jade.Agents.Player;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveOpponentBehaviour extends OneShotBehaviour {
    private boolean stupid;

    public ReceiveOpponentBehaviour(boolean stupid) {
        this.stupid = stupid;
    }

    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
        ProposalToPlayer msg = (ProposalToPlayer) myAgent.receive(mt);
        if (msg != null) {
            ((Player) getAgent()).setOpponent(msg.getOpponent());
            ((Player) getAgent()).setArbiter(msg.getSender());
            ((Player) getAgent()).setSymbol(msg.getSymbol());
            ((Player) getAgent()).setStart(msg.isFirstToPlay());
            System.out.println("StupidPlayerAgent " + getAgent().getAID().getName() + " received the opponent "
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
                }
                else {
                    getAgent().addBehaviour(new IntelligentPlayBehaviour());
                }
            } else {
                getAgent().addBehaviour(new ReceiveMessageBehaviour());
            }
        } else {
            block();
        }
    }
}