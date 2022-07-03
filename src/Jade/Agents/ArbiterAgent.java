package Jade.Agents;
import jade.core.Agent;
import Jade.Behaviours.RegisterBehaviour;
import Jade.Behaviours.Arbiter.WaitProposalArbiterBehaviour;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jason.stdlib.map.get;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ArbiterAgent extends Agent{
    
    private AID firstPlayer = null;
    private AID secondPlayer = null;
    private AID winner = null;
    private int round = 0;
    private int turno = 0;

    protected void setup() {
        // Register
        addBehaviour(new RegisterBehaviour("arbiter", "Arbiter"));
        // Add the behaviour to receive the messages to play and to arbiter
        addBehaviour(new WaitProposalArbiterBehaviour());
    }

    protected void takeDown() {
        try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("Arbiter Agent "+getAID().getName()+" terminating.");
        System.out.println("The winner of my game is "+ winner.getName());
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

    public void setNumRounds(int numRounds) {
        this.round = numRounds;
    }
}
