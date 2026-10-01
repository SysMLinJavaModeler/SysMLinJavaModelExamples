package dbssystem.patient;

import java.util.Optional;

import dbssystem.actuators.DBSActuator;
import dbssystem.sensors.PulseSensor;
import dbssystem.sensors.TremorSensor;
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
 * {@code PatientContainer} is the SysMLinJava model for a container of block
 * instances (parts) to execute in their own operating system process. The
 * container hosts the patient and his DBS actuator. In accordance with the
 * {@code PartContainer} construct, it also contains a "replica" of the two
 * sensors that are attached to the patient, i.e. the tremor motion sensor and
 * the blood pressure sensor. The replicas of the two sensors are used to create
 * the connectors from the patient to the actual sensors, whose instances are
 * located in another {@code PartContainer}, i.e. in another operating system
 * process (JVM).
 * 
 * @author ModelerOne
 */
public class PatientContainer extends PartContainer
{
	/**
	 * Part for the patient whose tremor is to be reduced/controlled by the DBS
	 * system
	 */
	@Part
	public Patient patient;
	/**
	 * Part for the DBS actuator embedded in the patient's brain.
	 */
	@Part
	public DBSActuator dbsActuator;

	/**
	 * Replica part for the pulse sensor whose actual instance is part of another
	 * container, i.e. in another process.
	 */
	@ExternalPartReplica
	public PulseSensor pulseSensor;
	/**
	 * Replica part for the tremor sensor whose actual instance is part of another
	 * container, i.e. in another process.
	 */
	@ExternalPartReplica
	public TremorSensor tremorSensor;

	/**
	 * Connector between the DBS actuator and the patient
	 */
	@FlowConnector
	public SysMLFlowConnector dbsToPatientConnector;

	/**
	 * Connector between the patient and the tremor sensor that is part of another
	 * container, i.e. external to this container.
	 */
	@FlowConnector
	public SysMLFlowConnector patientToTremorSensorConnector;
	/**
	 * Connector between the patient and the pulse sensor that is part of another
	 * container, i.e. external to this container.
	 */
	@FlowConnector
	public SysMLFlowConnector patientToPulseSensorConnector;

	/**
	 * Constructor
	 */
	public PatientContainer()
	{
		super("PatientContainer", 0L);
	}

	@Override
	protected void createParts()
	{
		patient = new Patient();
		dbsActuator = new DBSActuator();
		pulseSensor = new PulseSensor();
		tremorSensor = new TremorSensor();
	}

	@Override
	protected void createFlowConnectors()
	{
		dbsToPatientConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, dbsActuator.dbsSignalOutPort, patient.dbsSignalInPort, "", 0L);
		patientToTremorSensorConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, patient.motionOutPort, tremorSensor.motionInPort, "", 0L);
		patientToPulseSensorConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, patient.pressureOutPort, pulseSensor.pressureInPort, "", 0L);
	}

	@Override
	public void enableInteractionMessageTransmissions()
	{
		InteractionMessageTransmitter transmitter = new InteractionMessageTransmitter(InteractionMessageSequenceDisplay.udpPort, true);
		InteractionMessageTransmitters interactionMessageTransmitters = new InteractionMessageTransmitters(transmitter, true);

		patient.motionOutPort.messageUtility = Optional.of(interactionMessageTransmitters);
		patient.pressureOutPort.messageUtility = Optional.of(interactionMessageTransmitters);
		dbsActuator.dbsSignalOutPort.messageUtility = Optional.of(interactionMessageTransmitters);
	}

	/**
	 * Main for the execution of the container in its own process
	 * 
	 * @param args null
	 */
	public static void main(String[] args)
	{
		PatientContainer container = new PatientContainer();
		container.patient.start();
		container.dbsActuator.start();
		try
		{
			Thread.sleep(300_000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		container.patient.stop();
		container.dbsActuator.stop();
		System.exit(0);
	}
}
