package Jade.Agents;

import Jade.*;
import Jade.Behaviours.*;
import Jade.Behaviours.Players.*;
import jade.domain.DFService;
import jade.domain.FIPAException;


public class IntelligentPlayerAgent extends Player {

    /**
     * Setups the agent for the game.
     * In the following order the agent:
     * - Initializes the grid
     * - Sets himself as NOT-Stupid (to differenciate this agent from the stupid one)
     * - Regisers the agent in the DF
     * - Sets himself ready to receive his opponent and start the game
     * - Periodically checks if there's a winner or the game is over
     */
    protected void setup() {
        setGrid(new Grid());
        setStupid(false);
        addBehaviour(new RegisterBehaviour("player", "Intelligent Player"));
        addBehaviour(new ReceiveOpponentBehaviour(false));
        addBehaviour(new CheckWinnerBehaviour());
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
