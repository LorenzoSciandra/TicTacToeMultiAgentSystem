package Jade.Messages;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class InformWin extends ACLMessage {

    private AID winner;

    public InformWin(int perf, AID winner) {
        super(perf);
        this.winner = winner;
    }
    
    public AID getWinner() {
        return winner;
    }
}