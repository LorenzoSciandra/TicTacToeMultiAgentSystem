package Jade.Messages;
import Jade.*;
import jade.core.AID;

import jade.lang.acl.ACLMessage;

public class GridMessage extends ACLMessage {
    private Grid grid;
    private Boolean theresAWinner;
    private AID winner;

    public GridMessage(int performative, Grid grid) {
        super(performative);
        this.grid = grid;
        this.theresAWinner = grid.isWinner();
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Boolean getTheresAWinner() {
        return theresAWinner;
    }

    public void setTheresAWinner(Boolean theresAWinner) {
        this.theresAWinner = theresAWinner;
    }

    public AID getWinner() {
        return winner;
    }

    public void setWinner(AID winner) {
        this.winner = winner;
    }
}
