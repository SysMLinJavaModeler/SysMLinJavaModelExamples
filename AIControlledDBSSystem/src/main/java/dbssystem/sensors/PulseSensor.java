package dbssystem.sensors;

import java.util.Optional;

import dbssystem.common.PressureSignal;
import dbssystem.common.PulseValue;
import sysmlinjava.attributetypes.BBoolean;
import sysmlinjava.attributetypes.IInteger;
import sysmlinjava.attributetypes.InternetAddress;
import sysmlinjava.javaannotations.actions.Calculation;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.items.ItemIn;
import sysmlinjava.javaannotations.items.ItemOut;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;

/**
 * PulseSensor is the SysMLinJava model of a sensor of a patient's pulse for use
 * in the control of a deep-brain stimulation system. It receives pulse signals
 * from a patient and transforms them into pulse values for transmission to the
 * DBS controller. The pulse values are only transmitted upon indication of the
 * presence of a tremor in the patient.
 * 
 * @author ModelerOne
 *
 */
public class PulseSensor extends SysMLPart
{
	/**
	 * Port for the input of pressure signals used to calculate pulse
	 */
	@Port
	public PressureSignalInPort pressureInPort;
	/**
	 * Port for the output of the calculated pulse value (heart rate)
	 */
	@Port
	public PulseValueOutPort pulseOutPort;
	/**
	 * Port for the input of indication that tremor is present in the patient
	 */
	@Port
	public TremorPresenceInPort tremorPresenceInPort;

	/**
	 * Item input for the current sensed pressure
	 */
	@ItemIn
	public PressureSignal pressureIn;
	/**
	 * Item output for the current pulse output
	 */
	@ItemOut
	public PulseValue pulseOut;
	/**
	 * Item input for the current tremor presence indication input
	 */
	@ItemIn
	public BBoolean tremorPresenceIn;

	/**
	 * Value of the IP address for the port receiving the pressure signal
	 */
	@Attribute
	public InternetAddress ipAddress;
	/**
	 * Value of the UDP port number for the port receiving the pressure signal
	 */
	@Attribute
	public IInteger pressureUDPPort;

	/**
	 * Constructor
	 */
	public PulseSensor()
	{
		super("PulseSensor", 0L);
	}

	/**
	 * Starts the sensor by starting the state machine and the pressure input port
	 */
	@Override
	public void start()
	{
		super.start();
		pressureInPort.start();
	}

	/**
	 * Stops the sensor
	 */
	@Override
	public void stop()
	{
		pressureInPort.stop();
		super.stop();
	}

	/**
	 * Event handler for the reciept of a new pressure signal value
	 * 
	 * @param value the new pressure signal value
	 */
	public void onPressureSignal(PressureSignal value)
	{
		logger.info(value.toString());
		if (tremorPresenceIn.isTrue())
		{
			calculatePulseOut();
			pulseOutPort.transmit(pulseOut);
		}
	}

	/**
	 * Event handler for the reciept of a new indication of the presence of tremor
	 * 
	 * @param isPresent the new tremor presence indication
	 */
	public void onTremorPresence(BBoolean isPresent)
	{
		logger.info(isPresent.toString());
		tremorPresenceIn.setValue(isPresent);
	}

	/**
	 * Calculates the pulse output value from the pressure input value.
	 */
	@Calculation
	private void calculatePulseOut()
	{
		pulseOut = pressureIn.toPulse();
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new PulseSensorStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		ipAddress = InternetAddress.ofLocalHost();
		pressureUDPPort = new IInteger(8706);
	}

	@Override
	protected void createItems()
	{
		pressureIn = new PressureSignal(IInteger.of(0));
		pulseOut = new PulseValue(IInteger.of(0));
		tremorPresenceIn = new BBoolean(false);
	}

	@Override
	protected void createPorts()
	{
		pressureInPort = new PressureSignalInPort(this, ipAddress.toInetAddress(), pressureUDPPort.toInteger());
		pulseOutPort = new PulseValueOutPort(this, 0L, "PulseValueOutPort");
		tremorPresenceInPort = new TremorPresenceInPort(this, 0L, "TremorPresenceInPort");
	}
}
