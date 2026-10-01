
package dbssystem.sensors;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import dbssystem.controller.DBSController;
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
 * {@code DBSSensorsContainer} is the SysMLinJava model for a container of block
 * instances (parts) to execute in their own operating system process. The
 * container hosts the two patient sensors, i.e. a tremor motion sensor and a
 * blood pressure sensor. In accordance with the {@code PartContainer} construct, it
 * also contains a "replica" of the DBS controller to which it transmits the
 * sensor data. The replica of the DBS controller is used to create the
 * connectors from the sensors to the actual DBS controller, whose instance is
 * located in another {@code PartContainer}, i.e. in another operating system
 * process.
 * 
 * @author ModelerOne
 *
 */
public class DBSSensorsContainer extends PartContainer
{
	/**
	 * Part for the tremor motion sensor
	 */
	@Part
	public TremorSensor tremorSensor;
	/**
	 * Part for the pulse sensor
	 */
	@Part
	public PulseSensor pulseSensor;

	/**
	 * Replica part for DBS controller
	 */
	@ExternalPartReplica
	public DBSController dbsController;

	/**
	 * Connector between the tremor sensor and pulse sesnor
	 */
	@FlowConnector
	public SysMLFlowConnector tremorSensorToPulseSensorConnector;

	/**
	 * Connector between the pulse sensor and the controller
	 */
	@FlowConnector
	public SysMLFlowConnector pulseSensorToControllerConnector;
	/**
	 * Connector between the tremor sensor and the controller
	 */
	@FlowConnector
	public SysMLFlowConnector tremorSensorToControllerConnector;

	/**
	 * Constructor
	 */
	public DBSSensorsContainer()
	{
		super("DBSensorsContainer", 0L);
	}

	@Override
	protected void createParts()
	{
		tremorSensor = new TremorSensor();
		pulseSensor = new PulseSensor();
		dbsController = new DBSController();
	}

	@Override
	protected void createFlowConnectors()
	{
		tremorSensorToPulseSensorConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, tremorSensor.tremorPresenceOutPort, pulseSensor.tremorPresenceInPort, "", 0L);
		tremorSensorToControllerConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, tremorSensor.tremorLevelOutPort, dbsController.tremorInPort, "", 0L);
		pulseSensorToControllerConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, pulseSensor.pulseOutPort, dbsController.pulseInPort, "", 0L);
	}

	@Override
	public void enableInteractionMessageTransmissions()
	{
		InteractionMessageTransmitter transmitter = new InteractionMessageTransmitter(InteractionMessageSequenceDisplay.udpPort, true);
		InteractionMessageTransmitters interactionMessageTransmitters = new InteractionMessageTransmitters(transmitter, true);

		tremorSensor.tremorLevelOutPort.messageUtility = Optional.of(interactionMessageTransmitters);
		pulseSensor.pulseOutPort.messageUtility = Optional.of(interactionMessageTransmitters);
		tremorSensor.tremorPresenceOutPort.messageUtility = Optional.of(interactionMessageTransmitters);
		
	}

	/**
	 * Main for the execution of the container in its own process
	 * 
	 * @param args null arguments
	 */
	public static void main(String[] args)
	{
		DBSSensorsContainer container = new DBSSensorsContainer();
		container.tremorSensor.start();
		container.pulseSensor.start();

		try
		{
			Thread.sleep(300_000);
			container.tremorSensor.concurrentExecutionThreads.awaitTermination(30L, TimeUnit.NANOSECONDS);
			container.pulseSensor.concurrentExecutionThreads.awaitTermination(30L, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		container.tremorSensor.stop();
		container.pulseSensor.stop();
		System.exit(0);
	}
}
