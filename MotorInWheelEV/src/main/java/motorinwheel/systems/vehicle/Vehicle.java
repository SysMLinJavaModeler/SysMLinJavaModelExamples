package motorinwheel.systems.vehicle;

import java.util.Optional;

import motorinwheel.components.acceleration.Accelerator;
import motorinwheel.components.deceleration.Decelerator;
import motorinwheel.components.elecpowersupply.ElectricPowerSupply;
import motorinwheel.components.operatordisplays.OperatorDisplays;
import motorinwheel.components.suspensionchassisbody.SuspensionChassisBody;
import motorinwheel.components.wheel.WheelLocationEnum;
import motorinwheel.domain.MotorInWheelEVDomain;
import motorinwheel.systems.motorinwheel.MotorInWheelSystem;
import sysmlinjava.attributetypes.AreaMetersSquare;
import sysmlinjava.attributetypes.MassKilograms;
import sysmlinjava.attributetypes.RReal;
import sysmlinjava.connectors.SysMLFlowConnector;
import sysmlinjava.connectors.SysMLFlowConnector.TypesEnum;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.connectors.FlowConnector;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.parts.SysMLPart;

/**
 * The Motor-In-Wheel electric vehicle. The vehicle consists of model parts:
 * <ul>
 * <li>Operator displays that provide the operator a monitor of the vehicl's
 * speed</li>
 * <li>Accelerator that provides the operator a control for vehicle
 * acceleration</li>
 * <li>Decelerator that provides the operator a control for vehicle
 * deceleration</li>
 * <li>Electric power supply that provides power to the electric motors in the
 * wheels</li>
 * <li>Suspension/chassis/body that provides support and structure for the
 * vehicle parts</li>
 * <li>Motor-in-wheel systems that provide the forces to support and
 * accelerate/decelerate the vehicle</li>
 * </ul>
 * The vehicle also interfaces (provides ports) with the vehicle operator, the
 * roadway, and with the atmosphere through which it moves.
 * 
 * @author ModelerOne
 */
public class Vehicle extends SysMLPart
{
	/**
	 * Part for the operator displays, i.e. the speedometer
	 */
	@Part
	public OperatorDisplays operatorDisplays;
	/**
	 * Part for the acceleration system
	 */
	@Part
	public Accelerator accelerationSystem;
	/**
	 * Part for the deceleration (braking) system
	 */
	@Part
	public Decelerator decelerationSystem;
	/**
	 * Part for the electrical power supply
	 */
	@Part
	public ElectricPowerSupply powerSupply;
	/**
	 * Part for the suspension/chassis/body
	 */
	@Part
	public SuspensionChassisBody suspensionChassisBody;
	/**
	 * Part for one of the four motor-in-wheel systems
	 */
	@Part
	public MotorInWheelSystem motorInWheelLeftFront;
	/**
	 * Part for one of the four motor-in-wheel systems
	 */
	@Part
	public MotorInWheelSystem motorInWheelRightFront;
	/**
	 * Part for one of the four motor-in-wheel systems
	 */
	@Part
	public MotorInWheelSystem motorInWheelLeftRear;
	/**
	 * Part for one of the four motor-in-wheel systems
	 */
	@Part
	public MotorInWheelSystem motorInWheelRightRear;

	/**
	 * Value of the vehicle's mass
	 */
	@Attribute
	public static MassKilograms vehicleMass;
	/**
	 * Value of the vehicle's frontal area (that "drags" through the atmosphere)
	 */
	@Attribute
	public static AreaMetersSquare frontalArea;
	/**
	 * Value of vehicle's drag coefficient (for an arbitrarily assumed vehicle body)
	 */
	@Attribute
	public static RReal dragCoefficient;

