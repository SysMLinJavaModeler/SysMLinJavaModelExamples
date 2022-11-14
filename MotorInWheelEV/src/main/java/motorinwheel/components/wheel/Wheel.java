package motorinwheel.components.wheel;

import java.time.Instant;
import java.util.Optional;
import motorinwheel.common.ports.energy.MotorTorqueReceivePort;
import motorinwheel.common.ports.energy.RoadwaySurfaceForceTransmitPort;
import motorinwheel.common.ports.energy.RoadwaySurfaceForceReceivePort;
import motorinwheel.common.ports.energy.WheelSuspensionMountForceReceivePort;
import motorinwheel.common.ports.energy.BrakeTorqueReceivePort;
import motorinwheel.common.ports.information.ElectronicPulsesTransmitPort;
import motorinwheel.systems.motorinwheel.MotorInWheelSystem;
import motorinwheel.systems.vehicle.Vehicle;
import sysmlinjava.analysis.bom.annotations.BOMLineItemComment;
import sysmlinjava.analysis.bom.annotations.BOMLineItemValue;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.comments.Comment;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLComment;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.valuetypes.AccelerationKilometersPerHourPerSecond;
import sysmlinjava.valuetypes.DistanceMeters;
import sysmlinjava.valuetypes.ForceNewtons;
import sysmlinjava.valuetypes.FrequencyHertz;
import sysmlinjava.valuetypes.SpeedKilometersPerHour;
import sysmlinjava.valuetypes.TorqueNewtonMeters;

/**
 * The vehicle's wheel. Primary function is to provide forces to the roadway
 * that accelerate and decelerate the vehicle. Wheel includes ports for the
 * receipt of motor torque, brake torque, and suspension weight, as well as
 * forces from the roadway.
 * 
 * @author ModelerOne
 *
 */
public class Wheel extends SysMLBlock
{
	/**
	 * Port for the receipt of the motor's torque by the wheel's disc
	 */
	@FullPort
	public MotorTorqueReceivePort wheelMotorDisc;
	/**
	 * Port for the receipt of the brake's torque by the wheel's disc
	 */
	@FullPort
	public BrakeTorqueReceivePort wheelBrakeDisc;
	/**
	 * Port for the receipt of the roadway's forces the by wheel
	 */
	@FullPort
	public RoadwaySurfaceForceReceivePort roadwaySurface;
	/**
	 * Port for the transmission of the wheel's forces to the roadway
	 */
	@FullPort
	public RoadwaySurfaceForceTransmitPort tireSurface;
	/**
	 * Port for the receipt of the suspension's forces by the wheel
	 */
	@FullPort
	public WheelSuspensionMountForceReceivePort suspensionMount;
	/**
	 * Port for the transmission of the wheel's electronic pulses to the speedometer
	 */
	@FullPort
	public ElectronicPulsesTransmitPort pulseTransmissionLine;

	/**
	 * Flow value of motor torque into the wheel
	 */
	@Flow
	public TorqueNewtonMeters motorTorqueIn;
	/**
	 * Flow value of brake torque into the wheel
	 */
	@Flow
	public TorqueNewtonMeters brakeTorqueIn;
	/**
	 * Flow value of roadway forces into the wheel
	 */
	@Flow
	public ForceNewtons roadwaySurfaceForceIn;
	/**
	 * Flow value of wheel forces out to the roadway
	 */
	@Flow
	public ForceNewtons tireSurfaceForceOut;
	/**
	 * Flow value of suspension forces into the wheel
	 */
	@Flow
	public ForceNewtons suspensionForceIn;
	/**
	 * Flow value of electronic pulses of the wheel out to the speedometer
	 */
	@Flow
	public FrequencyHertz wheelRevolutionPulsesOut;

	/**
	 * Value of the motor's torque force on the wheel
	 */
	@Value
	public ForceNewtons motorTorqueForce;
	/**
	 * Value of the brake's torque force on the wheel
	 */
	@Value
	public ForceNewtons brakeTorqueForce;
	/**
	 * Value of the net horizontal forces on the wheel
	 */
	@Value
	public ForceNewtons wheelHorizontalForce;
	/**
	 * Value of the net vertical forces on the wheel
	 */
	@Value
	public ForceNewtons wheelVerticalForce;
	/**
	 * Value of the net suspension forces on the wheel
	 */
	@Value
	private ForceNewtons suspensionMountForce;
	/**
	 * Value of the wheel's roadway speed
	 */
	@Value
	public SpeedKilometersPerHour wheelSpeed;
	/**
	 * Value of the wheel's roadway acceleration
	 */
	@Value
	private AccelerationKilometersPerHourPerSecond wheelAcceleration;
	/**
	 * Value of the wheel's diameter
	 */
	@BOMLineItemValue
	@Value
	public DistanceMeters wheelDiameter;
	/**
	 * Value of the wheel's location on the vehicle
	 */
	@BOMLineItemValue
	@Value
	public WheelLocationEnum wheelLocation;

