package motorinwheel.components.suspensionchassisbody;

import java.util.Arrays;
import java.util.Optional;
import motorinwheel.common.ports.energy.BrakeSuspensionMountForceReceivePort;
import motorinwheel.common.ports.energy.MotorSuspensionMountForceReceivePort;
import motorinwheel.common.ports.energy.WheelSuspensionMountForceTransmitPort;
import motorinwheel.common.ports.matter.AirResistanceReceivePort;
import motorinwheel.components.wheel.WheelLocationEnum;
import motorinwheel.systems.vehicle.Vehicle;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.valuetypes.ForceNewtons;

/**
 * The vehicle's suspension/chassis/body assembly that suspends/transfers the
 * weights of other parts of the vehicle and interfaces with the atmosphere the
 * vehicle drives through.
 * 
 * @author ModelerOne
 *
 */
public class SuspensionChassisBody extends SysMLBlock
{
	/**
	 * Port for the transmission of suspension forces onto a wheel
	 */
	@FullPort
	public WheelSuspensionMountForceTransmitPort wheelMountLeftFront;
	/**
	 * Port for the transmission of suspension forces onto a wheel
	 */
	@FullPort
	public WheelSuspensionMountForceTransmitPort wheelMountRightFront;
	/**
	 * Port for the transmission of suspension forces onto a wheel
	 */
	@FullPort
	public WheelSuspensionMountForceTransmitPort wheelMountLeftRear;
	/**
	 * Port for the transmission of suspension forces onto a wheel
	 */
	@FullPort
	public WheelSuspensionMountForceTransmitPort wheelMountRightRear;
	/**
	 * Port for the receipt of a wheel's motor forces onto the suspension
	 */
	@FullPort
	public MotorSuspensionMountForceReceivePort motorMountLeftFront;
	/**
	 * Port for the receipt of a wheel's motor forces onto the suspension
	 */
	@FullPort
	public MotorSuspensionMountForceReceivePort motorMountRightFront;
	/**
	 * Port for the receipt of a wheel's motor forces onto the suspension
	 */
	@FullPort
	public MotorSuspensionMountForceReceivePort motorMountLeftRear;
	/**
	 * Port for the receipt of a wheel's motor forces onto the suspension
	 */
	@FullPort
	public MotorSuspensionMountForceReceivePort motorMountRightRear;
	/**
	 * Port for the receipt of a wheel's brake forces onto the suspension
	 */
	@FullPort
	public BrakeSuspensionMountForceReceivePort brakeMountLeftFront;
	/**
	 * Port for the receipt of a wheel's brake forces onto the suspension
	 */
	@FullPort
	public BrakeSuspensionMountForceReceivePort brakeMountRightFront;
	/**
	 * Port for the receipt of a wheel's brake forces onto the suspension
	 */
	@FullPort
	public BrakeSuspensionMountForceReceivePort brakeMountLeftRear;
	/**
	 * Port for the receipt of a wheel's brake forces onto the suspension
	 */
	@FullPort
	public BrakeSuspensionMountForceReceivePort brakeMountRightRear;
	/**
	 * Port for the receipt of the body's air resistance force onto the suspension
	 */
	@FullPort
	public AirResistanceReceivePort airResistence;

	/**
	 * Flow value of the air resistance force into the suspension
	 */
	@Flow
	public ForceNewtons airResistanceIn;
	/**
	 * Flow value of the suspension's forces out to a wheel
	 */
	@Flow
	public ForceNewtons wheelForcesOutLeftFront;
	/**
	 * Flow value of the suspension's forces out to a wheel
	 */
	@Flow
	public ForceNewtons wheelForcesOutRightFront;
	/**
	 * Flow value of the suspension's forces out to a wheel
	 */
	@Flow
	public ForceNewtons wheelForcesOutLeftRear;
	/**
	 * Flow value of the suspension's forces out to a wheel
	 */
	@Flow
	public ForceNewtons wheelForcesOutRightRear;
	/**
	 * Flow value of a motor's forces (weight) into the suspension
	 */
	@Flow
	public ForceNewtons motorWeightInLeftFront;
	/**
	 * Flow value of a motor's forces (weight) into the suspension
	 */
	@Flow
	public ForceNewtons motorWeightInRightFront;
	/**
	 * Flow value of a motor's forces (weight) into the suspension
	 */
	@Flow
	public ForceNewtons motorWeightInLeftRear;
	@Flow
	/**
	 * Flow value of a motor's forces (weight) into the suspension
	 */
	public ForceNewtons motorWeightInRightRear;
	/**
	 * Flow value of a brake's forces (weight) into the suspension
	 */
	@Flow
	public ForceNewtons brakeWeightInLeftFront;
	/**
	 * Flow value of a brake's forces (weight) into the suspension
	 */
	@Flow
	public ForceNewtons brakeWeightInRightFront;
	/**
	 * Flow value of a brake's forces (weight) into the suspension
	 */
	@Flow
	public ForceNewtons brakeWeightInLeftRear;
	/**
	 * Flow value of a brake's forces (weight) into the suspension
	 */
	@Flow
	public ForceNewtons brakeWeightInRightRear;

