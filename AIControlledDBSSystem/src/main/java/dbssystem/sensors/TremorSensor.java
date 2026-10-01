package dbssystem.sensors;

import java.util.Optional;

import dbssystem.common.MotionSignal;
import dbssystem.common.TremorLevel;
import sysmlinjava.attributetypes.BBoolean;
import sysmlinjava.attributetypes.DistanceMillimeters;
import sysmlinjava.attributetypes.FrequencyHertz;
import sysmlinjava.attributetypes.IInteger;
import sysmlinjava.attributetypes.InternetAddress;
import sysmlinjava.attributetypes.JerkMetersPerSecondCubed;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.actions.Calculation;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.items.ItemIn;
import sysmlinjava.javaannotations.items.ItemOut;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;

/**
 * TremorSensor is the SysMLinJava model of a sensor of a patient's tremor for
 * use in the control of a deep-brain stimulation system. It receives tremor
 * signals from a patient and transforms them into tremor values for
 * transmission to the DBS controller. It also transmits a signal to the pulse
 * sensor if/when tremor is present in the patient.
 * 
 * @author ModelerOne
 */
public class TremorSensor extends SysMLPart
{
	/**
	 * Port for the output of tremor level values
	 */
	@Port
	public TremorLevelOutPort tremorLevelOutPort;
	/**
	 * Port for the output of the presence of tremor
	 */
	@Port
	public TremorPresenceOutPort tremorPresenceOutPort;
	/**
	 * Port for the input of motion signal from the patient
	 */
	@Port
	public MotionSignalInPort motionInPort;

	/**
	 * Value for the max amplitude of motion recognized for calculating tremor jerk
	 */
	@Attribute
	public JerkMetersPerSecondCubed maxAmplitudeJerk;
	/**
	 * Value for the maximum jerk recognized from the patient
	 */
	@Attribute
	public JerkMetersPerSecondCubed maxValidJerk;
	/**
	 * Value of the IP address used to receive the patient's motion signal
	 */
	@Attribute
	public InternetAddress ipAddress;
	/**
	 * Value of the UDP port number used to receive the patient's motion signal
	 */
	@Attribute
	public IInteger motionUDPPort;

	/**
	 * Flow value output for indication that tremor is present in the patient
	 */
	@Attribute
	public BBoolean tremorPresenceOut;
	/**
	 * Item input for the jerk in the patient's motion
	 */
	@ItemIn
	public JerkMetersPerSecondCubed jerkIn;
	/**
	 * Item output for the tremor frequency
	 */
	@ItemOut
	public FrequencyHertz tremorFrequencyOut;
	/**
	 * Item output for the tremor amplitude
	 */
	@ItemOut
	public DistanceMillimeters tremorAmplitudeOut;

	/**
	 * Event handler for the receipt of a motion signal from the
	 * patient
	 * 
	 * @param motion motion signal from patient
	 */
	@Action
	public void onMotion(MotionSignal motion)
	{
		logger.info(motion.toString());
		jerkIn.setValue(motion.jerk);
		calculateTremorOut();
		tremorLevelOutPort.transmit(new TremorLevel(tremorFrequencyOut, tremorAmplitudeOut));
		if (jerkIn.value >= 0 && jerkIn.lessThanOrEqualTo(maxValidJerk))
			tremorPresenceOutPort.transmit(BBoolean.True);
		else
			tremorPresenceOutPort.transmit(BBoolean.False);
	}

	/**
	 * Calculates tremor level from patient motion. This calculation is an
	 * artificially simple algorithm used to enable demonstration of this
	 * SysMLinJava model.
	 */
	@Calculation
	private void calculateTremorOut()
	{
		if (jerkIn.value >= 0 && jerkIn.lessThanOrEqualTo(maxValidJerk))
		{
			tremorFrequencyOut.setValue(jerkIn.value * 10 * 2);
			if (jerkIn.lessThanOrEqualTo(maxAmplitudeJerk))
				tremorAmplitudeOut.setValue(jerkIn.value * 10 * 5);
			else
				tremorAmplitudeOut.setValue(15 - (jerkIn.value - maxAmplitudeJerk.value) * 10 * 5);
		}
		else
			logger.warning("invalid jerk value received: " + jerkIn.toString() + "; ignored");
	}

	/**
	 * Starts the sensor, i.e. starts the state machine and the port to receive
	 * motion inputs from the patient
	 */
	@Override
	public void start()
	{
		super.start();
		motionInPort.start();
	}

	/**
	 * Stops the sensor
	 */
	@Override
	public void stop()
	{
		motionInPort.stop();
		super.stop();
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new TremorSensorStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		maxValidJerk = new JerkMetersPerSecondCubed(0.7);
		maxAmplitudeJerk = new JerkMetersPerSecondCubed(0.3);
		ipAddress = InternetAddress.ofLocalHost();
		motionUDPPort = new IInteger(8705);
	}

	@Override
	protected void createItems()
	{
		jerkIn = new JerkMetersPerSecondCubed(0);
		tremorPresenceOut = new BBoolean(false);
		tremorFrequencyOut = new FrequencyHertz(0);
		tremorAmplitudeOut = new DistanceMillimeters(0);
	}

	@Override
	protected void createPorts()
	{
		tremorLevelOutPort = new TremorLevelOutPort(this, 0L, "TremorLevelOutPort");
		motionInPort = new MotionSignalInPort(this, ipAddress.toInetAddress(), motionUDPPort.toInteger());
		tremorPresenceOutPort = new TremorPresenceOutPort(this, 0L, "TremorPresencePort");
	}
}
