package Jade.Behaviours.MasterArbiter;
import Jade.Agents.MasterArbiterAgent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class GetPlayersArbitersBehaviour extends OneShotBehaviour {

	private AID[] arbiterAgents;
    private AID[] playerAgents;

	@Override
    public void action() {
		DFAgentDescription templateArbiter = new DFAgentDescription();
		ServiceDescription sdArbiter = new ServiceDescription();
		sdArbiter.setType("arbiter");
		templateArbiter.addServices(sdArbiter);
		try {
		    DFAgentDescription[] result = DFService.search(getAgent(), templateArbiter); 
			System.out.println("Found the following arbiters agents:");
			arbiterAgents = new AID[result.length];
			for (int i = 0; i < result.length; ++i) {
				arbiterAgents[i] = result[i].getName();
				System.out.println(arbiterAgents[i].getName());
			}

			DFAgentDescription templatePlayer = new DFAgentDescription();
			ServiceDescription sdPlayer = new ServiceDescription();
			sdPlayer.setType("player");
			templatePlayer.addServices(sdPlayer);

			try{
				DFAgentDescription[] result2 = DFService.search(getAgent(), templatePlayer); 
				System.out.println("Found the following players agents:");
				playerAgents = new AID[result2.length];
				for (int i = 0; i < result2.length; ++i) {
					playerAgents[i] = result2[i].getName();
					System.out.println(playerAgents[i].getName());
				}

				((MasterArbiterAgent) getAgent()).setPlayers(playerAgents);
				((MasterArbiterAgent) getAgent()).setArbiters(arbiterAgents);
			}
			catch (FIPAException fe) {
				fe.printStackTrace();
			}
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
    }
    
}


