package Jade.Messages;

import java.io.Serializable;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class ProposalToArbiter implements Serializable {

    private AID firstPlayer;
    private AID secondPlayer;
    private int round;

    public ProposalToArbiter(AID firstPlayer, AID secondPlayer, int round) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.round = round;
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
}
