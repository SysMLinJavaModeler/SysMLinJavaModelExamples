package motorinwheel.components.operatordisplays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import motorinwheel.common.ports.information.ElectronicPulsesReceivePort;
import motorinwheel.components.wheel.WheelLocationEnum;
import motorinwheel.systems.vehicle.Vehicle;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.kinds.SysMLFlowDirectionKind;
import sysmlinjava.valuetypes.FrontalArealSpeed;
import sysmlinjava.valuetypes.DistanceMeters;
import sysmlinjava.valuetypes.FrequencyHertz;
import sysmlinjava.valuetypes.SpeedKilometersPerHour;
import motorinwheel.common.ports.information.SpeedValueDisplayTransmitPort;
import motorinwheel.common.ports.matter.FrontalArealSpeedTransmitPort;

/**
 * Vehicle component that provides a display of the speed for the operator.
 * Function is to provide feedback to operator for his control of the vehicle.
 * 
 * @author ModelerOne
 *
 */
public class OperatorDisplays extends SysMLBlock
{
	/**
	 * Port for transmission of the vehicle's speed to the operator, i.e. a
	 * speedometer
	 */
	@FullPort
	public SpeedValueDisplayTransmitPort speedometer;
	/**
	 * Port for the transmission of frontal areal speed to the atmosphere. (This
	 * would normally be a "port" on the body of the vehicle, but transmitting speed
	 * to the atmosphere from the display was necessary to avoid dead-lock
	 * conditions during execution)
	 */
	@FullPort
	public FrontalArealSpeedTransmitPort atmosphere;
	/**
	 * Port for the receipt of electronic pulses (for speed calculation) from a
	 * wheel
	 */
	@FullPort
	public ElectronicPulsesReceivePort wheelPulseReceptionLineLeftFront;
	/**
	 * Port for the receipt of electronic pulses (for speed calculation) from a
	 * wheel
	 */
	@FullPort
	public ElectronicPulsesReceivePort wheelPulseReceptionLineRightFront;
	/**
	 * Port for the receipt of electronic pulses (for speed calculation) from a
	 * wheel
	 */
	@FullPort
	public ElectronicPulsesReceivePort wheelPulseReceptionLineLeftRear;
	/**
	 * Port for the receipt of electronic pulses (for speed calculation) from a
	 * wheel
	 */
	@FullPort
	public ElectronicPulsesReceivePort wheelPulseReceptionLineRightRear;

	/**
	 * Flow value of the frquency of the (speed) pulse for a wheel
	 */
	@Flow(direction = SysMLFlowDirectionKind.in)
	public FrequencyHertz wheelPulsesInLeftFront;
	/**
	 * Flow value of the frquency of the (speed) pulse for a wheel
	 */
	@Flow(direction = SysMLFlowDirectionKind.in)
	public FrequencyHertz wheelPulsesInRightFront;
	/**
	 * Flow value of the frquency of the (speed) pulse for a wheel
	 */
	@Flow(direction = SysMLFlowDirectionKind.in)
	public FrequencyHertz wheelPulsesInLeftRear;
	/**
	 * Flow value of the frquency of the (speed) pulse for a wheel
	 */
	@Flow(direction = SysMLFlowDirectionKind.in)
	public FrequencyHertz wheelPulsesInRightRear;
	/**
	 * Flow value of the frquency of the (speed) pulse for a wheel
	 */
	@Flow(direction = SysMLFlowDirectionKind.out)
	public SpeedKilometersPerHour speedViewOut;

	/**
	 * Value of the wheels diameter
	 */
	@Value
	public DistanceMeters wheelDiameter;
	/**
	 * Value of the mean frequency of the wheel pulses input. (Speed to be displayed
	 * to operator is the mean of all the wheels)
	 */
	@Value
	public FrequencyHertz meanWheelPulsesIn;

