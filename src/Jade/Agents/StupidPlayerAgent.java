package Jade.Agents;

import Jade.*;
import Jade.Behaviours.*;
import Jade.Behaviours.Players.*;
import jade.domain.DFService;
import jade.domain.FIPAException;


public class StupidPlayerAgent extends Player{
 
    protected void setup() {
        setGrid(new Grid());
        setStupid(true);
        addBehaviour(new RegisterBehaviour("stupid-player", "Stupid Player"));
        addBehaviour(new ReceiveOpponentBehaviour(true));
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("StupidPlayer Agent " + getAID().getName() + " terminating.");
    }

}
