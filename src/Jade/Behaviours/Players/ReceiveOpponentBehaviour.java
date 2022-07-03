package Jade.Behaviours.Players;
import jade.core.behaviours.*;
import Jade.Behaviours.Players.*;
import Jade.Messages.*;
import jade.domain.DFService;
import jade.core.AID;
import jade.domain.FIPAException;
import Jade.Agents.StupidPlayer;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveOpponentBehaviour extends OneShotBehaviour {

    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        ProposalToPlayer msg = (ProposalToPlayer) myAgent.receive(mt);
        if (msg != null) {
            ((StupidPlayer) getAgent()).setOpponent(msg.getOpponent());
            ((StupidPlayer) getAgent()).setArbiter(msg.getSender());
            ((StupidPlayer) getAgent()).setSymbol(msg.getSymbol());
            ((StupidPlayer) getAgent()).setStart(msg.isFirstToPlay());
            System.out.println("StupidPlayer Agent " + getAgent().getAID().getName() + " received the opponent " + getAgent().getOpponent().getName() + " and the arbiter " + getAgent().getArbiter().getName() + ".");
            getAgent().addBehaviour(new PlayBehaviour());
            getAgent().addBehaviour(new ReceiveMessageBehaviour());
        } else {
            block();
        }
    }
}