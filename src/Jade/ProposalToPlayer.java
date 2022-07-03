package Jade;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class ProposalToPlayer extends ACLMessage {
    private boolean firstToPlay;
    private char symbol;
    private AID opponent;

    public ProposalToPlayer(int performative, AID opponent, boolean firstToPlay, char symbol) {
        super(performative);
        this.firstToPlay = firstToPlay;
        this.symbol = symbol;
        this.opponent = opponent;
    }

    public boolean isFirstToPlay() {
        return firstToPlay;
    }

    public char getSymbol() {
        return symbol;
    }

    public AID getOpponent() {
        return opponent;
    }
}
