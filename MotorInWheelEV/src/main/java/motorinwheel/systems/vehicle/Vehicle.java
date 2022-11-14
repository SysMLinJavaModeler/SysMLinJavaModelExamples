package motorinwheel.systems.vehicle;

import java.util.Arrays;
import java.util.Optional;
import motorinwheel.components.acceleration.Accelerator;
import motorinwheel.components.deceleration.Decelerator;
import motorinwheel.components.elecpowersupply.ElectricPowerSupply;
import motorinwheel.components.operatordisplays.OperatorDisplays;
import motorinwheel.components.suspensionchassisbody.SuspensionChassisBody;
import motorinwheel.components.wheel.WheelLocationEnum;
import motorinwheel.constraintblocks.system.vehicle.VehicleEnergyConstraintBlock;
import motorinwheel.domain.MotorInWheelEVDomain;
import motorinwheel.systems.motorinwheel.MotorInWheelSystem;
import sysmlinjava.annotations.AssociationConnector;
import sysmlinjava.annotations.AssociationConnectorFunction;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.parametrics.BindingConnector;
import sysmlinjava.annotations.parametrics.BindingConnectorFunction;
import sysmlinjava.annotations.parametrics.ConstraintBlock;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.connectors.SysMLAssociationBlockConnector;
import sysmlinjava.connectors.SysMLAssociationBlockConnectorFunction;
import sysmlinjava.connectors.SysMLBindingConnector;
import sysmlinjava.connectors.SysMLBindingConnectorFunction;
import sysmlinjava.valuetypes.AreaMetersSquare;
import sysmlinjava.valuetypes.MassKilograms;
import sysmlinjava.valuetypes.RReal;

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
 *
 */