	/**
	 * Constraint to calculate the resultant force on the wheel
	 */
	@Constraint
	private SysMLConstraint wheelForcesCalculation;
	/**
	 * Constraint to calculate the wheel's acceleration
	 */
	@Constraint
	public SysMLConstraint wheelAccelerationCalculation;
	/**
	 * Constraint to calculate the wheel's speed
	 */
	@Constraint
	public SysMLConstraint wheelSpeedCalculation;
	/**
	 * Constrait to calculate the wheel's electronic pulse frequency
	 */
	@Constraint
	public SysMLConstraint wheelRevolutionPulseFrequencyCalculation;

	@BOMLineItemComment
	@Comment
	SysMLComment bomComment;

	/**
	 * Time of last change to wheel forces
	 */
	private Instant lastWheelForceChangeTime;

	/**
	 * Constructor
	 * 
	 * @param motorInWheelSystem motor-in-wheel system of which the wheel is a part
	 * @param name               unique name
	 * @param id                 unique ID
	 */
	public Wheel(MotorInWheelSystem motorInWheelSystem, String name, Long id)
	{
		super(motorInWheelSystem, name, id);
		lastWheelForceChangeTime = Instant.now();
	}

	/**
	 * Reception that reacts to a new value of motor torque for the wheel
	 * 
	 * @param torque value of motor torque
	 */
	@Reception
	public void onMotorTorque(TorqueNewtonMeters torque)
	{
		logger.info(wheelLocation.name + ", " + torque.toString());
		motorTorqueIn.value = torque.value;
		wheelForcesCalculation.apply();
		wheelAccelerationCalculation.apply();
		wheelSpeedCalculation.apply();
		wheelRevolutionPulseFrequencyCalculation.apply();
		lastWheelForceChangeTime = Instant.now();
		pulseTransmissionLine.transmit(wheelRevolutionPulsesOut);
	}

	/**
	 * Reception that reacts to a new value of brake torque for the wheel
	 * 
	 * @param torque value of brake torque
	 */
	@Reception
	public void onBrakeTorque(TorqueNewtonMeters torque)
	{
		logger.info(wheelLocation.name + ", " + torque.toString());
		brakeTorqueIn.value = torque.value;
		wheelForcesCalculation.apply();
		wheelAccelerationCalculation.apply();
		wheelSpeedCalculation.apply();
		wheelRevolutionPulseFrequencyCalculation.apply();
		lastWheelForceChangeTime = Instant.now();
		pulseTransmissionLine.transmit(wheelRevolutionPulsesOut);
	}

	/**
	 * Reception that reacts to a new value of roadway forces on the wheel
	 * 
	 * @param force roadway force on the wheel
	 */
	@Reception
	public void onRoadwayForce(ForceNewtons force)
	{
		logger.info(wheelLocation.name + ", " + force.toString());
		roadwaySurfaceForceIn.setValue(force);
		wheelForcesCalculation.apply();
		wheelAccelerationCalculation.apply();
		wheelSpeedCalculation.apply();
		wheelRevolutionPulseFrequencyCalculation.apply();
		lastWheelForceChangeTime = Instant.now();
		pulseTransmissionLine.transmit(wheelRevolutionPulsesOut);
	}

	/**
	 * Reception that reacts to a new value of suspension forces on the wheel
	 * 
	 * @param force roadway force on the wheel
	 */
	@Reception
	public void onSuspensionForce(ForceNewtons force)
	{
		logger.info(wheelLocation.name + ", " + force.toString());
		suspensionForceIn.setValue(force);
		wheelForcesCalculation.apply();
		wheelAccelerationCalculation.apply();
		wheelSpeedCalculation.apply();
		wheelRevolutionPulseFrequencyCalculation.apply();
		lastWheelForceChangeTime = Instant.now();
		// pulseTransmissionLine.transmit(wheelRevolutionPulsesOut);
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new WheelStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		wheelLocation = WheelLocationEnum.leftFront; //Set for real after constructor call
		motorTorqueForce = new ForceNewtons(0, Math.toRadians(90));
		brakeTorqueForce = new ForceNewtons(0, Math.toRadians(270));
		wheelHorizontalForce = new ForceNewtons(0, Math.toRadians(270));
		wheelVerticalForce = new ForceNewtons(Vehicle.vehicleMass.value / Vehicle.kilogramsPerNewton, Math.toRadians(180));
		suspensionMountForce = new ForceNewtons(wheelVerticalForce.value, Math.toRadians(180));
		wheelSpeed = new SpeedKilometersPerHour(0);
		wheelAcceleration = new AccelerationKilometersPerHourPerSecond(0);
		wheelDiameter = new DistanceMeters(24 / 39.37007874);
	}

