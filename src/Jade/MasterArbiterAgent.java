package Jade;
import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jason.stdlib.map.get;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class MasterArbiterAgent extends Agent{
    
    private AID[] arbiterAgents = null;
    private AID[] playerAgents = null;
    private AID winner = null;

    protected void setup() {
        // Register
        addBehaviour(new RegisterBehaviour("master-arbiter", "Master Arbiter"));
        // Wait that all agents are registered
        doWait(5000);
        // Add the behaviour to receive the messages to play and to arbiter
        addBehaviour(new GetPlayersArbiters());
        // Start the Game
        addBehaviour(new PlayGame(arbiterAgents, playerAgents));
    }

    protected void takeDown() {
        try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("Master Arbiter Agent "+getAID().getName()+" terminating. THE GAME IS OVER");
        System.out.println("The winner is "+ winner.getName());
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
