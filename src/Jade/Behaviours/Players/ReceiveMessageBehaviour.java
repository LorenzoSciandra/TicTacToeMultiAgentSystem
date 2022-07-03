package Jade.Behaviours.Players;
import Jade.*;
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


public class ReceiveMessageBehaviour extends OneShotBehaviour {

    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        GridMessage msg = (GridMessage) myAgent.receive(mt);
        if (msg != null && msg.getSender()== ((StupidPlayerAgent) getAgent()).getArbiterAID() ) {

          if(msg.getTheresAWinner()){
            System.out.println("StupidPlayer Agent " + getAgent().getAID().getName() + " received the message that the game is over.");

            if(msg.getWinner() == ((StupidPlayerAgent) getAgent()).getAID()){
              System.out.println("StupidPlayer Agent " + getAgent().getAID().getName() + " won the game.");
              // TODO: NUOVO COMPORTAMENTO PER RESETTARE LA GRID E RIMETTERSI IN ATTESA
            }
            else{
              System.out.println("StupidPlayer Agent " + getAgent().getAID().getName() + " lost the game.");
              getAgent().doDelete();
            }
          }
          else{
            System.out.println("StupidPlayer Agent " + getAgent().getAID().getName() + " received the message that the opponent played.");
            Grid receivedGrid = msg.getGrid();
            ((StupidPlayerAgent) getAgent()).setGrid(receivedGrid);
            getAgent().addBehaviour(new PlayBehaviour());
          }
        } else {
          block();
        }
      }
}
