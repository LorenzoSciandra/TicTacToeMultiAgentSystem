package Jade.Behaviours.Arbiter;

import Jade.Agents.ArbiterAgent;
import Jade.Messages.*;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class GameToCheckBehaviour extends OneShotBehaviour {

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        GridMessage msg = (GridMessage) getAgent().receive(mt);
        if (msg != null) {
            AID sender = msg.getSender();
            AID otherPlayer = otherAid(sender);
            ACLMessage sendMsg = new GridMessage(ACLMessage.INFORM, msg.getGrid());
            sendMsg.addReceiver(otherPlayer);
            getAgent().send(sendMsg);

            if (msg.getTheresAWinner()){
                AID masterArbiter = ((ArbiterAgent) getAgent()).getMasterArbiter();
                String winner = msg.getWinnerSymbol();
                ACLMessage winnerMessage = new InformWin(ACLMessage.INFORM, getAIDfromSymbol(winner));
                winnerMessage.addReceiver(masterArbiter);
                getAgent().send(winnerMessage);
                getAgent().addBehaviour(new WaitProposalArbiterBehaviour());
            }
            else{
                getAgent().addBehaviour(new GameToCheckBehaviour());
            }
            
        } else {
          block();
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
        if(symbol.equals("X")){
            return ((ArbiterAgent) getAgent()).getFirstPlayer();
        }
        else{
            return ((ArbiterAgent) getAgent()).getSecondPlayer();
        }
    }

}
