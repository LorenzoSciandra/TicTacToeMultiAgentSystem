package Jade.Messages;

import java.io.Serializable;

import Jade.*;
import jade.core.AID;

public class GridMessage implements Serializable {
    private Grid grid;
    private Boolean theresAWinner;
    private String winnerSymbol;
    private AID winnerAID;
    private boolean restartGame;

    public GridMessage(Grid grid) {
        this.grid = grid;
        this.theresAWinner = grid.isWinner();
        this.winnerSymbol = grid.getWinner();
        this.restartGame = grid.isFull();
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

    public boolean isRestartGame() {
        return restartGame;
    }

}
