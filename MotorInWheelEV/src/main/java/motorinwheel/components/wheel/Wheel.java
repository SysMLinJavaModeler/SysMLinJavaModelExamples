package motorinwheel.components.wheel;

import java.time.Instant;
import java.util.Optional;

import motorinwheel.common.ports.energy.BrakeTorqueReceivePort;
import motorinwheel.common.ports.energy.MotorTorqueReceivePort;
import motorinwheel.common.ports.energy.RoadwaySurfaceForceReceivePort;
import motorinwheel.common.ports.energy.RoadwaySurfaceForceTransmitPort;
import motorinwheel.common.ports.energy.WheelSuspensionMountForceReceivePort;
import motorinwheel.common.ports.information.ElectronicPulsesTransmitPort;
import motorinwheel.systems.motorinwheel.MotorInWheelSystem;
import motorinwheel.systems.vehicle.Vehicle;
import sysmlinjava.actions.SysMLCalculation;
import sysmlinjava.annotations.SysMLComment;
import sysmlinjava.attributetypes.AccelerationKilometersPerHourPerSecond;
import sysmlinjava.attributetypes.DistanceMeters;
import sysmlinjava.attributetypes.ForceNewtons;
import sysmlinjava.attributetypes.FrequencyHertz;
import sysmlinjava.attributetypes.SpeedKilometersPerHour;
import sysmlinjava.attributetypes.TorqueNewtonMeters;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.actions.Calculation;
import sysmlinjava.javaannotations.annotations.Comment;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.views.bom.annotations.BOMLineItemComment;
import sysmlinjava.views.bom.annotations.BOMLineItemValue;

/**
 * The vehicle's wheel. Primary function is to provide forces to the roadway
 * that accelerate and decelerate the vehicle. Wheel includes ports for the
 * receipt of motor torque, brake torque, and suspension weight, as well as
 * forces from the roadway.
 * 
 * @author ModelerOne
 *
 */
public class Wheel extends SysMLPart
{
	/**
	 * Port for the receipt of the motor's torque by the wheel's disc
	 */
	@Port
	public MotorTorqueReceivePort wheelMotorDisc;
	/**
	 * Port for the receipt of the brake's torque by the wheel's disc
	 */
	@Port
	public BrakeTorqueReceivePort wheelBrakeDisc;
	/**
	 * Port for the receipt of the roadway's forces the by wheel
	 */
	@Port
	public RoadwaySurfaceForceReceivePort roadwaySurface;
	/**
	 * Port for the transmission of the wheel's forces to the roadway
	 */
	@Port
	public RoadwaySurfaceForceTransmitPort tireSurface;
	/**
	 * Port for the receipt of the suspension's forces by the wheel
	 */
	@Port
	public WheelSuspensionMountForceReceivePort suspensionMount;
	/**
	 * Port for the transmission of the wheel's electronic pulses to the speedometer
	 */
	@Port
	public ElectronicPulsesTransmitPort pulseTransmissionLine;

	/**
	 * Attribute for value of motor torque into the wheel
	 */
	@Attribute
	public TorqueNewtonMeters motorTorqueIn;
	/**
	 * Attribute for value of brake torque into the wheel
	 */
	@Attribute
	public TorqueNewtonMeters brakeTorqueIn;
	/**
	 * Attribute for value of roadway forces into the wheel
	 */
	@Attribute
	public ForceNewtons roadwaySurfaceForceIn;
	/**
	 * Attribute for value of wheel forces out to the roadway
	 */
	@Attribute
	public ForceNewtons tireSurfaceForceOut;
	/**
	 * Attribute for value of suspension forces into the wheel
	 */
	@Attribute
	public ForceNewtons suspensionForceIn;
	/**
	 * Attribute for value of electronic pulses of the wheel out to the speedometer
	 */
	@Attribute
	public FrequencyHertz wheelRevolutionPulsesOut;

	/**
	 * Value of the motor's torque force on the wheel
	 */
	@Attribute
	public ForceNewtons motorTorqueForce;
	/**
	 * Value of the brake's torque force on the wheel
	 */
	@Attribute
	public ForceNewtons brakeTorqueForce;
	/**
	 * Value of the net horizontal forces on the wheel
	 */
	@Attribute
	public ForceNewtons wheelHorizontalForce;
	/**
	 * Value of the net vertical forces on the wheel
	 */
	@Attribute
	public ForceNewtons wheelVerticalForce;
	/**
	 * Value of the net suspension forces on the wheel
	 */
	@Attribute
	private ForceNewtons suspensionMountForce;
	/**
	 * Value of the wheel's roadway speed
	 */
	@Attribute
	public SpeedKilometersPerHour wheelSpeed;
	/**
	 * Value of the wheel's roadway acceleration
	 */
	@Attribute
	private AccelerationKilometersPerHourPerSecond wheelAcceleration;
	/**
	 * Value of the wheel's diameter
	 */
	@BOMLineItemValue
	@Attribute
	public DistanceMeters wheelDiameter;
	/**
	 * Value of the wheel's location on the vehicle
	 */
	@BOMLineItemValue
	@Attribute
	public WheelLocationEnum wheelLocation;

