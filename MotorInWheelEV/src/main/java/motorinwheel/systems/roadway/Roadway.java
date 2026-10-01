package motorinwheel.systems.roadway;

import java.util.Arrays;
import java.util.Optional;

import motorinwheel.common.ports.energy.RoadwaySurfaceForceReceivePort;
import motorinwheel.common.ports.energy.RoadwaySurfaceForceTransmitPort;
import motorinwheel.components.wheel.WheelLocationEnum;
import motorinwheel.domain.MotorInWheelEVDomain;
import motorinwheel.systems.vehicle.Vehicle;
import sysmlinjava.attributetypes.ForceNewtons;
import sysmlinjava.attributetypes.RReal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.actions.Calculation;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;

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
public class Roadway extends SysMLPart
{
	/**
	 * Port for the receipt of a wheel's forces (weight and torque) on the roadway
	 */
	@Port
	public RoadwaySurfaceForceReceivePort wheelTireSurfaceLeftFront;
	/**
	 * Port for the receipt of a wheel's forces (weight and torque) on the roadway
	 */
	@Port
	public RoadwaySurfaceForceReceivePort wheelTireSurfaceRightFront;
	/**
	 * Port for the receipt of a wheel's forces (weight and torque) on the roadway
	 */
	@Port
	public RoadwaySurfaceForceReceivePort wheelTireSurfaceLeftRear;
	/**
	 * Port for the receipt of a wheel's forces (weight and torque) on the roadway
	 */
	@Port
	public RoadwaySurfaceForceReceivePort wheelTireSurfaceRightRear;
	/**
	 * Port for the transmission of roadway forces (lift and friction) to a wheel
	 */
	@Port
	public RoadwaySurfaceForceTransmitPort roadwaySurfaceLeftFront;
	/**
	 * Port for the transmission of roadway forces (lift and friction) to a wheel
	 */
	@Port
	public RoadwaySurfaceForceTransmitPort roadwaySurfaceRightFront;
	/**
	 * Port for the transmission of roadway forces (lift and friction) to a wheel
	 */
	@Port
	public RoadwaySurfaceForceTransmitPort roadwaySurfaceLeftRear;
	/**
	 * Port for the transmission of roadway forces (lift and friction) to a wheel
	 */
	@Port
	public RoadwaySurfaceForceTransmitPort roadwaySurfaceRightRear;

	/**
	 * Attribute for one wheel's forces on the roadway
	 */
	@Attribute
	public ForceNewtons vehicleForcesInLeftFront;
	/**
	 * Attribute for one wheel's forces on the roadway
	 */
	@Attribute
	public ForceNewtons vehicleForcesInRightFront;
	/**
	 * Attribute for one wheel's forces on the roadway
	 */
	@Attribute
	public ForceNewtons vehicleForcesInLeftRear;
	/**
	 * Attribute for one wheel's forces on the roadway
	 */
	@Attribute
	public ForceNewtons vehicleForcesInRightRear;
	/**
	 * Attribute for the roadway's forces on a wheel
	 */
	@Attribute
	public ForceNewtons roadwayForcesOutLeftFront;
	/**
	 * Attribute for the roadway's forces on a wheel
	 */
	@Attribute
	public ForceNewtons roadwayForcesOutRightFront;
	/**
	 * Attribute for the roadway's forces on a wheel
	 */
	@Attribute
	public ForceNewtons roadwayForcesOutLeftRear;
	/**
	 * Attribute for the roadway's forces on a wheel
	 */
	@Attribute
	public ForceNewtons roadwayForcesOutRightRear;

	/**
	 * Value of the coefficient of friction between wheel and roadway
	 */
	@Attribute
	public RReal coefficientOfFriction;

//	/**
//	 * Calculation of net forces on the roadway for the left-front wheel
//	 */
//	@Calculation
//	public SysMLCalculation roadwayForcesCalculationLeftFront;
//	/**
//	 * Calculation of net forces on the roadway for the right-front wheel
//	 */
//	@Calculation
//	public SysMLCalculation roadwayForcesCalculationRightFront;
//	/**
//	 * Calculation of net forces on the roadway for the left-rear wheel
//	 */
//	@Calculation
//	public SysMLCalculation roadwayForcesCalculationLeftRear;
//	/**
//	 * Calculation of net forces on the roadway for the right-rear wheel
//	 */
//	@Calculation
//	public SysMLCalculation roadwayForcesCalculationRightRear;

	/**
	 * Constructor; initializes the roadway forces based on initial flow values
	 * 
	 * @param domain the domain in which the roadway exists
	 */
	public Roadway(MotorInWheelEVDomain domain)
	{
		super(Optional.of(domain), "Roadway", 0L);
		this.calculateRoadwayForcesOutLeftFront();
		this.calculateRoadwayForcesOutRightFront();
		this.calculateRoadwayForcesOutLeftRear();
		this.calculateRoadwayForcesOutRightRear();
//		((SysMLCalculationFunction)roadwayForcesCalculationLeftFront.function).perform();
//		((SysMLCalculationFunction)roadwayForcesCalculationRightFront.function).perform();
//		((SysMLCalculationFunction)roadwayForcesCalculationLeftRear.function).perform();
//		((SysMLCalculationFunction)roadwayForcesCalculationRightRear.function).perform();
	}

