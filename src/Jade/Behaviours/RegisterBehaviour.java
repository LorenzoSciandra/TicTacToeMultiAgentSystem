package Jade.Behaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class RegisterBehaviour extends OneShotBehaviour {

	private String type;
	private String name;

	public RegisterBehaviour(String type, String name) {
		this.type = type;
		this.name = name;
	}

	/**
	 * This behaviour is essential for the player to register in the DF (Directory
	 * Facilitator - Pagine Gialle)
	 * and be "searchable" by the Master Arbiter to organize the tournament.
	 * We create an Agent Description and a Service Description to register the
	 * single agents in the DF.
	 */
	@Override
	public void action() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAgent().getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(this.type);
		sd.setName(this.name);
		dfd.addServices(sd);
		try {
			DFService.register(getAgent(), dfd);
			System.out.println(this.type + " " + getAgent().getAID().getName() + " registered in DF.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

	}
}
