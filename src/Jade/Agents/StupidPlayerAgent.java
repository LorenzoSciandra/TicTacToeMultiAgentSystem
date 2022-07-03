package Jade.Agents;

import Jade.*;
import Jade.Behaviours.*;
import Jade.Behaviours.Players.*;
import Jade.Messages.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jason.stdlib.queue.add;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.function.Consumer;

public class StupidPlayerAgent extends Agent {
    private Grid myGrid;
    private AID myArbiter;
    private AID myOpponent;
    private String mySymbol;
    private Boolean start;

    protected void setup() {
        myGrid = new Grid();
        addBehaviour(new RegisterBehaviour("stupid-player", "Stupid Player"));
        addBehaviour(new ReceiveOpponentBehaviour());
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("StupidPlayer Agent " + getAID().getName() + " terminating.");
    }

    public AID getPlayerAID() {
        return getAID();
    }

    public AID getArbiterAID() {
        return myArbiter;
    }

    public AID getOpponentAID() {
        return myOpponent;
    }

    public String getSymbol() {
        return mySymbol;
    }

    public Grid getGrid() {
        return myGrid;
    }

    public void setSymbol(String symbol) {
        mySymbol = symbol;
    }

    public void setOpponent(AID opponent) {
        myOpponent = opponent;
    }

    public void setArbiter(AID arbiter) {
        myArbiter = arbiter;
    }

    public void setGrid(Grid grid) {
        myGrid = grid;
    }

    public void setStart(Boolean start) {
        this.start = start;
    }

    public Boolean getStart() {
        return start;
    }
    
}


