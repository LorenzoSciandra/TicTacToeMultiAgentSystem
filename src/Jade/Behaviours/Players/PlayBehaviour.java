package Jade.Behaviours.Players;
import jade.core.behaviours.*;
import Jade.Agents.*;
import Jade.Messages.*;
import jade.domain.DFService;
import jade.core.AID;
import jade.domain.FIPAException;
import Jade.Behaviours.Players.*;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class PlayBehaviour extends OneShotBehaviour {
    public void action() {
        // Get a random free position from the 3x3 grid (0,1,2) x (0,1,2)
        int row = (int) (Math.random() * 3);
        int col = (int) (Math.random() * 3);
        while (!((StupidPlayer)getAgent()).getGrid().setCell(row, col, ((StupidPlayer)getAgent()).getSymbol())) {
            row = (int) (Math.random() * 3);
            col = (int) (Math.random() * 3);
        }
        // Send the message to the arbiter
        ACLMessage msg = new GridMessage(ACLMessage.INFORM, ((StupidPlayer)getAgent()).getGrid());
        msg.addReceiver(((StupidPlayer)getAgent()).getArbiterAID());
        getAgent().send(msg);
    }
}