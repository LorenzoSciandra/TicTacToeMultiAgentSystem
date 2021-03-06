package Jade.Agents;

import Jade.*;
import Jade.Behaviours.*;
import Jade.Behaviours.Players.*;
import jade.domain.DFService;
import jade.domain.FIPAException;


public class StupidPlayerAgent extends Player{
 
    /**
     * Setups the agent for the game.
     * In the following order the agent:
     * - Initializes the grid
     * - Sets himself as Stupid (to differenciate this agent from the intelligent one)
     * - Regisers the agent in the DF
     * - Sets himself ready to receive his opponent and start the game
     * - Periodically checks if there's a winner or the game is over
     */
    protected void setup() {
        setGrid(new Grid());
        setStupid(true);
        addBehaviour(new RegisterBehaviour("player", "Stupid Player"));
        addBehaviour(new ReceiveOpponentBehaviour(true));
        addBehaviour(new CheckWinnerBehaviour());
        addBehaviour(new EndGameBehaviour());
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("StupidPlayer Agent " + getAID().getName() + " sta terminando.");
    }

}
