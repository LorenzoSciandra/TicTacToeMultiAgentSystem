package Jade.Messages;

import java.io.Serializable;

import jade.core.AID;

public class ProposalToArbiter implements Serializable {

    private AID firstPlayer;
    private AID secondPlayer;
    private int round;
    private int totalRounds;

    public ProposalToArbiter(AID firstPlayer, AID secondPlayer, int round, int totalRounds) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.round = round;
        this.totalRounds = totalRounds;
    }
    
    public AID getFirstPlayer() {
        return firstPlayer;
    }

    public AID getSecondPlayer() {
        return secondPlayer;
    }

    public int getRound() {
        return round;
    }

    public int getTotalRounds() {
        return totalRounds;
    }
}
