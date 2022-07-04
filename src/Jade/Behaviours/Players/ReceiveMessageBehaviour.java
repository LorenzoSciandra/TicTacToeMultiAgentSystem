package Jade.Behaviours.Players;

import Jade.*;
import jade.core.behaviours.*;
import Jade.Messages.*;
import Jade.Agents.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveMessageBehaviour extends OneShotBehaviour {

  public void action() {
    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
    GridMessage msg = (GridMessage) getAgent().receive(mt);
    if (msg != null) {

      if (msg.getTheresAWinner()) {
        System.out.println(
            "StupidPlayer Agent " + getAgent().getAID().getName() + " received the message that the game is over.");

        if (msg.getWinnerSymbol() == ((Player) getAgent()).getSymbol()) {
          System.out.println("StupidPlayer Agent " + getAgent().getAID().getName() + " won the game.");
          ((Player) getAgent()).setGrid(new Grid());
          getAgent().addBehaviour(new ReceiveOpponentBehaviour(((Player) getAgent()).getStupid()));
        } else {
          System.out.println("StupidPlayer Agent " + getAgent().getAID().getName() + " lost the game.");
          getAgent().doDelete();
        }
      } else {
        System.out.println(
            "StupidPlayer Agent " + getAgent().getAID().getName() + " received the message that the opponent played.");
        Grid receivedGrid = msg.getGrid();
        ((Player) getAgent()).setGrid(receivedGrid);
        if(msg.getGrid().isEmpty()){
          ((Player) getAgent()).setGrid(new Grid());
          if (((Player) getAgent()).getStart()) {
            ((Player)getAgent()).setStart(false);
            ((Player)getAgent()).setSymbol("O");
            getAgent().addBehaviour(new ReceiveMessageBehaviour());
          } else {
            ((Player)getAgent()).setStart(true);
            ((Player)getAgent()).setSymbol("X");
            if (((Player) getAgent()).getStupid()) {
              getAgent().addBehaviour(new PlayBehaviour());
            } else {
              getAgent().addBehaviour(new IntelligentPlayBehaviour());
            }
          }
        }
      }
    } else {
      block();
    }
  }
}
