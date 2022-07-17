package Jade.Messages;

import java.io.Serializable;

import jade.core.AID;

public class ProposalToPlayer implements Serializable {
    private boolean firstToPlay;
    private String symbol;
    private AID opponent;
    private int totalRounds;

    public ProposalToPlayer(AID opponent, boolean firstToPlay, String symbol, int totalRounds) {
        this.firstToPlay = firstToPlay;
        this.symbol = symbol;
        this.opponent = opponent;
        this.totalRounds = totalRounds;
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

    public int getTotalRounds() {
        return totalRounds;
    }
}
