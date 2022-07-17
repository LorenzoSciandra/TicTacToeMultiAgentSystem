package Jade.Messages;

import java.io.Serializable;

import jade.core.AID;

public class ProposalToPlayer implements Serializable {
    private boolean firstToPlay;
    private String symbol;
    private AID opponent;

    public ProposalToPlayer(AID opponent, boolean firstToPlay, String symbol) {
        this.firstToPlay = firstToPlay;
        this.symbol = symbol;
        this.opponent = opponent;
    }

    public boolean isFirstToPlay() {
        return firstToPlay;
    }

    public String getSymbol() {
        return symbol;
    }

    public AID getOpponent() {
        return opponent;
    }
}
