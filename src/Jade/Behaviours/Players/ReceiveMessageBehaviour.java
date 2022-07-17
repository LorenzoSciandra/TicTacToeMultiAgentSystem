package Jade.Behaviours.Players;

import Jade.*;
import jade.core.behaviours.*;
import Jade.Messages.*;
import Jade.Agents.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class ReceiveMessageBehaviour extends Behaviour {

  private boolean messageReceived = false;

  public void action() {
    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
    ACLMessage msg = getAgent().receive(mt);
    if (msg != null) {
      GridMessage content;
      try {
        messageReceived = true;
        content = (GridMessage) msg.getContentObject();
        if (content.getTheresAWinner()) {
          System.out.println(
              "StupidPlayer Agent " + getAgent().getAID().getName() + " received the message that the game is over.");

          if (content.getWinnerSymbol() == ((Player) getAgent()).getSymbol()) {
            System.out.println("StupidPlayer Agent " + getAgent().getAID().getName() + " won the game.");
            ((Player) getAgent()).setGrid(new Grid());
            getAgent().addBehaviour(new ReceiveOpponentBehaviour(((Player) getAgent()).getStupid()));
          } else {
            System.out.println("StupidPlayer Agent " + getAgent().getAID().getName() + " lost the game.");
            getAgent().doDelete();
          }
        } else {
          System.out.println(
              "StupidPlayer Agent " + getAgent().getAID().getName()
                  + " received the message that the opponent played.");
          Grid receivedGrid = content.getGrid();
          ((Player) getAgent()).setGrid(receivedGrid);
          if (content.getGrid().isEmpty()) {
            ((Player) getAgent()).setGrid(new Grid());
            if (((Player) getAgent()).getStart()) {
              ((Player) getAgent()).setStart(false);
              ((Player) getAgent()).setSymbol("O");
              getAgent().addBehaviour(new ReceiveMessageBehaviour());
            } else {
              ((Player) getAgent()).setStart(true);
              ((Player) getAgent()).setSymbol("X");
              if (((Player) getAgent()).getStupid()) {
                // aspettiamo che anche l'altro player abbia resettato il tutto
                getAgent().doWait(300);
                getAgent().addBehaviour(new PlayBehaviour());
              } else {
                getAgent().addBehaviour(new IntelligentPlayBehaviour());
              }
            }
          } else {
            if (((Player) getAgent()).getStupid()) {
              getAgent().addBehaviour(new PlayBehaviour());
            } else {
              getAgent().addBehaviour(new IntelligentPlayBehaviour());
            }
          }
        }
      } catch (UnreadableException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  @Override
  public boolean done() {
    if (messageReceived) {
      getAgent().removeBehaviour(this);
    }
    return messageReceived;
  }
}
