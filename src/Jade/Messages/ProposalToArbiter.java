package Jade.Messages;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class ProposalToArbiter extends ACLMessage {

    private AID firstPlayer;
    private AID secondPlayer;
    private int round;

    public ProposalToArbiter(int perf, AID firstPlayer, AID secondPlayer, int round) {
        super(perf);
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
