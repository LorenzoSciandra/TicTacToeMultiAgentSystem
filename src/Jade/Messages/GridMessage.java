package Jade.Messages;
import Jade.*;

import jade.lang.acl.ACLMessage;

public class GridMessage extends ACLMessage {
    private Grid grid;

    public GridMessage(int performative, Grid grid) {
        super(performative);
        this.grid = grid;
    }

    public Grid getGrid() {
        return grid;
    }
}
