package Jade;

import jade.lang.acl.ACLMessage;

public class MyMessage extends ACLMessage {
    private Grid grid;

    public MyMessage(int performative, Grid grid) {
        super(performative);
        this.grid = grid;
    }

    public Grid getGrid() {
        return grid;
    }
}
