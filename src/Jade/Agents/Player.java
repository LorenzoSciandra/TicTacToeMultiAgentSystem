package Jade.Agents;

import Jade.Grid;
import jade.core.AID;
import jade.core.Agent;

public class Player extends Agent{
    // These are all fake methods just to make the code compile.

    public AID getPlayerAID() {
        return getAID();
    }

    public AID getArbiterAID() {
        return getAID();
    }

    public AID getOpponentAID() {
        return getAID();
    }

    public String getSymbol() {
        return "";
    }

    public Grid getGrid() {
        return new Grid();
    }

    public void setSymbol(String symbol) {}

    public void setOpponent(AID opponent) {}

    public void setArbiter(AID arbiter) {}

    public void setGrid(Grid grid) {}

    public void setStart(Boolean start) {}

    public Boolean getStart() {
        return false;
    }
}