	/**
	 * Connectors between the electic motors in the wheels and their suspension
	 * mount points
	 */
	@FlowConnector
	public SysMLFlowConnector motorsToSuspensionChassisBodysLeftFrontConnector;
	@FlowConnector
	public SysMLFlowConnector motorsToSuspensionChassisBodysRightFrontConnector;
	@FlowConnector
	public SysMLFlowConnector motorsToSuspensionChassisBodysLeftRearConnector;
	@FlowConnector
	public SysMLFlowConnector motorsToSuspensionChassisBodysRightRearConnector;
	/**
	 * Connectors between the brake in the wheels and their suspension mount points
	 */
	@FlowConnector
	public SysMLFlowConnector brakesToSuspensionChassisBodysLeftFrontConnector;
	@FlowConnector
	public SysMLFlowConnector brakesToSuspensionChassisBodysRightFrontConnector;
	@FlowConnector
	public SysMLFlowConnector brakesToSuspensionChassisBodysLeftRearConnector;
	@FlowConnector
	public SysMLFlowConnector brakesToSuspensionChassisBodysRightRearConnector;
	/**
	 * Connectors between the suspension and the wheels on which it is mounted
	 */
	@FlowConnector
	public SysMLFlowConnector suspensionChassisBodysToWheelsLeftFrontConnector;
	@FlowConnector
	public SysMLFlowConnector suspensionChassisBodysToWheelsRightFrontConnector;
	@FlowConnector
	public SysMLFlowConnector suspensionChassisBodysToWheelsLeftRearConnector;
	@FlowConnector
	public SysMLFlowConnector suspensionChassisBodysToWheelsRightRearConnector;
	/**
	 * Connectors between the wheels electronic pulse generators and the speedometer
	 */
	@FlowConnector
	public SysMLFlowConnector wheelsToOperatorDisplayLeftFrontConnector;
	@FlowConnector
	public SysMLFlowConnector wheelsToOperatorDisplayRightFrontConnector;
	@FlowConnector
	public SysMLFlowConnector wheelsToOperatorDisplayLeftRearConnector;
	@FlowConnector
	public SysMLFlowConnector wheelsToOperatorDisplayRightRearConnector;
	/**
	 * Connectors between the power supply and the electic motors
	 */
	@FlowConnector
	public SysMLFlowConnector powerGeneratorToMotorsLeftFrontConnector;
	@FlowConnector
	public SysMLFlowConnector powerGeneratorToMotorsRightFrontConnector;
	@FlowConnector
	public SysMLFlowConnector powerGeneratorToMotorsLeftRearConnector;
	@FlowConnector
	public SysMLFlowConnector powerGeneratorToMotorsRightRearConnector;
	/**
	 * Connectors between the decelerator (brake pedal) and the brakes in the wheels
	 */
	@FlowConnector
	public SysMLFlowConnector decelerationSystemToBrakesLeftFrontConnector;
	@FlowConnector
	public SysMLFlowConnector decelerationSystemToBrakesRightFrontConnector;
	@FlowConnector
	public SysMLFlowConnector decelerationSystemToBrakesLeftRearConnector;
	@FlowConnector
	public SysMLFlowConnector decelerationSystemToBrakesRightRearConnector;
	/**
	 * Connector that invokes the function that makes the connections between the
	 * accelerator and the power supply
	 */
	@FlowConnector
	public SysMLFlowConnector accelerationSystemToPowerGeneratorConnector;

	public static final double kilogramsPerNewton = 0.101971621;

	/**
	 * Constructor
	 * 
	 * @param domain motor-in-wheel domain of which the vehicle is a part
	 */
	public Vehicle(MotorInWheelEVDomain domain)
	{
		super(Optional.of(domain), "Vehicle", 0L);
	}

	/**
	 * Operation to transmit motor-in-wheel weights to the vehicle (other weights
	 * statically added)
	 */
	@Action
	public void transmitWeights()
	{
		motorInWheelLeftFront.brake.transmitWeight();
		motorInWheelRightFront.brake.transmitWeight();
		motorInWheelLeftRear.brake.transmitWeight();
		motorInWheelRightRear.brake.transmitWeight();
		motorInWheelLeftFront.motor.transmitWeight();
		motorInWheelRightFront.motor.transmitWeight();
		motorInWheelLeftRear.motor.transmitWeight();
		motorInWheelRightRear.motor.transmitWeight();
	}

	@Override
	public void start()
	{
		operatorDisplays.start();
		accelerationSystem.start();
		decelerationSystem.start();
		powerSupply.start();
		suspensionChassisBody.start();
		motorInWheelLeftFront.start();
		motorInWheelRightFront.start();
		motorInWheelLeftRear.start();
		motorInWheelRightRear.start();
	}

	@Override
	public void stop()
	{
		operatorDisplays.stop();
		accelerationSystem.start();
		decelerationSystem.start();
		powerSupply.stop();
		suspensionChassisBody.stop();
		motorInWheelLeftFront.stop();
		motorInWheelRightFront.stop();
		motorInWheelLeftRear.stop();
		motorInWheelRightRear.stop();
	}

	@Override
	protected void createAttributes()
	{
		dragCoefficient = new RReal(0.35);
		frontalArea = new AreaMetersSquare(2.0);
		vehicleMass = new MassKilograms(1300);
	}

	@Override
	protected void createParts()
	{
		operatorDisplays = new OperatorDisplays(this, "OperatorDisplay", (long) 0);
		accelerationSystem = new Accelerator(this, "AccelerationSystem", (long) 0);
		decelerationSystem = new Decelerator(this, "DecelerationSystem", (long) 0);
		powerSupply = new ElectricPowerSupply(this, "ElectricPowerGenerator", (long) 0);
		suspensionChassisBody = new SuspensionChassisBody(this, "SuspensionChassisBody", (long) 0);
		motorInWheelLeftFront = new MotorInWheelSystem(this, "MotorInWheelLeftFront", WheelLocationEnum.leftFront);
		motorInWheelRightFront = new MotorInWheelSystem(this, "MotorInWheelRightFront", WheelLocationEnum.rightFront);
		motorInWheelLeftRear = new MotorInWheelSystem(this, "MotorInWheelLeftRear", WheelLocationEnum.leftRear);
		motorInWheelRightRear = new MotorInWheelSystem(this, "MotorInWheelRightRear", WheelLocationEnum.rightRear);
	}

