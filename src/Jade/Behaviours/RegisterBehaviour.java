package Jade.Behaviours;
import jade.core.behaviours.*;
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

	@Override
    public void action() {
        myAgent.doWait(200);
        //System.out.println("Preparing message...");
        DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAgent().getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(this.type);
		sd.setName(this.name);
		dfd.addServices(sd);
		try {
			DFService.register(getAgent(), dfd);
			System.out.println(this.type + " "+ getAgent().getAID().getName() + " registered in DF.");
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
        
    }
}
