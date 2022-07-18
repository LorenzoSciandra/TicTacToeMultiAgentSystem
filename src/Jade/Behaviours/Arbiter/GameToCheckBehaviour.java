package Jade.Behaviours.Arbiter;

import Jade.Agents.ArbiterAgent;
import Jade.Behaviours.EndGameBehaviour;
import Jade.Messages.*;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class GameToCheckBehaviour extends Behaviour {

    private boolean gameChecked = false;

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            gameChecked = true;
            GridMessage gridMessage;
            try {
                gridMessage = (GridMessage) msg.getContentObject();
                AID sender = msg.getSender();
                AID otherPlayer = otherAid(sender);
                ACLMessage sendMsg = new ACLMessage(ACLMessage.INFORM);
                GridMessage gridMessageToSend = new GridMessage(gridMessage.getGrid());

                sendMsg.setContentObject(gridMessageToSend);
                sendMsg.addReceiver(otherPlayer);
                gridMessage.getGrid().printGrid();
                getAgent().send(sendMsg);
                if (gridMessageToSend.getGrid().isWinner()) {
                    ACLMessage winnerMsg = new ACLMessage(ACLMessage.INFORM_IF);
                    ACLMessage loserMsg = new ACLMessage(ACLMessage.INFORM_IF);
                    winnerMsg.addReceiver(sender);
                    loserMsg.addReceiver(otherPlayer);
                    winnerMsg.setContent("WIN");
                    loserMsg.setContent("LOSE");
                    myAgent.send(winnerMsg);
                    myAgent.send(loserMsg);
                    AID masterArbiter = ((ArbiterAgent) getAgent()).getMasterArbiter();
                    String winner = gridMessageToSend.getWinnerSymbol();
                    ACLMessage winnerMessage = new ACLMessage(ACLMessage.INFORM);
                    winnerMessage.setContentObject(new InformWin(getAIDfromSymbol(winner)));
                    winnerMessage.addReceiver(masterArbiter);
                    ((ArbiterAgent)getAgent()).setWinner(gridMessage.getwinnerAid());
                    getAgent().send(winnerMessage);
                    getAgent().addBehaviour(new WaitProposalArbiterBehaviour());
                } else if (gridMessageToSend.getGrid().isFull()) {
                    ACLMessage tieMsg = new ACLMessage(ACLMessage.INFORM_IF);
                    tieMsg.addReceiver(sender);
                    tieMsg.addReceiver(otherPlayer);
                    tieMsg.setContent("TIE");
                    myAgent.send(tieMsg);
                    getAgent().addBehaviour(new GameToCheckBehaviour());
                } else {
                    getAgent().addBehaviour(new GameToCheckBehaviour());
                }
            } catch (Exception e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
        }
    }

    private AID otherAid(AID aid) {
        if (aid.equals(((ArbiterAgent) getAgent()).getFirstPlayer())) {
            return ((ArbiterAgent) getAgent()).getSecondPlayer();
        } else {
            return ((ArbiterAgent) getAgent()).getFirstPlayer();
        }
    }

    private AID getAIDfromSymbol(String symbol) {
        if (symbol.equals("X")) {
            return ((ArbiterAgent) getAgent()).getFirstPlayer();
        } else {
            return ((ArbiterAgent) getAgent()).getSecondPlayer();
        }
    }

    @Override
    public boolean done() {
        if (gameChecked) {
            getAgent().removeBehaviour(this);
        }
        return gameChecked;
    }

}