	/**
	 * Value of the air resistance forces being transmitted into each wheel
	 */
	@Value
	ForceNewtons airResistanceForcesInPerWheel;
	/**
	 * Value of the vehicle's weight forces being transmitted into each wheel
	 */
	@Value
	ForceNewtons vehicleWeightForcesInPerWheel;

	/**
	 * Constraint that calculates the resultant forces being transmitted to the
	 * left-front wheel.
	 */
	@Constraint
	public SysMLConstraint wheelForcesCalculationLeftFront;
	/**
	 * Constraint that calculates the resultant forces being transmitted to the
	 * right-front wheel.
	 */
	@Constraint
	public SysMLConstraint wheelForcesCalculationRightFront;
	/**
	 * Constraint that calculates the resultant forces being transmitted to the
	 * left-rear wheel.
	 */
	@Constraint
	public SysMLConstraint wheelForcesCalculationLeftRear;
	/**
	 * Constraint that calculates the resultant forces being transmitted to the
	 * right-rear wheel.
	 */
	@Constraint
	public SysMLConstraint wheelForcesCalculationRightRear;

	/**
	 * Constructor
	 * 
	 * @param vehicle vehicle of which the suspension/chassis/body is a part
	 * @param name    unique name
	 * @param id      unique ID
	 */
	public SuspensionChassisBody(Vehicle vehicle, String name, Long id)
	{
		super(vehicle, name, id);
	}

	/**
	 * Reception that reacts to the receipt of a value of the brake's weight for a
	 * wheel
	 * 
	 * @param brakeWeight weight of the brake
	 * @param location    wheel location
	 */
	@Reception
	public void onBrakeWeight(ForceNewtons brakeWeight, WheelLocationEnum location)
	{
		logger.info(brakeWeight.toString() + " " + location.toString());
		if (location.equals(WheelLocationEnum.leftFront))
		{
			brakeWeightInLeftFront.value = brakeWeight.value;
			wheelForcesCalculationLeftFront.apply();
			wheelMountLeftFront.transmit(wheelForcesOutLeftFront);
		}
		else if (location.equals(WheelLocationEnum.rightFront))
		{
			brakeWeightInRightFront.value = brakeWeight.value;
			wheelForcesCalculationRightFront.apply();
			wheelMountRightFront.transmit(wheelForcesOutRightFront);
		}
		else if (location.equals(WheelLocationEnum.leftRear))
		{
			brakeWeightInLeftRear.value = brakeWeight.value;
			wheelForcesCalculationLeftRear.apply();
			wheelMountLeftRear.transmit(wheelForcesOutLeftRear);
		}
		else if (location.equals(WheelLocationEnum.rightRear))
		{
			brakeWeightInRightRear.value = brakeWeight.value;
			wheelForcesCalculationRightRear.apply();
			wheelMountRightRear.transmit(wheelForcesOutRightRear);
		}
	}

	/**
	 * Reception that reacts to the receipt of a value of the motor's weight for a
	 * wheel
	 * 
	 * @param motorWeight weight of the motor
	 * @param location    wheel location
	 */
	@Reception
	public void onMotorWeight(ForceNewtons motorWeight, WheelLocationEnum location)
	{
		logger.info(motorWeight.toString() + " " + location.toString());
		if (location.equals(WheelLocationEnum.leftFront))
		{
			motorWeightInLeftFront.value = motorWeight.value;
			wheelForcesCalculationLeftFront.apply();
			wheelMountLeftFront.transmit(wheelForcesOutLeftFront);
		}
		else if (location.equals(WheelLocationEnum.rightFront))
		{
			motorWeightInRightFront.value = motorWeight.value;
			wheelForcesCalculationRightFront.apply();
			wheelMountRightFront.transmit(wheelForcesOutRightFront);
		}
		else if (location.equals(WheelLocationEnum.leftRear))
		{
			motorWeightInLeftRear.value = motorWeight.value;
			wheelForcesCalculationLeftRear.apply();
			wheelMountLeftRear.transmit(wheelForcesOutLeftRear);
		}
		else if (location.equals(WheelLocationEnum.rightRear))
		{
			motorWeightInRightRear.value = motorWeight.value;
			wheelForcesCalculationRightRear.apply();
			wheelMountRightRear.transmit(wheelForcesOutRightRear);
		}
	}

