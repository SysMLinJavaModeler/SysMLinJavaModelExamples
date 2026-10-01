package motorinwheel.components.operatordisplays;

import java.util.ArrayList;
import java.util.Optional;

import motorinwheel.common.ports.information.ElectronicPulsesReceivePort;
import motorinwheel.common.ports.information.SpeedValueDisplayTransmitPort;
import motorinwheel.common.ports.matter.FrontalArealSpeedTransmitPort;
import motorinwheel.components.wheel.WheelLocationEnum;
import motorinwheel.systems.vehicle.Vehicle;
import sysmlinjava.attributetypes.DistanceMeters;
import sysmlinjava.attributetypes.FrequencyHertz;
import sysmlinjava.attributetypes.FrontalArealSpeed;
import sysmlinjava.attributetypes.KeyValueMap;
import sysmlinjava.attributetypes.SpeedKilometersPerHour;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.actions.Calculation;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;

/**
 * Vehicle component that provides a display of the speed for the operator.
 * Function is to provide feedback to operator for his control of the vehicle.
 * 
 * @author ModelerOne
 *
 */
public class OperatorDisplays extends SysMLPart
{
	/**
	 * Port for transmission of the vehicle's speed to the operator, i.e. a
	 * speedometer
	 */
	@Port
	public SpeedValueDisplayTransmitPort speedometer;
	/**
	 * Port for the transmission of frontal areal speed to the atmosphere. (This
	 * would normally be a "port" on the body of the vehicle, but transmitting speed
	 * to the atmosphere from the display was necessary to avoid dead-lock
	 * conditions during execution)
	 */
	@Port
	public FrontalArealSpeedTransmitPort atmosphere;
	/**
	 * Port for the receipt of electronic pulses (for speed calculation) from a
	 * wheel
	 */
	@Port
	public ElectronicPulsesReceivePort wheelPulseReceptionLineLeftFront;
	/**
	 * Port for the receipt of electronic pulses (for speed calculation) from a
	 * wheel
	 */
	@Port
	public ElectronicPulsesReceivePort wheelPulseReceptionLineRightFront;
	/**
	 * Port for the receipt of electronic pulses (for speed calculation) from a
	 * wheel
	 */
	@Port
	public ElectronicPulsesReceivePort wheelPulseReceptionLineLeftRear;
	/**
	 * Port for the receipt of electronic pulses (for speed calculation) from a
	 * wheel
	 */
	@Port
	public ElectronicPulsesReceivePort wheelPulseReceptionLineRightRear;

	/**
	 * Attribute for value of the frquency of the (speed) pulse for a wheel
	 */
	@Attribute
	public FrequencyHertz wheelPulsesInLeftFront;
	/**
	 * Attribute for value of the frquency of the (speed) pulse for a wheel
	 */
	@Attribute
	public FrequencyHertz wheelPulsesInRightFront;
	/**
	 * Attribute for value of the frequency of the (speed) pulse for a wheel
	 */
	@Attribute
	public FrequencyHertz wheelPulsesInLeftRear;
	/**
	 * Attribute for value of the frquency of the (speed) pulse for a wheel
	 */
	@Attribute
	public FrequencyHertz wheelPulsesInRightRear;
	/**
	 * Attribute for value of the frquency of the (speed) pulse for a wheel
	 */
	@Attribute
	public SpeedKilometersPerHour speedViewOut;

	/**
	 * Value of the wheels diameter
	 */
	@Attribute
	public DistanceMeters wheelDiameter;
	/**
	 * Value of the mean frequency of the wheel pulses input. (Speed to be displayed
	 * to operator is the mean of all the wheels)
	 */
	@Attribute
	public FrequencyHertz meanWheelPulsesIn;

	/**
	 * Collection of all the frequencies of the wheel pulses
	 */
	public KeyValueMap<WheelLocationEnum, FrequencyHertz> wheelLocationPulseFrequencies;

	/**
	 * Constructor
	 * 
	 * @param contextPart part in whose context the displays reside
	 * @param name         unique name
	 * @param id           unique ID
	 */
	public OperatorDisplays(SysMLPart contextPart, String name, Long id)
	{
		super(Optional.of(contextPart), name, id);
	}

