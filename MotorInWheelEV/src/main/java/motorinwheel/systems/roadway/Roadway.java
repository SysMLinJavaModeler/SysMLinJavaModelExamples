package motorinwheel.systems.roadway;

import java.util.Arrays;
import java.util.Optional;
import motorinwheel.common.ports.energy.RoadwaySurfaceForceTransmitPort;
import motorinwheel.common.ports.energy.RoadwaySurfaceForceReceivePort;
import motorinwheel.components.wheel.WheelLocationEnum;
import motorinwheel.domain.MotorInWheelEVDomain;
import motorinwheel.systems.vehicle.Vehicle;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.valuetypes.ForceNewtons;
import sysmlinjava.valuetypes.RReal;

/**
 * The roadway on which the MotorInWheelEV travels. The roadway functions to
 * lift/support the wheels of the vehicle as well as impose friction on the
 * them. {@code Roadway} consists mainly of two ports (vehicle wheel forces into
 * the roadway and roadway forces into the wheel) for each of the vehicles
 * wheels, and the flows of forces associated with the ports. It also contains
 * the constraint that calculates the net forces on the roadway from the wheel
 * forces received from the vehicle and roadway forces transmitted to the
 * vehicle
 * 
 * @author ModelerOne
 *
 */
public class Roadway extends SysMLBlock
{
	/**
	 * Port for the receipt of a wheel's forces (weight and torque) on the roadway
	 */
	@FullPort
	public RoadwaySurfaceForceReceivePort wheelTireSurfaceLeftFront;
	/**
	 * Port for the receipt of a wheel's forces (weight and torque) on the roadway
	 */
	@FullPort
	public RoadwaySurfaceForceReceivePort wheelTireSurfaceRightFront;
	/**
	 * Port for the receipt of a wheel's forces (weight and torque) on the roadway
	 */
	@FullPort
	public RoadwaySurfaceForceReceivePort wheelTireSurfaceLeftRear;
	/**
	 * Port for the receipt of a wheel's forces (weight and torque) on the roadway
	 */
	@FullPort
	public RoadwaySurfaceForceReceivePort wheelTireSurfaceRightRear;
	/**
	 * Port for the transmission of roadway forces (lift and friction) to a wheel
	 */
	@FullPort
	public RoadwaySurfaceForceTransmitPort roadwaySurfaceLeftFront;
	/**
	 * Port for the transmission of roadway forces (lift and friction) to a wheel
	 */
	@FullPort
	public RoadwaySurfaceForceTransmitPort roadwaySurfaceRightFront;
	/**
	 * Port for the transmission of roadway forces (lift and friction) to a wheel
	 */
	@FullPort
	public RoadwaySurfaceForceTransmitPort roadwaySurfaceLeftRear;
	/**
	 * Port for the transmission of roadway forces (lift and friction) to a wheel
	 */
	@FullPort
	public RoadwaySurfaceForceTransmitPort roadwaySurfaceRightRear;

	/**
	 * Flow of one wheel's forces on the roadway
	 */
	@Flow
	public ForceNewtons vehicleForcesInLeftFront;
	/**
	 * Flow of one wheel's forces on the roadway
	 */
	@Flow
	public ForceNewtons vehicleForcesInRightFront;
	/**
	 * Flow of one wheel's forces on the roadway
	 */
	@Flow
	public ForceNewtons vehicleForcesInLeftRear;
	/**
	 * Flow of one wheel's forces on the roadway
	 */
	@Flow
	public ForceNewtons vehicleForcesInRightRear;
	/**
	 * Flow of the roadway's forces on a wheel
	 */
	@Flow
	public ForceNewtons roadwayForcesOutLeftFront;
	/**
	 * Flow of the roadway's forces on a wheel
	 */
	@Flow
	public ForceNewtons roadwayForcesOutRightFront;
	/**
	 * Flow of the roadway's forces on a wheel
	 */
	@Flow
	public ForceNewtons roadwayForcesOutLeftRear;
	/**
	 * Flow of the roadway's forces on a wheel
	 */
	@Flow
	public ForceNewtons roadwayForcesOutRightRear;