	/**
	 * Reception that reacts to the receipt of a value of the air resistance force
	 * on the body
	 * 
	 * @param airResistance air resistance force
	 */
	@Reception
	public void onAirResistance(ForceNewtons airResistance)
	{
		logger.info(airResistance.toString());
		airResistanceIn.setValue(airResistance);
		airResistanceForcesInPerWheel.value = airResistance.value / 4;
		wheelForcesCalculationLeftFront.apply();
		wheelForcesCalculationRightFront.apply();
		wheelForcesCalculationLeftRear.apply();
		wheelForcesCalculationRightRear.apply();
		wheelMountLeftFront.transmit(wheelForcesOutLeftFront);
		wheelMountRightFront.transmit(wheelForcesOutRightFront);
		wheelMountLeftRear.transmit(wheelForcesOutLeftRear);
		wheelMountRightRear.transmit(wheelForcesOutRightRear);
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new SuspensionChassisBodyStateMachine(this));
	}

	public static final double kilogramsPerNewton = 0.101971621;

	@Override
	protected void createValues()
	{
		airResistanceForcesInPerWheel = new ForceNewtons(0, Math.toRadians(270));
		vehicleWeightForcesInPerWheel = new ForceNewtons((Vehicle.vehicleMass.value / kilogramsPerNewton) / 4, Math.toRadians(180));
	}

	@Override
	protected void createFlows()
	{
		airResistanceIn = new ForceNewtons(0, Math.toRadians(270));
		wheelForcesOutLeftFront = new ForceNewtons(0, Math.toRadians(180));
		wheelForcesOutRightFront = new ForceNewtons(0, Math.toRadians(180));
		wheelForcesOutLeftRear = new ForceNewtons(0, Math.toRadians(180));
		wheelForcesOutRightRear = new ForceNewtons(0, Math.toRadians(180));
		motorWeightInLeftFront = new ForceNewtons(0, Math.toRadians(180));
		motorWeightInRightFront = new ForceNewtons(0, Math.toRadians(180));
		motorWeightInLeftRear = new ForceNewtons(0, Math.toRadians(180));
		motorWeightInRightRear = new ForceNewtons(0, Math.toRadians(180));
		brakeWeightInLeftFront = new ForceNewtons(0, Math.toRadians(180));
		brakeWeightInRightFront = new ForceNewtons(0, Math.toRadians(180));
		brakeWeightInLeftRear = new ForceNewtons(0, Math.toRadians(180));
		brakeWeightInRightRear = new ForceNewtons(0, Math.toRadians(180));
	}

	@Override
	protected void createFullPorts()
	{
		wheelMountLeftFront = new WheelSuspensionMountForceTransmitPort(this, (long)WheelLocationEnum.leftFront.ordinal);
		wheelMountRightFront = new WheelSuspensionMountForceTransmitPort(this, (long)WheelLocationEnum.rightFront.ordinal);
		wheelMountLeftRear = new WheelSuspensionMountForceTransmitPort(this, (long)WheelLocationEnum.leftRear.ordinal);
		wheelMountRightRear = new WheelSuspensionMountForceTransmitPort(this, (long)WheelLocationEnum.rightRear.ordinal);
		motorMountLeftFront = new MotorSuspensionMountForceReceivePort(this, this, (long)WheelLocationEnum.leftFront.ordinal);
		motorMountRightFront = new MotorSuspensionMountForceReceivePort(this, this, (long)WheelLocationEnum.rightFront.ordinal);
		motorMountLeftRear = new MotorSuspensionMountForceReceivePort(this, this, (long)WheelLocationEnum.leftRear.ordinal);
		motorMountRightRear = new MotorSuspensionMountForceReceivePort(this, this, (long)WheelLocationEnum.rightRear.ordinal);
		brakeMountLeftFront = new BrakeSuspensionMountForceReceivePort(this, this, (long)WheelLocationEnum.leftFront.ordinal);
		brakeMountRightFront = new BrakeSuspensionMountForceReceivePort(this, this, (long)WheelLocationEnum.rightFront.ordinal);
		brakeMountLeftRear = new BrakeSuspensionMountForceReceivePort(this, this, (long)WheelLocationEnum.leftRear.ordinal);
		brakeMountRightRear = new BrakeSuspensionMountForceReceivePort(this, this, (long)WheelLocationEnum.rightRear.ordinal);
		airResistence = new AirResistanceReceivePort(this, this, 0L);
	}

	@Override
	protected void createConstraints()
	{
		wheelForcesCalculationLeftFront = () ->
		{
			ForceNewtons wheelForces = ForceNewtons.sum(Arrays.asList(brakeWeightInLeftFront, motorWeightInLeftFront, airResistanceForcesInPerWheel, vehicleWeightForcesInPerWheel));
			wheelForcesOutLeftFront.setValue(wheelForces);
		};
		wheelForcesCalculationRightFront = () ->
		{
			ForceNewtons wheelForces = ForceNewtons.sum(Arrays.asList(brakeWeightInRightFront, motorWeightInRightFront, airResistanceForcesInPerWheel, vehicleWeightForcesInPerWheel));
			wheelForcesOutRightFront.setValue(wheelForces);
		};
		wheelForcesCalculationLeftRear = () ->
		{
			ForceNewtons wheelForces = ForceNewtons.sum(Arrays.asList(brakeWeightInLeftRear, motorWeightInLeftRear, airResistanceForcesInPerWheel, vehicleWeightForcesInPerWheel));
			wheelForcesOutLeftRear.setValue(wheelForces);
		};
		wheelForcesCalculationRightRear = () ->
		{
			ForceNewtons wheelForces = ForceNewtons.sum(Arrays.asList(brakeWeightInRightRear, motorWeightInRightRear, airResistanceForcesInPerWheel, vehicleWeightForcesInPerWheel));
			wheelForcesOutRightRear.setValue(wheelForces);
		};
	}
}
