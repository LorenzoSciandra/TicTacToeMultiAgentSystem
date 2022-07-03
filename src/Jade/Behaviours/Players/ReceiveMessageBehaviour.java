package Jade.Behaviours.Players;
import Jade.*;
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


public class ReceiveMessageBehaviour extends CyclicBehaviour {
    // TODO: metterla in un file a parte
    // TODO: fare sì che implementi la logica dell'agente

    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        GridMessage msg = (GridMessage) myAgent.receive(mt);
        if (msg != null) {
          Grid receivedGrid = msg.getGrid();
          ((StupidPlayer) getAgent()).setGrid(receivedGrid);
          getAgent().addBehaviour(new PlayBehaviour());
        } else {
          block();
        }
      }
}
