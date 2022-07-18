package Jade.Agents;

import jade.core.Agent;
import Jade.Behaviours.RegisterBehaviour;
import Jade.Behaviours.MasterArbiter.GetPlayersArbitersBehaviour;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;

public class MasterArbiterAgent extends Agent {

    private AID[] arbiterAgents;
    private AID[] playerAgents;
    private AID winner;

    /**
     * Setups the agent for the game.
     * In the following order the agent:
     * - Register himself in the DF
     * - Waits for all the players and arbiters to be ready
     * - Adds a behaviour to assign all the arbiters to the players and wait for the games to finish
     */
    protected void setup() {
        addBehaviour(new RegisterBehaviour("master-arbiter", "Master Arbiter"));
        doWait(500);
        addBehaviour(new GetPlayersArbitersBehaviour());
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("Master Arbiter Agent " + getAID().getName() + " sta terminando.");
        if (winner != null) {
            doWait(100);
            System.out.println("GAME OVER. VINCITORE: " + winner.getName());
        }
    }

    
    /** 
     * Returns the array of the player agents
     * @return AID[]
     */
    public AID[] getPlayers() {
        return this.playerAgents;
    }

    
    /** 
     * Sets the array of the player agents
     * @param players
     */
    public void setPlayers(AID[] players) {
        this.playerAgents = players;
    }

    
    /** 
     * Returns the array of the arbiter agents
     * @return AID[]
     */
    public AID[] getArbiters() {
        return this.arbiterAgents;
    }

    
    /** 
     * Sets the array of the arbiter agents
     * @param players
     */
    public void setArbiters(AID[] players) {
        this.arbiterAgents = players;
    }

    
    /** 
     * Returns the winner agent AID
     * @return AID
     */
    public AID getWinner() {
        return this.winner;
    }

    
    /** 
     * Sets the winner agent AID
     * @param winner
     */
    public void setWinner(AID winner) {
        this.winner = winner;
    }

}