	@Override
	protected void createFlows()
	{
		motorTorqueIn = new TorqueNewtonMeters(0);
		brakeTorqueIn = new TorqueNewtonMeters(0);
		roadwaySurfaceForceIn = new ForceNewtons(0);
		tireSurfaceForceOut = new ForceNewtons(0);
		suspensionForceIn = new ForceNewtons(Vehicle.vehicleMass.value / Vehicle.kilogramsPerNewton, Math.toRadians(180));
		wheelRevolutionPulsesOut = new FrequencyHertz(0);
	}

	@Override
	protected void createFullPorts()
	{
		wheelMotorDisc = new MotorTorqueReceivePort(this, this, 0L);
		wheelBrakeDisc = new BrakeTorqueReceivePort(this, this, 0L);
		roadwaySurface = new RoadwaySurfaceForceReceivePort(this, this, 0L);
		tireSurface = new RoadwaySurfaceForceTransmitPort(this, 0L);
		suspensionMount = new WheelSuspensionMountForceReceivePort(this, this, 0L);
		pulseTransmissionLine = new ElectronicPulsesTransmitPort(this, 0L);
	}

	public static final double metersPerKilometer = 1000;
	public static final double secondsPerMinute = 60;
	public static final double minutesPerHour = 60;
	public static final double kilogramsPerNewton = 0.101971621;
	public static final double kilometersPerHourPerMetersPerSecond = 3.6;

	@Override
	protected void createConstraints()
	{
		wheelForcesCalculation = () ->
		{
			double torqueForce = (motorTorqueIn.value - brakeTorqueIn.value) / (wheelDiameter.value / 2);
			double airResistanceForce = suspensionForceIn.horizontalComponent().value;
			if (Math.abs(airResistanceForce) < 0.001)
				airResistanceForce = 0;
			double roadwayResistanceForce = roadwaySurfaceForceIn.horizontalComponent().value;
			if (Math.abs(roadwayResistanceForce) < 0.001)
				roadwayResistanceForce = 0;
			double totalHorizontalForce = torqueForce - airResistanceForce - roadwayResistanceForce;
			wheelHorizontalForce.value = Math.abs(totalHorizontalForce);
			wheelHorizontalForce.direction.value = totalHorizontalForce > 0 ? Math.toRadians(270) : Math.toRadians(90);

			double vehicleWeightForce = suspensionForceIn.verticalComponent().value;
			double roadwayLiftForce = roadwaySurfaceForceIn.verticalComponent().value;
			double totalVerticalForce = vehicleWeightForce - roadwayLiftForce;
			wheelVerticalForce.value = Math.abs(totalVerticalForce);
			wheelVerticalForce.direction.value = totalVerticalForce > 0 ? Math.toRadians(180) : Math.toRadians(0);
		};

		wheelAccelerationCalculation = () ->
		{
			double force = wheelHorizontalForce.direction.value < Math.toRadians(180) ? -wheelHorizontalForce.value : wheelHorizontalForce.value;
			double mass = suspensionForceIn.verticalComponent().value * kilogramsPerNewton / 4;
			double accelerationMetersPerSecondPerSecond = force / mass;
			double accelerationKilometersPerHourPerSecond = accelerationMetersPerSecondPerSecond * kilometersPerHourPerMetersPerSecond;
			wheelAcceleration.value = accelerationKilometersPerHourPerSecond;
		};

		wheelSpeedCalculation = () ->
		{
			double seconds = (Instant.now().toEpochMilli() - lastWheelForceChangeTime.toEpochMilli()) / 1000; // dt
			double currentWheelSpeed = wheelSpeed.value;
			wheelSpeed.value = currentWheelSpeed + wheelAcceleration.value * seconds; // s1 = s0 + a * dt;
		};

		wheelRevolutionPulseFrequencyCalculation = () ->
		{
			double metersPerRevolution = wheelDiameter.value * Math.PI;
			double revolutionsPerMinute = (wheelSpeed.value / minutesPerHour) / (metersPerRevolution / metersPerKilometer);
			wheelRevolutionPulsesOut.value = revolutionsPerMinute / secondsPerMinute;
		};
	}

	@Override
	protected void createComments()
	{
		bomComment = new SysMLComment("Description:Wheel component of motor-in-wheel component\nSource:Competitive sourcing TBD");
	}
}