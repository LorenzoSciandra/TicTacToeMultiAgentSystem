package Jade.Messages;
import Jade.*;
import jade.core.AID;

import jade.lang.acl.ACLMessage;

public class GridMessage extends ACLMessage {
    private Grid grid;
    private Boolean theresAWinner;
    private String winnerSymbol;
    private AID winnerAID;

    public GridMessage(int performative, Grid grid) {
        super(performative);
        this.grid = grid;
        this.theresAWinner = grid.isWinner();
        this.winnerSymbol = grid.getWinner();
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

    public String getWinnerSymbol() {
        return winnerSymbol;
    }

    public AID getwinnerAid() {
        return winnerAID;
    }

    public void setWinnerAID(AID winnerAID) {
        this.winnerAID = winnerAID;
    }


}