	/**
	 * Reception that reacts to receipt of new forces on a specified wheel
	 * 
	 * @param force         new forces value
	 * @param wheelLocation specified wheel
	 */
	@Action
	public void onVehicleForces(ForceNewtons force, WheelLocationEnum wheelLocation)
	{
		if (wheelLocation == WheelLocationEnum.leftFront)
		{
			if (Math.abs(force.value - vehicleForcesInLeftFront.value) > 100.0)
			{
				calculateRoadwayForcesOutLeftFront();
				roadwaySurfaceLeftFront.transmit(roadwayForcesOutLeftFront);
			}
		}
		else if (wheelLocation == WheelLocationEnum.rightFront)
		{
			if (Math.abs(force.value - vehicleForcesInRightFront.value) > 100.0)
			{
				calculateRoadwayForcesOutRightFront();
				roadwaySurfaceRightFront.transmit(roadwayForcesOutRightFront);
			}
		}
		else if (wheelLocation == WheelLocationEnum.leftRear)
		{
			if (Math.abs(force.value - vehicleForcesInLeftRear.value) > 100.0)
			{
				calculateRoadwayForcesOutLeftRear();
				roadwaySurfaceLeftRear.transmit(roadwayForcesOutLeftRear);
			}
		}
		else if (wheelLocation == WheelLocationEnum.rightRear)
		{
			if (Math.abs(force.value - vehicleForcesInRightRear.value) > 100.0)
			{
				calculateRoadwayForcesOutRightRear();
				roadwaySurfaceRightRear.transmit(roadwayForcesOutRightRear);
			}
		}
	}

	@Calculation
	private void calculateRoadwayForcesOutLeftFront()
	{
		double weightForce = vehicleForcesInLeftFront.verticalComponent().value;
		ForceNewtons roadwayFriction = new ForceNewtons(weightForce * coefficientOfFriction.value, Math.toRadians(270));
		ForceNewtons roadwayLift = new ForceNewtons(weightForce, Math.toRadians(0));
		roadwayForcesOutLeftFront.setValue(ForceNewtons.sum(Arrays.asList(roadwayFriction, roadwayLift)));
	}

	@Calculation
	private void calculateRoadwayForcesOutRightFront()
	{
		double weightForce = vehicleForcesInRightFront.verticalComponent().value;
		ForceNewtons roadwayFriction = new ForceNewtons(weightForce * coefficientOfFriction.value, Math.toRadians(270));
		ForceNewtons roadwayLift = new ForceNewtons(weightForce, Math.toRadians(0));
		roadwayForcesOutRightFront.setValue(ForceNewtons.sum(Arrays.asList(roadwayFriction, roadwayLift)));
	}

	@Calculation
	private void calculateRoadwayForcesOutLeftRear()
	{
		double weightForce = vehicleForcesInLeftRear.verticalComponent().value;
		ForceNewtons roadwayFriction = new ForceNewtons(weightForce * coefficientOfFriction.value, Math.toRadians(270));
		ForceNewtons roadwayLift = new ForceNewtons(weightForce, Math.toRadians(0));
		roadwayForcesOutLeftRear.setValue(ForceNewtons.sum(Arrays.asList(roadwayFriction, roadwayLift)));
	}

	@Calculation
	private void calculateRoadwayForcesOutRightRear()
	{
		double weightForce = vehicleForcesInRightRear.verticalComponent().value;
		ForceNewtons roadwayFriction = new ForceNewtons(weightForce * coefficientOfFriction.value, Math.toRadians(270));
		ForceNewtons roadwayLift = new ForceNewtons(weightForce, Math.toRadians(0));
		roadwayForcesOutRightRear.setValue(ForceNewtons.sum(Arrays.asList(roadwayFriction, roadwayLift)));
	}

	@Override
	protected void createStateMachine()
	{
		this.stateMachine = Optional.of(new RoadwayStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		coefficientOfFriction = new RReal(0.025);

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
	protected void createPorts()
	{
		wheelTireSurfaceLeftFront = new RoadwaySurfaceForceReceivePort(this, this, (long)WheelLocationEnum.leftFront.ordinal());
		wheelTireSurfaceRightFront = new RoadwaySurfaceForceReceivePort(this, this, (long)WheelLocationEnum.rightFront.ordinal());
		wheelTireSurfaceLeftRear = new RoadwaySurfaceForceReceivePort(this, this, (long)WheelLocationEnum.leftRear.ordinal());
		wheelTireSurfaceRightRear = new RoadwaySurfaceForceReceivePort(this, this, (long)WheelLocationEnum.rightRear.ordinal());

		roadwaySurfaceLeftFront = new RoadwaySurfaceForceTransmitPort(this, (long)WheelLocationEnum.leftFront.ordinal());
		roadwaySurfaceRightFront = new RoadwaySurfaceForceTransmitPort(this, (long)WheelLocationEnum.rightFront.ordinal());
		roadwaySurfaceLeftRear = new RoadwaySurfaceForceTransmitPort(this, (long)WheelLocationEnum.leftRear.ordinal());
		roadwaySurfaceRightRear = new RoadwaySurfaceForceTransmitPort(this, (long)WheelLocationEnum.rightRear.ordinal());
	}
}