	@Override
	protected void createFlowConnectors()
	{
		motorsToSuspensionChassisBodysLeftFrontConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, motorInWheelLeftFront.motor.suspensionMount,
		suspensionChassisBody.motorMountLeftFront, "", 0L);
		motorsToSuspensionChassisBodysRightFrontConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, motorInWheelRightFront.motor.suspensionMount,
		suspensionChassisBody.motorMountRightFront, "", 0L);
		motorsToSuspensionChassisBodysLeftRearConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, motorInWheelLeftRear.motor.suspensionMount,
		suspensionChassisBody.motorMountLeftRear, "", 0L);
		motorsToSuspensionChassisBodysRightRearConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, motorInWheelRightRear.motor.suspensionMount,
		suspensionChassisBody.motorMountRightRear, "", 0L);

		brakesToSuspensionChassisBodysLeftFrontConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, motorInWheelLeftFront.brake.suspensionMount,
		suspensionChassisBody.brakeMountLeftFront, "", 0L);
		brakesToSuspensionChassisBodysRightFrontConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, motorInWheelRightFront.brake.suspensionMount,
		suspensionChassisBody.brakeMountRightFront, "", 0L);
		brakesToSuspensionChassisBodysLeftRearConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, motorInWheelLeftRear.brake.suspensionMount,
		suspensionChassisBody.brakeMountLeftRear, "", 0L);
		brakesToSuspensionChassisBodysRightRearConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, motorInWheelRightRear.brake.suspensionMount,
		suspensionChassisBody.brakeMountRightRear, "", 0L);

		suspensionChassisBodysToWheelsLeftFrontConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, suspensionChassisBody.wheelMountLeftFront,
		motorInWheelLeftFront.wheel.suspensionMount, "", 0L);
		suspensionChassisBodysToWheelsRightFrontConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, suspensionChassisBody.wheelMountRightFront,
		motorInWheelRightFront.wheel.suspensionMount, "", 0L);
		suspensionChassisBodysToWheelsLeftRearConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, suspensionChassisBody.wheelMountLeftRear,
		motorInWheelLeftRear.wheel.suspensionMount, "", 0L);
		suspensionChassisBodysToWheelsRightRearConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, suspensionChassisBody.wheelMountRightRear,
		motorInWheelRightRear.wheel.suspensionMount, "", 0L);

		wheelsToOperatorDisplayLeftFrontConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, motorInWheelLeftFront.wheel.pulseTransmissionLine,
		operatorDisplays.wheelPulseReceptionLineLeftFront, "", 0L);
		wheelsToOperatorDisplayRightFrontConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, motorInWheelRightFront.wheel.pulseTransmissionLine,
		operatorDisplays.wheelPulseReceptionLineRightFront, "", 0L);
		wheelsToOperatorDisplayLeftRearConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, motorInWheelLeftRear.wheel.pulseTransmissionLine,
		operatorDisplays.wheelPulseReceptionLineLeftRear, "", 0L);
		wheelsToOperatorDisplayRightRearConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, motorInWheelRightRear.wheel.pulseTransmissionLine,
		operatorDisplays.wheelPulseReceptionLineRightRear, "", 0L);

		accelerationSystemToPowerGeneratorConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, accelerationSystem.electricalPowerControl, powerSupply.control, "", 0L);

		powerGeneratorToMotorsLeftFrontConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, powerSupply.motorPowerLineLeftFront,
		motorInWheelLeftFront.motor.electricalLine, "", 0L);
		powerGeneratorToMotorsRightFrontConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, powerSupply.motorPowerLineRightFront,
		motorInWheelRightFront.motor.electricalLine, "", 0L);
		powerGeneratorToMotorsLeftRearConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, powerSupply.motorPowerLineLeftRear, motorInWheelLeftRear.motor.electricalLine,
		"", 0L);
		powerGeneratorToMotorsRightRearConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, powerSupply.motorPowerLineRightRear,
		motorInWheelRightRear.motor.electricalLine, "", 0L);

		decelerationSystemToBrakesLeftFrontConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, decelerationSystem.brakeLeftFront,
		motorInWheelLeftFront.brake.hydraulicLine, "", 0L);
		decelerationSystemToBrakesRightFrontConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, decelerationSystem.brakeRightFront,
		motorInWheelRightFront.brake.hydraulicLine, "", 0L);
		decelerationSystemToBrakesLeftRearConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, decelerationSystem.brakeLeftRear, motorInWheelLeftRear.brake.hydraulicLine,
		"", 0L);
		decelerationSystemToBrakesRightRearConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, decelerationSystem.brakeRightRear,
		motorInWheelRightRear.brake.hydraulicLine, "", 0L);
	}
}
