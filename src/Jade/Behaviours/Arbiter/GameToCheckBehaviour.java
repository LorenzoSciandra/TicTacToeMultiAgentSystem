package Jade.Behaviours.Arbiter;

import java.io.IOException;

import Jade.Agents.ArbiterAgent;
import Jade.Messages.GridMessage;
import Jade.Messages.InformWin;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class GameToCheckBehaviour extends Behaviour {

    private boolean gameChecked = false;

    /**
     * This behaviour acts in a four way interaction with the player agents:
     * - The arbiter receives the GridMessage from a player regarding a legal move
     * that he wants to send to the other player
     * - The arbiter can notice that the move brings to a state of WIN, LOSE and
     * informs the two that the game is over
     * - The arbiter can notice that the move brings to a state of TIE and informs
     * the two that the game must go on
     * - If none of the above happens, the arbiter can continue the game, calling
     * the GameToCheckBehaviour again
     */
    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            gameChecked = true;
            GridMessage gridMessage;
            try {
                gridMessage = (GridMessage) msg.getContentObject();
                AID firstPlayer = msg.getSender();
                AID secondPlayer = otherAid(firstPlayer);
                ACLMessage sendMsg = new ACLMessage(ACLMessage.INFORM);
                GridMessage gridMessageToSend = new GridMessage(gridMessage.getGrid());
                sendMsg.setContentObject(gridMessageToSend);
                sendMsg.addReceiver(secondPlayer);
                gridMessage.getGrid().printGrid();
                getAgent().send(sendMsg);

                if (gridMessageToSend.getGrid().isWinner()) {
                    communicateWinnerAndLoser(firstPlayer, secondPlayer, gridMessageToSend);
                } else if (gridMessageToSend.getGrid().isFull()) {
                    communicateTie(firstPlayer, secondPlayer);
                } else {
                    getAgent().addBehaviour(new GameToCheckBehaviour());
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * Communicates the winner and the loser of the game
     * to the two players, sending them an INFORM_IF message.
     * Then, start the WaitProposalArbiterBehaviour to wait for a new proposal
     * from the Master Arbiter.
     * @param playerWinner
     * @param playerLoser
     * @param gridMessage
     * @throws IOException
     */
    private void communicateWinnerAndLoser(AID playerWinner, AID playerLoser, GridMessage gridMessage)
            throws IOException {
        ACLMessage winnerMsg = new ACLMessage(ACLMessage.INFORM_IF);
        ACLMessage loserMsg = new ACLMessage(ACLMessage.INFORM_IF);
        winnerMsg.addReceiver(playerWinner);
        loserMsg.addReceiver(playerLoser);
        winnerMsg.setContent("WIN");
        loserMsg.setContent("LOSE");
        myAgent.send(winnerMsg);
        myAgent.send(loserMsg);
        AID masterArbiter = ((ArbiterAgent) getAgent()).getMasterArbiter();
        String winner = gridMessage.getWinnerSymbol();
        ACLMessage winnerMessage = new ACLMessage(ACLMessage.INFORM);
        winnerMessage.setContentObject(new InformWin(getAIDfromSymbol(winner)));
        winnerMessage.addReceiver(masterArbiter);
        ((ArbiterAgent) getAgent()).setWinner(gridMessage.getwinnerAid());
        getAgent().send(winnerMessage);
        getAgent().addBehaviour(new WaitProposalArbiterBehaviour());
    }

    /**
     * Communicates the tie to the two players, sending them an INFORM_IF message.
     * Then, start the GameToCheckBehaviour to check the game again.
     * @param player1
     * @param player2
     * @throws IOException
     */
    private void communicateTie(AID firstPlayer, AID secondPlayer) {
        ACLMessage tieMsg = new ACLMessage(ACLMessage.INFORM_IF);
        tieMsg.addReceiver(firstPlayer);
        tieMsg.addReceiver(secondPlayer);
        tieMsg.setContent("TIE");
        myAgent.send(tieMsg);
        getAgent().addBehaviour(new GameToCheckBehaviour());
    }

    
    /** 
     * Returns the opposing player AID of the given AID
     * @param aid
     * @return AID
     */
    private AID otherAid(AID aid) {
        if (aid.equals(((ArbiterAgent) getAgent()).getFirstPlayer())) {
            return ((ArbiterAgent) getAgent()).getSecondPlayer();
        } else {
            return ((ArbiterAgent) getAgent()).getFirstPlayer();
        }
    }

    
    /** 
     * Returns the AID of the player with the given symbol
     * @param symbol
     * @return AID
     */
    private AID getAIDfromSymbol(String symbol) {
        if (symbol.equals("X")) {
            return ((ArbiterAgent) getAgent()).getFirstPlayer();
        } else {
            return ((ArbiterAgent) getAgent()).getSecondPlayer();
        }
    }

    
    /** 
     * Returns true if the game has been checked, false otherwise
     * If the game has been checked, the behaviour is removed.
     * @return boolean
     */
    @Override
    public boolean done() {
        if (gameChecked) {
            getAgent().removeBehaviour(this);
        }
        return gameChecked;
    }

}
