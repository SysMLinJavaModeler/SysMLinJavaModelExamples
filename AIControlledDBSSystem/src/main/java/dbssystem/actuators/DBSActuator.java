package dbssystem.actuators;

import java.util.Optional;

import dbssystem.common.DBSControl;
import dbssystem.common.DBSSignal;
import sysmlinjava.attributetypes.FrequencyHertz;
import sysmlinjava.attributetypes.IInteger;
import sysmlinjava.attributetypes.InternetAddress;
import sysmlinjava.attributetypes.PhaseShiftRadians;
import sysmlinjava.attributetypes.PotentialElectricalVolts;
import sysmlinjava.javaannotations.actions.Calculation;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.items.ItemIn;
import sysmlinjava.javaannotations.items.ItemOut;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;

/**
 * DBSActuator is the SysMLinJava model of an actuator for a deep-brain
 * stimulation system. It receives control signals and transforms them into low
 * power electrical signals to be injected into the patient's brain to
 * reduce/control the patient's tremor.
 * 
 * @author ModelerOne
 *
 */
public class DBSActuator extends SysMLPart
{
	/**
	 * Port for ouput of the DBS signal to the patient
	 */
	@Port
	public DBSSignalOutPort dbsSignalOutPort;
	/**
	 * Port for input of the control signals to the acuator
	 */
	@Port
	public DBSControlInPort controlInPort;

	/**
	 * Item of the control that was last input
	 */
	@ItemIn
	public DBSControl controlIn;
	/**
	 * Value of the standard amplitude to be used for the DBS signal to the patient
	 */
	@Attribute
	private PotentialElectricalVolts standardAmplitude;
	/**
	 * Value of the UDP port used to receive the controls
	 */
	@Attribute
	private IInteger controlUDPPort;
	/**
	 * Value of the IP address used to receive the controls
	 */
	@Attribute
	private InternetAddress controlIPAddress;

	/**
	 * Item of the DBS signal currently being injected out into the patient's
	 * brain.
	 */
	@ItemOut
	public DBSSignal signalOut;

	/**
	 * Constructor
	 */
	public DBSActuator()
	{
		super("DBSActuator", 0L);
	}

	/**
	 * Event handler for receipt of new DBS control object. The operation stores the
	 * control information, invokes the constraint to calculate the new DBS signal
	 * value, and transmits the new signal to the patient's brain.
	 * 
	 * @param control control data received
	 */
	public void onDBSControl(DBSControl control)
	{
		logger.info(control.toString());
		controlIn.frequency.value = control.frequency.value;
		controlIn.phaseShift.value = control.phaseShift.value;
		calculateSignalOut();
		dbsSignalOutPort.transmit(signalOut);
	}

	/**
	 * Starts the actuator, ie. starts the state machine and the control port
	 */
	@Override
	public void start()
	{
		super.start();
		controlInPort.start();
	}

	/**
	 * Stops the actuator
	 */
	@Override
	public void stop()
	{
		controlInPort.stop();
		super.stop();
	}

	/**
	 * Calculates the actuator outputs to the values provided in the actuator's
	 * control.
	 */
	@Calculation
	private void calculateSignalOut()
	{
		signalOut.frequency.value = controlIn.frequency.value;
		signalOut.phaseShift.value = controlIn.phaseShift.value;
		signalOut.amplitude.value = standardAmplitude.value;
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new DBSActuatorStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		standardAmplitude = new PotentialElectricalVolts(20.0e-6);
		controlIPAddress = InternetAddress.ofLocalHost();
		controlUDPPort = new IInteger(8703);
	}

	@Override
	protected void createItems()
	{
		controlIn = new DBSControl(new FrequencyHertz(0), new PhaseShiftRadians(0));
		signalOut = new DBSSignal(new FrequencyHertz(0), new PotentialElectricalVolts(0), new PhaseShiftRadians(0));
	}

	@Override
	protected void createPorts()
	{
		dbsSignalOutPort = new DBSSignalOutPort(this);
		controlInPort = new DBSControlInPort(this, controlIPAddress.toInetAddress(), controlUDPPort.toInteger());
	}
}