	/**
	 * Reception that reacts to a new value of the wheel pulse frequency for a
	 * specified wheel
	 * 
	 * @param wheelPulseFrequency the wheel pulse frequency
	 * @param wheelLocation       wheel's location on the vehicle
	 */
	@Action
	public void onWheelPulseFrequency(FrequencyHertz wheelPulseFrequency, WheelLocationEnum wheelLocation)
	{
		if (wheelLocation.equals(WheelLocationEnum.leftFront))
			wheelPulsesInLeftFront.value = wheelPulseFrequency.value;
		else if (wheelLocation.equals(WheelLocationEnum.rightFront))
			wheelPulsesInRightFront.value = wheelPulseFrequency.value;
		else if (wheelLocation.equals(WheelLocationEnum.leftRear))
			wheelPulsesInLeftRear.value = wheelPulseFrequency.value;
		else if (wheelLocation.equals(WheelLocationEnum.rightRear))
			wheelPulsesInRightRear.value = wheelPulseFrequency.value;
		FrequencyHertz frequency = wheelLocationPulseFrequencies.get(wheelLocation);
		if (frequency != null)
			frequency.value = wheelPulseFrequency.value;
		else
			wheelLocationPulseFrequencies.put(wheelLocation, wheelPulseFrequency);
		calculateMeanWheelPulseFrequency();
		calculateSpeedViewOut();
		speedometer.transmit(speedViewOut);
		atmosphere.transmit(new FrontalArealSpeed(Vehicle.frontalArea, speedViewOut));
	}

	private static final double secondsPerHour = 3600;
	private static final double metersPerKilometer = 1000;

	/**
	 * Calculation of the mean frequency of the wheels' pulses
	 */
	@Calculation
	protected void calculateMeanWheelPulseFrequency()
	{
		ArrayList<FrequencyHertz> frequencies = new ArrayList<>();
		frequencies.addAll(wheelLocationPulseFrequencies.values()); // array of frequencies
		frequencies.forEach(frequency -> meanWheelPulsesIn.value += frequency.value); // sum of frequencies
		meanWheelPulsesIn.value /= frequencies.size(); // mean frequency
	}

	/**
	 * Calculation of the speed displayed on the speedometer
	 */
	@Calculation
	protected void calculateSpeedViewOut()
	{
		double speedkmPerHr = meanWheelPulsesIn.value * wheelDiameter.value * Math.PI * secondsPerHour / metersPerKilometer;
		speedViewOut.setValue(speedkmPerHr);
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new OperatorDisplaysStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		wheelDiameter = new DistanceMeters(24 / 39.37007874);
		meanWheelPulsesIn = new FrequencyHertz(0);
		speedViewOut = new SpeedKilometersPerHour(0);
		wheelPulsesInLeftFront = new FrequencyHertz(0);
		wheelPulsesInRightFront = new FrequencyHertz(0);
		wheelPulsesInLeftRear = new FrequencyHertz(0);
		wheelPulsesInRightRear = new FrequencyHertz(0);
		wheelLocationPulseFrequencies = new KeyValueMap<>();
	}

	@Override
	protected void createPorts()
	{
		speedometer = new SpeedValueDisplayTransmitPort(this, 0L);
		atmosphere = new FrontalArealSpeedTransmitPort(this, 0L);
		wheelPulseReceptionLineLeftFront = new ElectronicPulsesReceivePort(this, this, (long)WheelLocationEnum.leftFront.ordinal());
		wheelPulseReceptionLineRightFront = new ElectronicPulsesReceivePort(this, this, (long)WheelLocationEnum.rightFront.ordinal());
		wheelPulseReceptionLineLeftRear = new ElectronicPulsesReceivePort(this, this, (long)WheelLocationEnum.leftRear.ordinal());
		wheelPulseReceptionLineRightRear = new ElectronicPulsesReceivePort(this, this, (long)WheelLocationEnum.rightRear.ordinal());
	}
}
