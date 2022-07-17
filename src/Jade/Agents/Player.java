package Jade.Agents;

import Jade.Grid;
import jade.core.AID;
import jade.core.Agent;

public class Player extends Agent{
    // These are all fake methods just to make the code compile.

    private Grid myGrid;
    private AID myArbiter;
    private AID myOpponent;
    private String mySymbol;
    private Boolean start;
    private Boolean stupid;
    private int totalRounds;
    private int round = 0;

    public AID getPlayerAID() {
        return getAID();
    }

    public AID getArbiterAID() {
        return myArbiter;
    }

    public AID getOpponentAID() {
        return myOpponent;
    }

    public String getSymbol() {
        return mySymbol;
    }

    public Grid getGrid() {
        return myGrid;
    }

    public void setSymbol(String symbol) {
        this.mySymbol = symbol;
    }

    public void setOpponent(AID opponent) {
        this.myOpponent = opponent;
    }

    public void setArbiter(AID arbiter) {
        this.myArbiter = arbiter;
    }

    public void setGrid(Grid grid) {
        this.myGrid = grid;
    }

    public void setStart(Boolean start) {
        this.start = start;
    }

    public Boolean getStart() {
        return start;
    }

    public void setStupid(Boolean stupid) {
        this.stupid = stupid;
    }

    public Boolean getStupid() {
        return stupid;
    }

    public void setTotalRounds(int totalRounds) {
        this.totalRounds = totalRounds;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }
}
