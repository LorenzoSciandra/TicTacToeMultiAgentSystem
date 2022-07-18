package Jade.Messages;

import java.io.Serializable;

import jade.core.AID;

public class ProposalToPlayer implements Serializable {
    private boolean firstToPlay;
    private String symbol;
    private AID opponent;
    private int totalRounds;
    private int currentRound;

    public ProposalToPlayer(AID opponent, boolean firstToPlay, String symbol, int totalRounds, int currentRound) {
        this.firstToPlay = firstToPlay;
        this.symbol = symbol;
        this.opponent = opponent;
        this.totalRounds = totalRounds;
        this.currentRound = currentRound;
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

    public int getRound() {
        return currentRound;
    }
}