	/**
	 * Value of the coefficient of friction between wheel and roadway
	 */
	@Value
	public RReal coefficientOfFriction;

	/**
	 * Constraint for the calculation of net forces on the roadway for the left-front wheel
	 */
	@Constraint
	public SysMLConstraint roadwayForcesCalculationLeftFront;
	/**
	 * Constraint for the calculation of net forces on the roadway for the right-front wheel
	 */
	@Constraint
	public SysMLConstraint roadwayForcesCalculationRightFront;
	/**
	 * Constraint for the calculation of net forces on the roadway for the left-rear wheel
	 */
	@Constraint
	public SysMLConstraint roadwayForcesCalculationLeftRear;
	/**
	 * Constraint for the calculation of net forces on the roadway for the right-rear wheel
	 */
	@Constraint
	public SysMLConstraint roadwayForcesCalculationRightRear;

	/**
	 * Constructor; initializes the roadway forces based on initial flow values
	 * 
	 * @param domain the domain in which the roadway exists
	 */
	public Roadway(MotorInWheelEVDomain domain)
	{
		super(domain, "Roadway", 0L);
		roadwayForcesCalculationLeftFront.apply();
		roadwayForcesCalculationRightFront.apply();
		roadwayForcesCalculationLeftRear.apply();
		roadwayForcesCalculationRightRear.apply();
	}

	/**
	 * Reception that reacts to receipt of new forces on a specified wheel
	 * 
	 * @param force         new forces value
	 * @param wheelLocation specified wheel
	 */
	@Reception
	public void onVehicleForces(ForceNewtons force, WheelLocationEnum wheelLocation)
	{
		if (wheelLocation == WheelLocationEnum.leftFront)
		{
			if (Math.abs(force.value - vehicleForcesInLeftFront.value) > 100.0)
			{
				roadwayForcesCalculationLeftFront.apply();
				roadwaySurfaceLeftFront.transmit(roadwayForcesOutLeftFront);
			}
		}
		else if (wheelLocation == WheelLocationEnum.rightFront)
		{
			if (Math.abs(force.value - vehicleForcesInRightFront.value) > 100.0)
			{
				roadwayForcesCalculationRightFront.apply();
				roadwaySurfaceRightFront.transmit(roadwayForcesOutRightFront);
			}
		}
		else if (wheelLocation == WheelLocationEnum.leftRear)
		{
			if (Math.abs(force.value - vehicleForcesInLeftRear.value) > 100.0)
			{
				roadwayForcesCalculationLeftRear.apply();
				roadwaySurfaceLeftRear.transmit(roadwayForcesOutLeftRear);
			}
		}
		else if (wheelLocation == WheelLocationEnum.rightRear)
		{
			if (Math.abs(force.value - vehicleForcesInRightRear.value) > 100.0)
			{
				roadwayForcesCalculationRightRear.apply();
				roadwaySurfaceRightRear.transmit(roadwayForcesOutRightRear);
			}
		}
	}

