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
    private int totalRounds;

    /**
     * This method is called when the arbiter is created.
     * The arbiter then schedules three behaviours:
     * - RegisterBehaviour: to register itself in the DF
     * - WaitProposalArbiterBehaviour: to receive the couple of players from the master to arbitrate
     * - EndGameBehaviour: to end the game
     */
    @Override
    protected void setup() {
        // Register
        addBehaviour(new RegisterBehaviour("arbiter", "Arbiter"));
        addBehaviour(new WaitProposalArbiterBehaviour());
        addBehaviour(new EndGameBehaviour());
    }

    /**
     * This method is called when the arbiter is terminated.
     * The arbiter then unregisters itself from the DF
     */
    protected void takeDown() {
        try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("Arbiter Agent "+getAID().getName()+" sta terminando.");
	}

    
    /** 
     * Returns the AID of the first player of the couple.
     * @return AID
     */
    public AID getFirstPlayer() {
        return this.firstPlayer;
    }
    
    /** 
     * Sets the AID of the first player of the couple.
     * @param firstPlayer
     */
    public void setFirstPlayer(AID firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    
    /** 
     * Returns the AID of the second player of the couple.
     * @return AID
     */
    public AID getSecondPlayer() {
        return this.secondPlayer;
    }
    
    /** 
     * Sets the AID of the second player of the couple.
     * @param secondPlayer
     */
    public void setSecondPlayer(AID secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    
    /** 
     * Sets the number of rounds of the game.
     * @param numRound
     */
    public void setNumRound(int numRound) {
        this.round = numRound;
    }

    /**
     * Advances the turn.
     */
    public void nextTurn() {
        this.turno++;
    }

    
    /** 
     * Returns the turn number.
     * A turn is the n-th move of the game.
     * 
     * @return int
     */
    public int getTurno() {
        return this.turno;
    }

    
    /** 
     * Returns the round number.
     * A round is a match between two players.
     * @return int
     */
    public int getRound() {
        return this.round;
    }

    
    /** 
     * Returns the AID of the winner
     * @return AID
     */
    public AID getWinner() {
        return this.winner;
    }

    
    /** 
     * Sets the AID of the winner
     * @param winner
     */
    public void setWinner(AID winner) {
        this.winner = winner;
    }

    
    /** 
     * Returns the AID of the assigned Master Arbiter.
     * @return AID
     */
    public AID getMasterArbiter() {
        return this.masterArbiter;
    }

    
    /** 
     * Sets the AID of the assigned Master Arbiter.
     * @param masterArbiter
     */
    public void setMasterArbiter(AID masterArbiter) {
        this.masterArbiter = masterArbiter;
    }

    
    /** 
     * Returns the symbol of the first player.
     * @return String
     */
    public String getFirstSymbol() {
        return this.firstSymbol;
    }

    
    /** 
     * Sets the symbol of the first player.
     * @param firstSymbol
     */
    public void setFirstSymbol(String firstSymbol) {
        this.firstSymbol = firstSymbol;
    }

    
    /** 
     * Returns the symbol of the second player.
     * @return String
     */
    public String getSecondSymbol() {
        return this.secondSymbol;
    }

    
    /** 
     * Sets the symbol of the second player.
     * @param secondSymbol
     */
    public void setSecondSymbol(String secondSymbol) {
        this.secondSymbol = secondSymbol;
    }

    
    /** 
     * Returns the total number of rounds of the game.
     * @return int
     */
    public int getTotalRounds() {
        return this.totalRounds;
    }

    
    /** 
     * Sets the total number of rounds of the game.
     * @param totalRounds
     */
    public void setTotalRounds(int totalRounds) {
        this.totalRounds = totalRounds;
    }
    
}
