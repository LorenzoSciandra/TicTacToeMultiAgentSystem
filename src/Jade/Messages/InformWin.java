package Jade.Messages;

import java.io.Serializable;

import jade.core.AID;

public class InformWin implements Serializable {

    private AID winner;

    public InformWin(AID winner) {
        this.winner = winner;
    }

    public AID getWinner() {
        return winner;
    }
}