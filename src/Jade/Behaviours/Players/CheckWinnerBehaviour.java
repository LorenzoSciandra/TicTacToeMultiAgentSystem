package Jade.Behaviours.Players;

import Jade.Grid;
import Jade.Agents.Player;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class CheckWinnerBehaviour extends CyclicBehaviour {

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM_IF);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            System.out.println(getAgent().getName() + " " + msg.getContent());
            switch (msg.getContent()) {
                case "WIN":
                    System.out.println("Agent " + getAgent().getAID().getName() + " ha vinto.");
                    if (((Player) getAgent()).getRound() < ((Player) getAgent()).getTotalRounds() - 1) {
                        ((Player) getAgent()).setGrid(new Grid());
                        System.out.println("HO VINTO E ASPETTO IL PROSSIMO ROUND " + getAgent().getName());
                        getAgent().addBehaviour(new ReceiveOpponentBehaviour(((Player) getAgent()).getStupid()));
                    }
                    break;
                case "LOSE":
                    System.out.println("Agent " + getAgent().getAID().getName() + " ha perso.");
                    break;
                case "TIE": // TIE
                    System.out.println("PAREGGIO");
                    ((Player) getAgent()).setGrid(new Grid());
                    if (((Player) getAgent()).getStart()) {
                        getAgent().doWait(1000);
                        //((Player) getAgent()).setStart(false);
                        //((Player) getAgent()).setSymbol("O");
                        if (((Player) getAgent()).getStupid()) {
                            // aspettiamo che anche l'altro player abbia resettato il tutto
                            getAgent().addBehaviour(new PlayBehaviour());
                        } else {
                            getAgent().addBehaviour(new IntelligentPlayBehaviour());
                        }
                    } else {
                        //((Player) getAgent()).setStart(true);
                        //((Player) getAgent()).setSymbol("X");
                        getAgent().addBehaviour(new ReceiveMessageBehaviour());
                    }
                    break;
                default:
                    break;

            }
        } else {
            block();
        }
    }

}