	/**
	 * Calculation to calculate the resultant force on the wheel
	 */
	@Calculation
	private SysMLCalculation wheelForcesCalculation;
	/**
	 * Calculation to calculate the wheel's acceleration
	 */
	@Calculation
	public SysMLCalculation wheelAccelerationCalculation;
	/**
	 * Calculation to calculate the wheel's speed
	 */
	@Calculation
	public SysMLCalculation wheelSpeedCalculation;
	/**
	 * Constrait to calculate the wheel's electronic pulse frequency
	 */
	@Calculation
	public SysMLCalculation wheelRevolutionPulseFrequencyCalculation;

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
		super(Optional.of(motorInWheelSystem), name, id);
		lastWheelForceChangeTime = Instant.now();
	}

	/**
	 * Reception that reacts to a new value of motor torque for the wheel
	 * 
	 * @param torque value of motor torque
	 */
	@Action
	public void onMotorTorque(TorqueNewtonMeters torque)
	{
		motorTorqueIn.value = torque.value;
		calculateWheelForces();
		calculateWheelAcceleration();
		calculateWheelSpeed();
		calculateWheelRevolutionPulsesOut();
		lastWheelForceChangeTime = Instant.now();
		pulseTransmissionLine.transmit(wheelRevolutionPulsesOut);
	}

	/**
	 * Action that reacts to a new value of brake torque for the wheel
	 * 
	 * @param torque value of brake torque
	 */
	@Action
	public void onBrakeTorque(TorqueNewtonMeters torque)
	{
		brakeTorqueIn.value = torque.value;
		calculateWheelForces();
		calculateWheelAcceleration();
		calculateWheelSpeed();
		calculateWheelRevolutionPulsesOut();
		lastWheelForceChangeTime = Instant.now();
		pulseTransmissionLine.transmit(wheelRevolutionPulsesOut);
	}

	/**
	 * Action that reacts to a new value of roadway forces on the wheel
	 * 
	 * @param force roadway force on the wheel
	 */
	@Action
	public void onRoadwayForce(ForceNewtons force)
	{
		roadwaySurfaceForceIn.setValue(force);
		calculateWheelForces();
		calculateWheelAcceleration();
		calculateWheelSpeed();
		calculateWheelRevolutionPulsesOut();
		lastWheelForceChangeTime = Instant.now();
		pulseTransmissionLine.transmit(wheelRevolutionPulsesOut);
	}

	/**
	 * Action that reacts to a new value of suspension forces on the wheel
	 * 
	 * @param force roadway force on the wheel
	 */
	@Action
	public void onSuspensionForce(ForceNewtons force)
	{
		suspensionForceIn.setValue(force);
		calculateWheelForces();
		calculateWheelAcceleration();
		calculateWheelSpeed();
		calculateWheelRevolutionPulsesOut();
		lastWheelForceChangeTime = Instant.now();
		// pulseTransmissionLine.transmit(wheelRevolutionPulsesOut);
	}

	@Calculation
	private void calculateWheelForces()
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
	}

	@Calculation
	private void calculateWheelAcceleration()
	{
		double force = wheelHorizontalForce.direction.value < Math.toRadians(180) ? -wheelHorizontalForce.value : wheelHorizontalForce.value;
		double mass = suspensionForceIn.verticalComponent().value * kilogramsPerNewton / 4;
		double accelerationMetersPerSecondPerSecond = force / mass;
		double accelerationKilometersPerHourPerSecond = accelerationMetersPerSecondPerSecond * kilometersPerHourPerMetersPerSecond;
		wheelAcceleration.value = accelerationKilometersPerHourPerSecond;
	}

	@Calculation
	private void calculateWheelSpeed()
	{
		double seconds = (Instant.now().toEpochMilli() - lastWheelForceChangeTime.toEpochMilli()) / 1000; // dt
		double currentWheelSpeed = wheelSpeed.value;
		wheelSpeed.value = currentWheelSpeed + wheelAcceleration.value * seconds; // s1 = s0 + a * dt;
	}

	@Calculation
	private void calculateWheelRevolutionPulsesOut()
	{
		double metersPerRevolution = wheelDiameter.value * Math.PI;
		double revolutionsPerMinute = (wheelSpeed.value / minutesPerHour) / (metersPerRevolution / metersPerKilometer);
		wheelRevolutionPulsesOut.value = revolutionsPerMinute / secondsPerMinute;
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new WheelStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		wheelLocation = WheelLocationEnum.leftFront; // Set for real after constructor call
		motorTorqueForce = new ForceNewtons(0, Math.toRadians(90));
		brakeTorqueForce = new ForceNewtons(0, Math.toRadians(270));
		wheelHorizontalForce = new ForceNewtons(0, Math.toRadians(270));
		wheelVerticalForce = new ForceNewtons(Vehicle.vehicleMass.value / Vehicle.kilogramsPerNewton, Math.toRadians(180));
		suspensionMountForce = new ForceNewtons(wheelVerticalForce.value, Math.toRadians(180));
		wheelSpeed = new SpeedKilometersPerHour(0);
		wheelAcceleration = new AccelerationKilometersPerHourPerSecond(0);
		wheelDiameter = new DistanceMeters(24 / 39.37007874);
		motorTorqueIn = new TorqueNewtonMeters(0);
		brakeTorqueIn = new TorqueNewtonMeters(0);
		roadwaySurfaceForceIn = new ForceNewtons(0);
		tireSurfaceForceOut = new ForceNewtons(0);
		suspensionForceIn = new ForceNewtons(Vehicle.vehicleMass.value / Vehicle.kilogramsPerNewton, Math.toRadians(180));
		wheelRevolutionPulsesOut = new FrequencyHertz(0);
	}

	@Override
	protected void createPorts()
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
	protected void createComments()
	{
		bomComment = new SysMLComment("Description:Wheel component of motor-in-wheel component\nSource:Competitive sourcing TBD");
	}
}