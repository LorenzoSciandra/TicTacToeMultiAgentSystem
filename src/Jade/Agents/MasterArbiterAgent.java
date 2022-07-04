package Jade.Agents;
import jade.core.Agent;
import Jade.Behaviours.RegisterBehaviour;
import Jade.Behaviours.MasterArbiter.GetPlayersArbitersBehaviour;
import Jade.Behaviours.MasterArbiter.PlayGameBehaviour;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;


public class MasterArbiterAgent extends Agent{
    
    private AID[] arbiterAgents = null;
    private AID[] playerAgents = null;
    private AID winner = null;

    protected void setup() {
        // Register
        addBehaviour(new RegisterBehaviour("master-arbiter", "Master Arbiter"));
        // Wait that all agents are registered
        doWait(3000);
        // Add the behaviour to receive the messages to play and to arbiter
        addBehaviour(new GetPlayersArbitersBehaviour());
        // Start the Game
        addBehaviour(new PlayGameBehaviour(arbiterAgents, playerAgents));
    }

    protected void takeDown() {
        try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("Master Arbiter Agent "+getAID().getName()+" terminating.");
        System.out.println(" THE GAME IS OVER!. The final winner is "+ winner.getName());
	}

    public AID[] getPlayers() {
        return this.playerAgents;
    }
    public void setPlayers(AID[] players) {
        this.playerAgents = players;
    }

    public AID[] getArbiters() {
        return this.arbiterAgents;
    }
    public void setArbiters(AID[] players) {
        this.arbiterAgents = players;
    }
    public AID getWinner() {
        return this.winner;
    }
    public void setWinner(AID winner) {
        this.winner = winner;
    }

}
