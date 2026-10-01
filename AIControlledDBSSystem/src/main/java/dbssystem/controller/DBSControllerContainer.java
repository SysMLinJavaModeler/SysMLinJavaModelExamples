package dbssystem.controller;

import java.util.Optional;

import dbssystem.actuators.DBSActuator;
import sysmlinjava.connectors.SysMLFlowConnector;
import sysmlinjava.connectors.SysMLFlowConnector.TypesEnum;
import sysmlinjava.javaannotations.connectors.FlowConnector;
import sysmlinjava.javaannotations.parts.ExternalPartReplica;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.parts.PartContainer;
import sysmlinjava.views.interactionssequencediagram.InteractionMessageSequenceDisplay;
import sysmlinjava.views.interactionssequencediagram.InteractionMessageTransmitter;
import sysmlinjava.views.interactionssequencediagram.InteractionMessageTransmitters;

/**
 * {@code DBSControllerContainer} is the SysMLinJava model for a container of
 * block instances (parts) to execute in their own operating system process. The
 * container hosts the DBS controller, i.e. an AI-based controller for
 * translation of tremor motion sensor and blood pressure sensor inputs into DBS
 * actuator control outputs. In accordance with the {@code PartContainer}
 * construct, it also contains a "replica" of the DBS actuator to which it
 * transmits the control data. The replica of the DBS controller is used to
 * create the connectors from the controller to the actual DBS actuator, whose
 * instance is located in another {@code PartContainer}, i.e. in another
 * operating system process.
 * 
 * @author ModelerOne
 */
public class DBSControllerContainer extends PartContainer
{
	/**
	 * Part for the DBS controller
	 */
	@Part
	public DBSController controller;

	/**
	 * Replica part for the DBS actuator controlled by the DBSController and located
	 * in another process
	 */
	@ExternalPartReplica
	public DBSActuator dbsActuator;

	/**
	 * Connector between the DBS controller and the DBS actuator that is actually in
	 * another container in another operating system process
	 */
	@FlowConnector
	public SysMLFlowConnector controllerToActuatorConnector;

	/**
	 * Constructor
	 */
	public DBSControllerContainer()
	{
		super("DBSControllerContainer", 0L);
	}

	@Override
	protected void createParts()
	{
		controller = new DBSController();
		dbsActuator = new DBSActuator();
	}

	@Override
	protected void createFlowConnectors()
	{
		controllerToActuatorConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, controller.controlOutPort, dbsActuator.controlInPort, "", 0L);
	}

	@Override
	public void enableInteractionMessageTransmissions()
	{
		InteractionMessageTransmitter transmitter = new InteractionMessageTransmitter(InteractionMessageSequenceDisplay.udpPort, true);
		InteractionMessageTransmitters interactionMessageTransmitters = new InteractionMessageTransmitters(transmitter, true);

		controller.controlOutPort.messageUtility = Optional.of(interactionMessageTransmitters);
	}

	/**
	 * Main for the execution of the container in its own process
	 * 
	 * @param args null
	 */
	public static void main(String[] args)
	{
		DBSControllerContainer container = new DBSControllerContainer();
		container.controller.start();
		try
		{
			Thread.sleep(300_000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		container.controller.stop();
		System.exit(0);
	}
}
