package Jade.Behaviours.Players;

import java.io.IOException;
import java.util.ArrayList;

import Jade.Agents.IntelligentPlayerAgent;
import Jade.Messages.GridMessage;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class IntelligentPlayBehaviour extends Behaviour {

    private boolean mossaFatta = false;

    /**
     * Behaviour of the intelligent player.
     * When a message is received from the arbiter (at any turn in the game)
     * the player decides its move, then it sends it to the arbiter.
     */
    public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        GridMessage content = new GridMessage(((IntelligentPlayerAgent) getAgent()).getGrid());
        try {
            decideMove();
            System.out.println("Agent " + getAgent().getAID().getName() + " ha scelto la mossa");
            mossaFatta = true;
            msg.setContentObject(content);
            msg.addReceiver(((IntelligentPlayerAgent) getAgent()).getArbiterAID());
            getAgent().send(msg);
            getAgent().addBehaviour(new ReceiveMessageBehaviour());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that decides the move of the player.
     * Differentiates between the turns number with cases.
     */
    public void decideMove() {
        int turn = 10 - ((IntelligentPlayerAgent) getAgent()).getGrid().getFreeCells().size();
        switch (turn) {
            case 1:
                firstTurn();
                break;
            case 2:
                secondTurn();
                break;
            case 3:
                thirdTurn();
                break;
            default:
                otherTurns();
                break;
        }
    }

    private void firstTurn() {
        // If it's the first turn, we want to start in a random corner, so:
        // (0,0), (0,2), (2,0), (2,2)
        // First, we randomly select 2 or 0 to be the row, then we randomly select 0 or
        // 2 to be the column
        int[] row = { 0, 2 };
        int[] col = { 0, 2 };
        int rowIndex = (int) (Math.random() * 2);
        int colIndex = (int) (Math.random() * 2);
        int rowValue = row[rowIndex];
        int colValue = col[colIndex];
        // Then we set the cell in the grid (it will be always free because it's the
        // first turn)
        ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(rowValue, colValue,
                ((IntelligentPlayerAgent) getAgent()).getSymbol());
    }

    private void secondTurn() {
        // If it's the second turn, another agent has already played somewhere. We want
        // to place, if it's free, the spot in the center
        // If it's not free, we want to place the spot in the corner.
        // First, we check if the center is free
        if (((IntelligentPlayerAgent) getAgent()).getGrid().isLegal(1, 1)) {
            ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(1, 1,
                    ((IntelligentPlayerAgent) getAgent()).getSymbol());
        } else {
            // If it's not free, we check if a corner is free
            if (((IntelligentPlayerAgent) getAgent()).getGrid().isLegal(0, 0)) {
                ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(0, 0,
                        ((IntelligentPlayerAgent) getAgent()).getSymbol());
            } else if (((IntelligentPlayerAgent) getAgent()).getGrid().isLegal(0, 2)) {
                ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(0, 2,
                        ((IntelligentPlayerAgent) getAgent()).getSymbol());
            } else if (((IntelligentPlayerAgent) getAgent()).getGrid().isLegal(2, 0)) {
                ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(2, 0,
                        ((IntelligentPlayerAgent) getAgent()).getSymbol());
            } else if (((IntelligentPlayerAgent) getAgent()).getGrid().isLegal(2, 2)) {
                ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(2, 2,
                        ((IntelligentPlayerAgent) getAgent()).getSymbol());
            }
        }
    }

    private void thirdTurn() {
        // If it's the third turn, we have occupied a corner for sure! We want to place
        // the symbol in the opposite corner
        // And if it's not free, we choose the center. And if it's not free, we choose a
        // random spot from the freeCells list.
        // First, we check what is the corner that is occupied (use
        // isMySymbolThere(row,col,symbol) method)
        String mySymbol = ((IntelligentPlayerAgent) getAgent()).getSymbol();
        int row = 1;
        int col = 1;
        if (((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(0, 0, mySymbol)) {
            row = 2;
            col = 2;
        } else if (((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(0, 2, mySymbol)) {
            row = 2;
            col = 0;
        } else if (((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(2, 0, mySymbol)) {
            row = 0;
            col = 2;
        } else if (((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(2, 2, mySymbol)) {
            row = 0;
            col = 0;
        }
        if (row != 1 && col != 1) {
            ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(row, col,
                    ((IntelligentPlayerAgent) getAgent()).getSymbol());
        } else {
            // If the center is not free, we choose a random free spot in the grid
            if (!((IntelligentPlayerAgent) getAgent()).getGrid().setCell(row, col,
                    ((IntelligentPlayerAgent) getAgent()).getSymbol())) {
                ArrayList<Integer> freeCells = (ArrayList<Integer>) ((IntelligentPlayerAgent) getAgent()).getGrid()
                        .getFreeCells();
                int randomIndex = (int) (Math.random() * freeCells.size());
                ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(freeCells.get(randomIndex),
                        ((IntelligentPlayerAgent) getAgent()).getSymbol());

            }
        }

    }

    private void otherTurns() {
        // In any other turn, we want to check if we can win horizontally, vertically or
        // diagonally.
        // If we can, we want to place the symbol in the winning position.
        // If we can't, we want to check if we can block the opponent from winning.

        String mySymbol = ((IntelligentPlayerAgent) getAgent()).getSymbol();
        String myOpposingSymbol = "";
        if (mySymbol.equals("X")) {
            myOpposingSymbol = "O";
        } else {
            myOpposingSymbol = "X";
        }
        // First, we check if we can win horizontally
        if (!checkHorizontal(mySymbol))
            // If we can't win horizontally, we check if we can win vertically
            if (!checkVertical(mySymbol))
                // If we can't win vertically, we check if we can win diagonally
                if (!checkDiagonal(mySymbol))
                    // If we can't win diagonally, we check if we can block the opponent from
                    // winning.
                    // First, we check if we can block the opponent from winning horizontally
                    if (!checkHorizontal(myOpposingSymbol))
                        // If we can't block the opponent from winning horizontally, we check if we can
                        // block the opponent from winning vertically
                        if (!checkVertical(myOpposingSymbol))
                            // If we can't block the opponent from winning vertically, we check if we can
                            // block the opponent from winning diagonally
                            if (!checkDiagonal(myOpposingSymbol)) {
                                // If we can't block the opponent from winning diagonally, we do a random legal
                                // move
                                ArrayList<Integer> freeCells = (ArrayList<Integer>) ((IntelligentPlayerAgent) getAgent())
                                        .getGrid().getFreeCells();
                                int randomIndex = (int) (Math.random() * freeCells.size());
                                ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(freeCells.get(randomIndex),
                                        ((IntelligentPlayerAgent) getAgent()).getSymbol());
                            }
    }

    private boolean checkHorizontal(String symbol) {
        for (int i = 0; i < 3; i++) {
            if (((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(i, 0, symbol)
                    && ((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(i, 1, symbol)
                    && ((IntelligentPlayerAgent) getAgent()).getGrid().isFree(i, 2)) {
                ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(i, 2,
                        ((IntelligentPlayerAgent) getAgent()).getSymbol());
                return true;
            } else if (((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(i, 1, symbol)
                    && ((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(i, 2, symbol)
                    && ((IntelligentPlayerAgent) getAgent()).getGrid().isFree(i, 0)) {
                ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(i, 0,
                        ((IntelligentPlayerAgent) getAgent()).getSymbol());
                return true;
            } else if (((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(i, 0, symbol)
                    && ((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(i, 2, symbol)
                    && ((IntelligentPlayerAgent) getAgent()).getGrid().isFree(i, 1)) {
                ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(i, 1,
                        ((IntelligentPlayerAgent) getAgent()).getSymbol());
                return true;
            }
        }
        return false;
    }

    private boolean checkVertical(String symbol) {
        for (int i = 0; i < 3; i++) {
            if (((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(0, i, symbol)
                    && ((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(1, i, symbol)
                    && ((IntelligentPlayerAgent) getAgent()).getGrid().isFree(2, i)) {
                ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(2, i,
                        ((IntelligentPlayerAgent) getAgent()).getSymbol());
                return true;
            } else if (((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(1, i, symbol)
                    && ((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(2, i, symbol)
                    && ((IntelligentPlayerAgent) getAgent()).getGrid().isFree(0, i)) {
                ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(0, i,
                        ((IntelligentPlayerAgent) getAgent()).getSymbol());
                return true;
            } else if (((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(0, i, symbol)
                    && ((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(2, i, symbol)
                    && ((IntelligentPlayerAgent) getAgent()).getGrid().isFree(1, i)) {
                ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(1, i,
                        ((IntelligentPlayerAgent) getAgent()).getSymbol());
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonal(String symbol) {
        if (((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(0, 0, symbol)
                && ((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(1, 1, symbol)
                && ((IntelligentPlayerAgent) getAgent()).getGrid().isFree(2, 2)) {
            ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(2, 2,
                    ((IntelligentPlayerAgent) getAgent()).getSymbol());
            return true;
        } else if (((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(1, 1, symbol)
                && ((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(2, 2, symbol)
                && ((IntelligentPlayerAgent) getAgent()).getGrid().isFree(0, 0)) {
            ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(0, 0,
                    ((IntelligentPlayerAgent) getAgent()).getSymbol());
            return true;
        } else if (((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(0, 0, symbol)
                && ((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(2, 2, symbol)
                && ((IntelligentPlayerAgent) getAgent()).getGrid().isFree(1, 1)) {
            ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(1, 1,
                    ((IntelligentPlayerAgent) getAgent()).getSymbol());
            return true;
        } else if (((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(0, 2, symbol)
                && ((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(1, 1, symbol)
                && ((IntelligentPlayerAgent) getAgent()).getGrid().isFree(2, 0)) {
            ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(2, 0,
                    ((IntelligentPlayerAgent) getAgent()).getSymbol());
            return true;
        } else if (((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(1, 1, symbol)
                && ((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(2, 0, symbol)
                && ((IntelligentPlayerAgent) getAgent()).getGrid().isFree(0, 2)) {
            ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(0, 2,
                    ((IntelligentPlayerAgent) getAgent()).getSymbol());
            return true;
        } else if (((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(0, 2, symbol)
                && ((IntelligentPlayerAgent) getAgent()).getGrid().isMySymbolThere(2, 0, symbol)
                && ((IntelligentPlayerAgent) getAgent()).getGrid().isFree(1, 1)) {
            ((IntelligentPlayerAgent) getAgent()).getGrid().setCell(1, 1,
                    ((IntelligentPlayerAgent) getAgent()).getSymbol());
            return true;
        }
        return false;
    }

    @Override
    public boolean done() {
        if (mossaFatta) {
            getAgent().removeBehaviour(this);
        }
        return mossaFatta;
    }
}