	/**
	 * Collection of all the frequencies of the wheel pulses
	 */
	public Map<WheelLocationEnum, FrequencyHertz> wheelLocationPulseFrequencies;

	/**
	 * Constraint to calculate the mean frequency of the wheels' pulses
	 */
	@Constraint
	public SysMLConstraint meanWheelPulseFrequencyCalculation;
	/**
	 * Constraint to calculate the speed displayed on the speedometer
	 */
	@Constraint
	public SysMLConstraint speedViewCalculation;

	/**
	 * Constructor
	 * 
	 * @param contextBlock block in whose context the displays reside
	 * @param name         unique name
	 * @param id           unique ID
	 */
	public OperatorDisplays(SysMLBlock contextBlock, String name, Long id)
	{
		super(contextBlock, name, id);
	}

	/**
	 * Reception that reacts to a new value of the wheel pulse frequency for a
	 * specified wheel
	 * 
	 * @param wheelPulseFrequency the wheel pulse frequency
	 * @param wheelLocation       wheel's location on the vehicle
	 */
	@Reception
	public void onWheelPulseFrequency(FrequencyHertz wheelPulseFrequency, WheelLocationEnum wheelLocation)
	{
		logger.info(wheelPulseFrequency.toString() + ", " + wheelLocation.toString());
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
		meanWheelPulseFrequencyCalculation.apply();
		speedViewCalculation.apply();
		speedometer.transmit(speedViewOut);
		atmosphere.transmit(new FrontalArealSpeed(Vehicle.frontalArea, speedViewOut));
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new OperatorDisplaysStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		wheelDiameter = new DistanceMeters(24 / 39.37007874);
		meanWheelPulsesIn = new FrequencyHertz(0);
	}

	@Override
	protected void createReferences()
	{
		wheelLocationPulseFrequencies = new HashMap<>();
	}

	@Override
	protected void createFlows()
	{
		speedViewOut = new SpeedKilometersPerHour(0);
		wheelPulsesInLeftFront = new FrequencyHertz(0);
		wheelPulsesInRightFront = new FrequencyHertz(0);
		wheelPulsesInLeftRear = new FrequencyHertz(0);
		wheelPulsesInRightRear = new FrequencyHertz(0);
	}

	@Override
	protected void createFullPorts()
	{
		speedometer = new SpeedValueDisplayTransmitPort(this, 0L);
		atmosphere = new FrontalArealSpeedTransmitPort(this, 0L);
		wheelPulseReceptionLineLeftFront = new ElectronicPulsesReceivePort(this, this, (long)WheelLocationEnum.leftFront.ordinal);
		wheelPulseReceptionLineRightFront = new ElectronicPulsesReceivePort(this, this, (long)WheelLocationEnum.rightFront.ordinal);
		wheelPulseReceptionLineLeftRear = new ElectronicPulsesReceivePort(this, this, (long)WheelLocationEnum.leftRear.ordinal);
		wheelPulseReceptionLineRightRear = new ElectronicPulsesReceivePort(this, this, (long)WheelLocationEnum.rightRear.ordinal);
	}

	private static final double secondsPerHour = 3600;
	private static final double metersPerKilometer = 1000;

	@Override
	protected void createConstraints()
	{
		meanWheelPulseFrequencyCalculation = () ->
		{
			ArrayList<FrequencyHertz> frequencies = new ArrayList<>();
			frequencies.addAll(wheelLocationPulseFrequencies.values()); // array of frequencies
			frequencies.forEach(frequency -> meanWheelPulsesIn.value += frequency.value); // sum of frequencies
			meanWheelPulsesIn.value /= frequencies.size(); // mean frequency
		};

		speedViewCalculation = () ->
		{
			double speedkmPerHr = meanWheelPulsesIn.value * wheelDiameter.value * Math.PI * secondsPerHour / metersPerKilometer;
			speedViewOut.setValue(speedkmPerHr);
		};
	}
}
