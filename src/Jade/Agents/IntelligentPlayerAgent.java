package Jade.Agents;

import Jade.*;
import Jade.Behaviours.*;
import Jade.Behaviours.Players.*;
import jade.domain.DFService;
import jade.domain.FIPAException;


public class IntelligentPlayerAgent extends Player {

    protected void setup() {
        setGrid(new Grid());
        setStupid(false);
        addBehaviour(new RegisterBehaviour("player", "Intelligent Player"));
        addBehaviour(new ReceiveOpponentBehaviour(false));
        addBehaviour(new EndGameBehaviour());
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("IntelligentPlayer Agent " + getAID().getName() + " sta terminando.");
    }

}