public class Vehicle extends SysMLBlock
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
	@Value
	public static MassKilograms vehicleMass;
	/**
	 * Value of the vehicle's frontal area (that "drags" through the atmosphere)
	 */
	@Value
	public static AreaMetersSquare frontalArea;
	/**
	 * Value of vehicle's drag coefficient (for an arbitrarily assumed vehicle body)
	 */
	@Value
	public static RReal dragCoefficient;

	/**
	 * Constraint block for the calculation of a graph of vehicle energy used per
	 * distance traveled
	 */
	@ConstraintBlock
	public VehicleEnergyConstraintBlock vehicleEnergyConstraintBlock;

	/**
	 * Functions that make the connections between the electic motors in the wheels
	 * and their suspension mount points
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction motorsToSuspensionChassisBodyLeftFrontConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction motorsToSuspensionChassisBodyRightFrontConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction motorsToSuspensionChassisBodyLeftRearConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction motorsToSuspensionChassisBodyRightRearConnectorFunction;
	/**
	 * Functions that make the connections between the brake in the wheels and their
	 * suspension mount points
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction brakesToSuspensionChassisBodyLeftFrontConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction brakesToSuspensionChassisBodyRightFrontConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction brakesToSuspensionChassisBodyLeftRearConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction brakesToSuspensionChassisBodyRightRearConnectorFunction;
	/**
	 * Functions that make the connections between the suspension and the wheels on
	 * which it is mounted
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction suspensionChassisBodysToWheelsLeftFrontConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction suspensionChassisBodysToWheelsRightFrontConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction suspensionChassisBodysToWheelsLeftRearConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction suspensionChassisBodysToWheelsRightRearConnectorFunction;
	/**
	 * Functions that make the connections between the wheels electronic pulse
	 * generators and the speedometer
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction wheelsToOperatorDisplayLeftFrontConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction wheelsToOperatorDisplayRightFrontConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction wheelsToOperatorDisplayLeftRearConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction wheelsToOperatorDisplayRightRearConnectorFunction;
	/**
	 * Function that makes the connections between the accelerator and the power
	 * supply
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction accelerationSystemToPowerSupplyConnectorFunction;
	/**
	 * Functions that make the connections between the power supply and the electic
	 * motors
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction powerSupplyToMotorsLeftFrontConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction powerSupplyToMotorsRightFrontConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction powerSupplyToMotorsLeftRearConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction powerSupplyToMotorsRightRearConnectorFunction;
	/**
	 * Functions that make the connections between the decelerator (brake pedal) and
	 * the brakes in the wheels
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction decelerationSystemToBrakesLeftFrontConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction decelerationSystemToBrakesRightFrontConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction decelerationSystemToBrakesLeftRearConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction decelerationSystemToBrakesRightRearConnectorFunction;

	/**
	 * Connectors that invoke the functions that make the connections between the
	 * electic motors in the wheels and their suspension mount points
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector motorsToSuspensionChassisBodysLeftFrontConnector;
	@AssociationConnector
	public SysMLAssociationBlockConnector motorsToSuspensionChassisBodysRightFrontConnector;
	@AssociationConnector
	public SysMLAssociationBlockConnector motorsToSuspensionChassisBodysLeftRearConnector;
	@AssociationConnector
	public SysMLAssociationBlockConnector motorsToSuspensionChassisBodysRightRearConnector;
	/**
	 * Connectors that invoke the functions that make the connections between the
	 * brake in the wheels and their suspension mount points
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector brakesToSuspensionChassisBodysLeftFrontConnector;
	@AssociationConnector
	public SysMLAssociationBlockConnector brakesToSuspensionChassisBodysRightFrontConnector;
	@AssociationConnector
	public SysMLAssociationBlockConnector brakesToSuspensionChassisBodysLeftRearConnector;
	@AssociationConnector
	public SysMLAssociationBlockConnector brakesToSuspensionChassisBodysRightRearConnector;
	/**
	 * Connectors that invoke the functions that make the connections between the
	 * suspension and the wheels on which it is mounted
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector suspensionChassisBodysToWheelsLeftFrontConnector;
	@AssociationConnector
	public SysMLAssociationBlockConnector suspensionChassisBodysToWheelsRightFrontConnector;
	@AssociationConnector
	public SysMLAssociationBlockConnector suspensionChassisBodysToWheelsLeftRearConnector;
	@AssociationConnector
	public SysMLAssociationBlockConnector suspensionChassisBodysToWheelsRightRearConnector;
	/**
	 * Connectors that invoke the functions that make the connections between the
	 * wheels electronic pulse generators and the speedometer
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector wheelsToOperatorDisplayLeftFrontConnector;
	@AssociationConnector
	public SysMLAssociationBlockConnector wheelsToOperatorDisplayRightFrontConnector;
	@AssociationConnector
	public SysMLAssociationBlockConnector wheelsToOperatorDisplayLeftRearConnector;
	@AssociationConnector
	public SysMLAssociationBlockConnector wheelsToOperatorDisplayRightRearConnector;
	/**
	 * Connectors that invoke the functions that make the connections between the
	 * power supply and the electic motors
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector powerGeneratorToMotorsLeftFrontConnector;
	@AssociationConnector
	public SysMLAssociationBlockConnector powerGeneratorToMotorsRightFrontConnector;
	@AssociationConnector
	public SysMLAssociationBlockConnector powerGeneratorToMotorsLeftRearConnector;
	@AssociationConnector
	public SysMLAssociationBlockConnector powerGeneratorToMotorsRightRearConnector;
	/**
	 * Connectors that invoke the functions that make the connections between the
	 * decelerator (brake pedal) and the brakes in the wheels
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector decelerationSystemToBrakesLeftFrontConnector;
	@AssociationConnector
	public SysMLAssociationBlockConnector decelerationSystemToBrakesRightFrontConnector;
	@AssociationConnector
	public SysMLAssociationBlockConnector decelerationSystemToBrakesLeftRearConnector;
	@AssociationConnector
	public SysMLAssociationBlockConnector decelerationSystemToBrakesRightRearConnector;
	/**
	 * Connector that invokes the function that makes the connections between the
	 * accelerator and the power supply
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector accelerationSystemToPowerGeneratorConnector;

	/**
	 * Function that binds the wheel energy and speed values to their corresponding
	 * constraint parameters
	 */
	@BindingConnectorFunction
	public SysMLBindingConnectorFunction wheelEnergyAndSpeedParameterConnectorFunction;

	/**
	 * Binding connectors that invoke the function to bind the wheel energy and
	 * speed values to their corresponding constraint parameters
	 */
	@BindingConnector
	public SysMLBindingConnector wheelEnergyAndSpeedParameterConnectors;

	public static final double kilogramsPerNewton = 0.101971621;

	/**
	 * Constructor
	 * 
	 * @param domain motor-in-wheel domain of which the vehicle is a part
	 */
	public Vehicle(MotorInWheelEVDomain domain)
	{
		super(domain, "Vehicle", 0L);
	}

	/**
	 * Operation to transmit motor-in-wheel weights to the vehicle (other weights
	 * statically added)
	 */
	@Operation
	public void transmitWeights()
	{
		logger.info("transmitting weights");
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
		vehicleEnergyConstraintBlock.start();
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
		vehicleEnergyConstraintBlock.stop();
	}

	@Override
	protected void createValues()
	{
		dragCoefficient = new RReal(0.35);
		frontalArea = new AreaMetersSquare(2.0);
		vehicleMass = new MassKilograms(1300);
	}

	@Override
	protected void createParts()
	{
		operatorDisplays = new OperatorDisplays(this, "OperatorDisplay", (long)0);
		accelerationSystem = new Accelerator(this, "AccelerationSystem", (long)0);
		decelerationSystem = new Decelerator(this, "DecelerationSystem", (long)0);
		powerSupply = new ElectricPowerSupply(this, "ElectricPowerGenerator", (long)0);
		suspensionChassisBody = new SuspensionChassisBody(this, "SuspensionChassisBody", (long)0);
		motorInWheelLeftFront = new MotorInWheelSystem(this, "MotorInWheelLeftFront", WheelLocationEnum.leftFront);
		motorInWheelRightFront = new MotorInWheelSystem(this, "MotorInWheelRightFront", WheelLocationEnum.rightFront);
		motorInWheelLeftRear = new MotorInWheelSystem(this, "MotorInWheelLeftRear", WheelLocationEnum.leftRear);
		motorInWheelRightRear = new MotorInWheelSystem(this, "MotorInWheelRightRear", WheelLocationEnum.rightRear);
	}

	@Override
	protected void createConstraintBlocks()
	{
		vehicleEnergyConstraintBlock = new VehicleEnergyConstraintBlock(Optional.empty());
	}

	@Override
	protected void createConnectorFunctions()
	{
		motorsToSuspensionChassisBodyLeftFrontConnectorFunction = () ->	motorInWheelLeftFront.motor.suspensionMount.addConnectedPortPeer(suspensionChassisBody.motorMountLeftFront);
		motorsToSuspensionChassisBodyRightFrontConnectorFunction = () -> motorInWheelRightFront.motor.suspensionMount.addConnectedPortPeer(suspensionChassisBody.motorMountRightFront);
		motorsToSuspensionChassisBodyLeftRearConnectorFunction = () -> motorInWheelLeftRear.motor.suspensionMount.addConnectedPortPeer(suspensionChassisBody.motorMountLeftRear);
		motorsToSuspensionChassisBodyRightRearConnectorFunction = () -> motorInWheelRightRear.motor.suspensionMount.addConnectedPortPeer(suspensionChassisBody.motorMountRightRear);

		brakesToSuspensionChassisBodyLeftFrontConnectorFunction = () ->	motorInWheelLeftFront.brake.suspensionMount.addConnectedPortPeer(suspensionChassisBody.brakeMountLeftFront);
		brakesToSuspensionChassisBodyRightFrontConnectorFunction = () -> motorInWheelRightFront.brake.suspensionMount.addConnectedPortPeer(suspensionChassisBody.brakeMountRightFront);
		brakesToSuspensionChassisBodyLeftRearConnectorFunction = () -> motorInWheelLeftRear.brake.suspensionMount.addConnectedPortPeer(suspensionChassisBody.brakeMountLeftRear);
		brakesToSuspensionChassisBodyRightRearConnectorFunction = () -> motorInWheelRightRear.brake.suspensionMount.addConnectedPortPeer(suspensionChassisBody.brakeMountRightRear);

		suspensionChassisBodysToWheelsLeftFrontConnectorFunction = () -> suspensionChassisBody.wheelMountLeftFront.addConnectedPortPeer(motorInWheelLeftFront.wheel.suspensionMount);
		suspensionChassisBodysToWheelsRightFrontConnectorFunction = () -> suspensionChassisBody.wheelMountRightFront.addConnectedPortPeer(motorInWheelRightFront.wheel.suspensionMount);
		suspensionChassisBodysToWheelsLeftRearConnectorFunction = () -> suspensionChassisBody.wheelMountLeftRear.addConnectedPortPeer(motorInWheelLeftRear.wheel.suspensionMount);
		suspensionChassisBodysToWheelsRightRearConnectorFunction = () -> suspensionChassisBody.wheelMountRightRear.addConnectedPortPeer(motorInWheelRightRear.wheel.suspensionMount);

		wheelsToOperatorDisplayLeftFrontConnectorFunction = () ->motorInWheelLeftFront.wheel.pulseTransmissionLine.addConnectedPortPeer(operatorDisplays.wheelPulseReceptionLineLeftFront);
		wheelsToOperatorDisplayRightFrontConnectorFunction = () ->motorInWheelRightFront.wheel.pulseTransmissionLine.addConnectedPortPeer(operatorDisplays.wheelPulseReceptionLineRightFront);
		wheelsToOperatorDisplayLeftRearConnectorFunction = () ->motorInWheelLeftRear.wheel.pulseTransmissionLine.addConnectedPortPeer(operatorDisplays.wheelPulseReceptionLineLeftRear);
		wheelsToOperatorDisplayRightRearConnectorFunction = () ->motorInWheelRightRear.wheel.pulseTransmissionLine.addConnectedPortPeer(operatorDisplays.wheelPulseReceptionLineRightRear);

		accelerationSystemToPowerSupplyConnectorFunction = () -> accelerationSystem.electricalPowerControl.addConnectedPortPeer(powerSupply.control);

		powerSupplyToMotorsLeftFrontConnectorFunction = () ->powerSupply.motorPowerLineLeftFront.addConnectedPortPeer(motorInWheelLeftFront.motor.electricalLine);
		powerSupplyToMotorsRightFrontConnectorFunction = () ->powerSupply.motorPowerLineRightFront.addConnectedPortPeer(motorInWheelRightFront.motor.electricalLine);
		powerSupplyToMotorsLeftRearConnectorFunction = () ->powerSupply.motorPowerLineLeftRear.addConnectedPortPeer(motorInWheelLeftRear.motor.electricalLine);
		powerSupplyToMotorsRightRearConnectorFunction = () ->powerSupply.motorPowerLineRightRear.addConnectedPortPeer(motorInWheelRightRear.motor.electricalLine);

		decelerationSystemToBrakesLeftFrontConnectorFunction = () ->decelerationSystem.brakeLeftFront.addConnectedPortPeer(motorInWheelLeftFront.brake.hydraulicLine);
		decelerationSystemToBrakesRightFrontConnectorFunction = () ->decelerationSystem.brakeRightFront.addConnectedPortPeer(motorInWheelRightFront.brake.hydraulicLine);
		decelerationSystemToBrakesLeftRearConnectorFunction = () ->decelerationSystem.brakeLeftRear.addConnectedPortPeer(motorInWheelLeftRear.brake.hydraulicLine);
		decelerationSystemToBrakesRightRearConnectorFunction = () ->decelerationSystem.brakeRightRear.addConnectedPortPeer(motorInWheelRightRear.brake.hydraulicLine);
	}

	@Override
	protected void createConnectors()
	{
		motorsToSuspensionChassisBodysLeftFrontConnector = new SysMLAssociationBlockConnector(motorInWheelLeftFront.motor, suspensionChassisBody, motorsToSuspensionChassisBodyLeftFrontConnectorFunction);
		brakesToSuspensionChassisBodysLeftFrontConnector = new SysMLAssociationBlockConnector(motorInWheelLeftFront.brake, suspensionChassisBody, brakesToSuspensionChassisBodyLeftFrontConnectorFunction);
		suspensionChassisBodysToWheelsLeftFrontConnector = new SysMLAssociationBlockConnector(suspensionChassisBody, motorInWheelLeftFront.wheel, suspensionChassisBodysToWheelsLeftFrontConnectorFunction);
		wheelsToOperatorDisplayLeftFrontConnector = new SysMLAssociationBlockConnector(motorInWheelLeftFront.wheel, operatorDisplays, wheelsToOperatorDisplayLeftFrontConnectorFunction);
		powerGeneratorToMotorsLeftFrontConnector = new SysMLAssociationBlockConnector(powerSupply, motorInWheelLeftFront.motor, powerSupplyToMotorsLeftFrontConnectorFunction);
		decelerationSystemToBrakesLeftFrontConnector = new SysMLAssociationBlockConnector(decelerationSystem, motorInWheelLeftFront.brake, decelerationSystemToBrakesLeftFrontConnectorFunction);

		motorsToSuspensionChassisBodysRightFrontConnector = new SysMLAssociationBlockConnector(motorInWheelRightFront.motor, suspensionChassisBody, motorsToSuspensionChassisBodyRightFrontConnectorFunction);
		brakesToSuspensionChassisBodysRightFrontConnector = new SysMLAssociationBlockConnector(motorInWheelRightFront.brake, suspensionChassisBody, brakesToSuspensionChassisBodyRightFrontConnectorFunction);
		suspensionChassisBodysToWheelsRightFrontConnector = new SysMLAssociationBlockConnector(suspensionChassisBody, motorInWheelRightFront.wheel, suspensionChassisBodysToWheelsRightFrontConnectorFunction);
		wheelsToOperatorDisplayRightFrontConnector = new SysMLAssociationBlockConnector(motorInWheelRightFront.wheel, operatorDisplays, wheelsToOperatorDisplayRightFrontConnectorFunction);
		powerGeneratorToMotorsRightFrontConnector = new SysMLAssociationBlockConnector(powerSupply, motorInWheelRightFront.motor, powerSupplyToMotorsRightFrontConnectorFunction);
		decelerationSystemToBrakesRightFrontConnector = new SysMLAssociationBlockConnector(decelerationSystem, motorInWheelRightFront.brake, decelerationSystemToBrakesRightFrontConnectorFunction);

		accelerationSystemToPowerGeneratorConnector = new SysMLAssociationBlockConnector(accelerationSystem, powerSupply, accelerationSystemToPowerSupplyConnectorFunction);

		motorsToSuspensionChassisBodysLeftRearConnector = new SysMLAssociationBlockConnector(motorInWheelLeftRear.motor, suspensionChassisBody, motorsToSuspensionChassisBodyLeftRearConnectorFunction);
		brakesToSuspensionChassisBodysLeftRearConnector = new SysMLAssociationBlockConnector(motorInWheelLeftRear.brake, suspensionChassisBody, brakesToSuspensionChassisBodyLeftRearConnectorFunction);
		suspensionChassisBodysToWheelsLeftRearConnector = new SysMLAssociationBlockConnector(suspensionChassisBody, motorInWheelLeftRear.wheel, suspensionChassisBodysToWheelsLeftRearConnectorFunction);
		wheelsToOperatorDisplayLeftRearConnector = new SysMLAssociationBlockConnector(motorInWheelLeftRear.wheel, operatorDisplays, wheelsToOperatorDisplayLeftRearConnectorFunction);
		powerGeneratorToMotorsLeftRearConnector = new SysMLAssociationBlockConnector(powerSupply, motorInWheelLeftRear.motor, powerSupplyToMotorsLeftRearConnectorFunction);
		decelerationSystemToBrakesLeftRearConnector = new SysMLAssociationBlockConnector(decelerationSystem, motorInWheelLeftRear.brake, decelerationSystemToBrakesLeftRearConnectorFunction);

		motorsToSuspensionChassisBodysRightRearConnector = new SysMLAssociationBlockConnector(motorInWheelRightRear.motor, suspensionChassisBody, motorsToSuspensionChassisBodyRightRearConnectorFunction);
		brakesToSuspensionChassisBodysRightRearConnector = new SysMLAssociationBlockConnector(motorInWheelRightRear.brake, suspensionChassisBody, brakesToSuspensionChassisBodyRightRearConnectorFunction);
		suspensionChassisBodysToWheelsRightRearConnector = new SysMLAssociationBlockConnector(suspensionChassisBody, motorInWheelRightRear.wheel, suspensionChassisBodysToWheelsRightRearConnectorFunction);
		wheelsToOperatorDisplayRightRearConnector = new SysMLAssociationBlockConnector(motorInWheelRightRear.wheel, operatorDisplays, wheelsToOperatorDisplayRightRearConnectorFunction);
		powerGeneratorToMotorsRightRearConnector = new SysMLAssociationBlockConnector(powerSupply, motorInWheelRightRear.motor, powerSupplyToMotorsRightRearConnectorFunction);
		decelerationSystemToBrakesRightRearConnector = new SysMLAssociationBlockConnector(decelerationSystem, motorInWheelRightRear.brake, decelerationSystemToBrakesRightRearConnectorFunction);
	}

	@Override
	protected void createConstraintParameterConnectorFunctions()
	{
		wheelEnergyAndSpeedParameterConnectorFunction = () ->
		{
			vehicleEnergyConstraintBlock.vehicleSpeedPort.parameterContextBlock = operatorDisplays;
			operatorDisplays.speedViewOut.addValueChangeObserver(vehicleEnergyConstraintBlock.vehicleSpeedPort);
			vehicleEnergyConstraintBlock.vehicleSpeedPort.valueChanged();

			vehicleEnergyConstraintBlock.wheelEnergyInLeftFront.wheelPowerPort.parameterContextBlock = motorInWheelLeftFront.motor;
			motorInWheelLeftFront.motor.electricalPowerIn.addValueChangeObserver(vehicleEnergyConstraintBlock.wheelEnergyInLeftFront.wheelPowerPort);
			vehicleEnergyConstraintBlock.wheelEnergyInLeftFront.wheelPowerPort.valueChanged();

			vehicleEnergyConstraintBlock.wheelEnergyInRightFront.wheelPowerPort.parameterContextBlock = motorInWheelRightFront.motor;
			motorInWheelRightFront.motor.electricalPowerIn.addValueChangeObserver(vehicleEnergyConstraintBlock.wheelEnergyInRightFront.wheelPowerPort);
			vehicleEnergyConstraintBlock.wheelEnergyInRightFront.wheelPowerPort.valueChanged();

			vehicleEnergyConstraintBlock.wheelEnergyInLeftRear.wheelPowerPort.parameterContextBlock = motorInWheelLeftRear.motor;
			motorInWheelLeftRear.motor.electricalPowerIn.addValueChangeObserver(vehicleEnergyConstraintBlock.wheelEnergyInLeftRear.wheelPowerPort);
			vehicleEnergyConstraintBlock.wheelEnergyInLeftRear.wheelPowerPort.valueChanged();

			vehicleEnergyConstraintBlock.wheelEnergyInRightRear.wheelPowerPort.parameterContextBlock = motorInWheelRightRear.motor;
			motorInWheelRightRear.motor.electricalPowerIn.addValueChangeObserver(vehicleEnergyConstraintBlock.wheelEnergyInRightRear.wheelPowerPort);
			vehicleEnergyConstraintBlock.wheelEnergyInRightRear.wheelPowerPort.valueChanged();
		};
	}

	@Override
	protected void createConstraintParameterConnectors()
	{
		wheelEnergyAndSpeedParameterConnectors = new SysMLBindingConnector(Arrays.asList(this), vehicleEnergyConstraintBlock, wheelEnergyAndSpeedParameterConnectorFunction);
	}
}