	@Override
	protected void createStateMachine()
	{
		this.stateMachine = Optional.of(new RoadwayStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		coefficientOfFriction = new RReal(0.025);
	}

	@Override
	protected void createFlows()
	{
		double vehicleWeightMagnitude = Vehicle.vehicleMass.value * Vehicle.kilogramsPerNewton;
		double vehicleWeightDirection = Math.toRadians(180);
		vehicleForcesInLeftFront = new ForceNewtons(vehicleWeightMagnitude, vehicleWeightDirection);
		vehicleForcesInRightFront = new ForceNewtons(vehicleWeightMagnitude, vehicleWeightDirection);
		vehicleForcesInLeftRear = new ForceNewtons(vehicleWeightMagnitude, vehicleWeightDirection);
		vehicleForcesInRightRear = new ForceNewtons(vehicleWeightMagnitude, vehicleWeightDirection);

		roadwayForcesOutLeftFront = new ForceNewtons(0);
		roadwayForcesOutRightFront = new ForceNewtons(0);
		roadwayForcesOutLeftRear = new ForceNewtons(0);
		roadwayForcesOutRightRear = new ForceNewtons(0);
	}

	@Override
	protected void createFullPorts()
	{
		wheelTireSurfaceLeftFront = new RoadwaySurfaceForceReceivePort(this, this, (long)WheelLocationEnum.leftFront.ordinal);
		wheelTireSurfaceRightFront = new RoadwaySurfaceForceReceivePort(this, this, (long)WheelLocationEnum.rightFront.ordinal);
		wheelTireSurfaceLeftRear = new RoadwaySurfaceForceReceivePort(this, this, (long)WheelLocationEnum.leftRear.ordinal);
		wheelTireSurfaceRightRear = new RoadwaySurfaceForceReceivePort(this, this, (long)WheelLocationEnum.rightRear.ordinal);

		roadwaySurfaceLeftFront = new RoadwaySurfaceForceTransmitPort(this, (long)WheelLocationEnum.leftFront.ordinal);
		roadwaySurfaceRightFront = new RoadwaySurfaceForceTransmitPort(this, (long)WheelLocationEnum.rightFront.ordinal);
		roadwaySurfaceLeftRear = new RoadwaySurfaceForceTransmitPort(this, (long)WheelLocationEnum.leftRear.ordinal);
		roadwaySurfaceRightRear = new RoadwaySurfaceForceTransmitPort(this, (long)WheelLocationEnum.rightRear.ordinal);
	}

	@Override
	protected void createConstraints()
	{
		roadwayForcesCalculationLeftFront = () ->
		{
			double weightForce = vehicleForcesInLeftFront.verticalComponent().value;
			ForceNewtons roadwayFriction = new ForceNewtons(weightForce * coefficientOfFriction.value, Math.toRadians(270));
			ForceNewtons roadwayLift = new ForceNewtons(weightForce, Math.toRadians(0));
			roadwayForcesOutLeftFront.setValue(ForceNewtons.sum(Arrays.asList(roadwayFriction, roadwayLift)));
		};
		roadwayForcesCalculationRightFront = () ->
		{
			double weightForce = vehicleForcesInRightFront.verticalComponent().value;
			ForceNewtons roadwayFriction = new ForceNewtons(weightForce * coefficientOfFriction.value, Math.toRadians(270));
			ForceNewtons roadwayLift = new ForceNewtons(weightForce, Math.toRadians(0));
			roadwayForcesOutRightFront.setValue(ForceNewtons.sum(Arrays.asList(roadwayFriction, roadwayLift)));
		};
		roadwayForcesCalculationLeftRear = () ->
		{
			double weightForce = vehicleForcesInLeftRear.verticalComponent().value;
			ForceNewtons roadwayFriction = new ForceNewtons(weightForce * coefficientOfFriction.value, Math.toRadians(270));
			ForceNewtons roadwayLift = new ForceNewtons(weightForce, Math.toRadians(0));
			roadwayForcesOutLeftRear.setValue(ForceNewtons.sum(Arrays.asList(roadwayFriction, roadwayLift)));
		};
		roadwayForcesCalculationRightRear = () ->
		{
			double weightForce = vehicleForcesInRightRear.verticalComponent().value;
			ForceNewtons roadwayFriction = new ForceNewtons(weightForce * coefficientOfFriction.value, Math.toRadians(270));
			ForceNewtons roadwayLift = new ForceNewtons(weightForce, Math.toRadians(0));
			roadwayForcesOutRightRear.setValue(ForceNewtons.sum(Arrays.asList(roadwayFriction, roadwayLift)));
		};
	}
}
