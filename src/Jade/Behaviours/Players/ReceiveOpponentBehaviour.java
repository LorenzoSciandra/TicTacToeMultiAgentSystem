package Jade.Behaviours.Players;
import jade.core.behaviours.*;
import Jade.Behaviours.Players.*;
import Jade.Messages.*;
import jade.domain.DFService;
import jade.core.AID;
import jade.domain.FIPAException;
import Jade.Agents.StupidPlayerAgent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveOpponentBehaviour extends OneShotBehaviour {

    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
        ProposalToPlayer msg = (ProposalToPlayer) myAgent.receive(mt);
        if (msg != null) {
            ((StupidPlayerAgent) getAgent()).setOpponent(msg.getOpponent());
            ((StupidPlayerAgent) getAgent()).setArbiter(msg.getSender());
            ((StupidPlayerAgent) getAgent()).setSymbol(msg.getSymbol());
            ((StupidPlayerAgent) getAgent()).setStart(msg.isFirstToPlay());
            System.out.println("StupidPlayerAgent " + getAgent().getAID().getName() + " received the opponent " + ((StupidPlayerAgent) getAgent()).getOpponentAID().getName() + " and the arbiter " + ((StupidPlayerAgent) getAgent()).getArbiterAID().getName() + ".");
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
			reply.setContent("OK, I accept to play the game against " + ((StupidPlayerAgent) getAgent()).getOpponentAID().getName() + " with arbiter " + ((StupidPlayerAgent) getAgent()).getArbiterAID().getName() + ".");
            getAgent().send(reply);
            getAgent().addBehaviour(new PlayBehaviour());
        } else {
            block();
        }
    }
}