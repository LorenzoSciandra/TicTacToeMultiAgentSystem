package Jade.Agents;
import jade.core.Agent;
import Jade.Behaviours.EndGameBehaviour;
import Jade.Behaviours.RegisterBehaviour;
import Jade.Behaviours.Arbiter.WaitProposalArbiterBehaviour;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;

public class ArbiterAgent extends Agent{
    
    private AID masterArbiter = null;
    private AID firstPlayer = null;
    private AID secondPlayer = null;
    private AID winner = null;
    private int round = 0;
    private int turno = 0;
    private String firstSymbol = "X";
    private String secondSymbol = "O";

    protected void setup() {
        // Register
        addBehaviour(new RegisterBehaviour("arbiter", "Arbiter"));
        // Add the behaviour to receive the messages to play and to arbiter
        addBehaviour(new WaitProposalArbiterBehaviour());
        addBehaviour(new EndGameBehaviour());
    }

    protected void takeDown() {
        try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("Arbiter Agent "+getAID().getName()+" sta terminando.");
	}

    public AID getFirstPlayer() {
        return this.firstPlayer;
    }
    public void setFirstPlayer(AID firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public AID getSecondPlayer() {
        return this.secondPlayer;
    }
    public void setSecondPlayer(AID secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public void setNumRound(int numRound) {
        this.round = numRound;
    }

    public void nextTurn() {
        this.turno++;
    }

    public int getTurno() {
        return this.turno;
    }

    public int getRound() {
        return this.round;
    }

    public AID getWinner() {
        return this.winner;
    }

    public void setWinner(AID winner) {
        this.winner = winner;
    }

    public AID getMasterArbiter() {
        return this.masterArbiter;
    }

    public void setMasterArbiter(AID masterArbiter) {
        this.masterArbiter = masterArbiter;
    }

    public String getFirstSymbol() {
        return this.firstSymbol;
    }

    public void setFirstSymbol(String firstSymbol) {
        this.firstSymbol = firstSymbol;
    }

    public String getSecondSymbol() {
        return this.secondSymbol;
    }

    public void setSecondSymbol(String secondSymbol) {
        this.secondSymbol = secondSymbol;
    }
    
}
