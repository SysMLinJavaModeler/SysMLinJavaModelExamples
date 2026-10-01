package dbssystem.patient;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import dbssystem.common.DBSSignal;
import dbssystem.common.MotionSignal;
import dbssystem.common.PressureSignal;
import sysmlinjava.attributetypes.FrequencyHertz;
import sysmlinjava.attributetypes.IInteger;
import sysmlinjava.attributetypes.JerkMetersPerSecondCubed;
import sysmlinjava.attributetypes.PhaseShiftRadians;
import sysmlinjava.attributetypes.PotentialElectricalVolts;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.actions.Calculation;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.items.ItemIn;
import sysmlinjava.javaannotations.items.ItemOut;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;

/**
 * Patient is the SysMLinJava model of a patient using a deep-brain stimulation
 * system. It receives DBS signals from a DBS actuator emplanted in his brain to
 * reduce his tremor motion. The Patient also transmits tremor motion signals as
 * well as blood pressure signals to sensors that send information to a DBS
 * controller.
 * <p>
 * Note that this patient simulation model presents an "open loop" simulation,
 * i.e. the patient's response to the DBS is based solely on a time-based
 * calculation and is not a function of the received DBS signal. This simple
 * simulation is used to focus the simulation on demonstrating the concepts and
 * principles of modeling and simulation with SysMLinJava.
 * 
 * @author ModelerOne
 *
 */
public class Patient extends SysMLPart
{
	/**
	 * Port to receive the DBS signal from the DBS actuator
	 */
	@Port
	public DBSSignalInPort dbsSignalInPort;
	/**
	 * Port to transmit a tremor motion signal to a tremor sensorr
	 */
	@Port
	public MotionSignalOutPort motionOutPort;
	/**
	 * Port to transmit a blood pressure signal to a pulse sensor
	 */
	@Port
	public PressureSignalOutPort pressureOutPort;

	/**
	 * Item input for the current DBS signal
	 */
	@ItemIn
	public DBSSignal dbsSignalIn;
	/**
	 * Item output for the current tremor motion signal
	 */
	@ItemOut
	public MotionSignal motionOut;
	/**
	 * Item output for the current blood pressure signal
	 */
	@ItemOut
	public PressureSignal pressureOut;

	/**
	 * Time at which the patient simulation began
	 */
	@Attribute
	public Instant startInstant;

	/**
	 * Constructor
	 */
	public Patient()
	{
		super("Patient", 0L);
	}

	/**
	 * Event handler (reception) to receive the next value of the DBS signal
	 * 
	 * @param signal the DBS signal value
	 */
	@Action
	public void onDBSSignal(DBSSignal signal)
	{
		logger.info(signal.toString());
		dbsSignalIn.setValue(signal);
	}

	/**
	 * Event handler for the next time at which the simulation is to transmit
	 * patient tremor motion and blood pressure values to the sensors.
	 */
	@Action
	public void onMotionTime()
	{
		logger.info("motion time");
		calculateMotionPressureOut();
		motionOutPort.transmit(motionOut);
		pressureOutPort.transmit(pressureOut);
	}

	@Override
	public void start()
	{
		super.start();
		dbsSignalInPort.start();
	}

	@Override
	public void stop()
	{
		dbsSignalInPort.stop();
		super.stop();
	}

	/**
	 * Calculate the patient's new tremor motion and blood pressure
	 * values
	 */
	@Calculation
	private void calculateMotionPressureOut()
	{
		Duration timeSinceStart = Duration.between(startInstant, Instant.now());
		motionOut.jerk.value = 0.6 - (0.6 / 300.0) * timeSinceStart.toSeconds();
		pressureOut.rate.value = 40;
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new PatientStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		startInstant = Instant.now();
	}

	@Override
	protected void createItems()
	{
		dbsSignalIn = new DBSSignal(new FrequencyHertz(0), new PotentialElectricalVolts(0), new PhaseShiftRadians(0));
		motionOut = new MotionSignal(new JerkMetersPerSecondCubed(0));
		pressureOut = new PressureSignal(IInteger.of(60));
	}

	@Override
	protected void createPorts()
	{
		dbsSignalInPort = new DBSSignalInPort(this);
		pressureOutPort = new PressureSignalOutPort(this);
		motionOutPort = new MotionSignalOutPort(this);
	}
}
