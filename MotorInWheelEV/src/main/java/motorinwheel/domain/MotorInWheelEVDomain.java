package motorinwheel.domain;

import java.util.List;
import java.util.Optional;
import motorinwheel.systems.atmosphere.Atmosphere;
import motorinwheel.systems.operator.Operator;
import motorinwheel.systems.roadway.Roadway;
import motorinwheel.systems.vehicle.Vehicle;
import sysmlinjava.analysis.interactionmessagetransmitter.InteractionMessageTransmitters;
import sysmlinjava.analysis.interactionsequence.InteractionMessageSequenceDisplay;
import sysmlinjava.analysis.interactionsequence.InteractionMessageTransmitter;
import sysmlinjava.annotations.AssociationConnector;
import sysmlinjava.annotations.AssociationConnectorFunction;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.parametrics.BindingConnector;
import sysmlinjava.annotations.parametrics.BindingConnectorFunction;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.connectors.SysMLAssociationBlockConnector;
import sysmlinjava.connectors.SysMLAssociationBlockConnectorFunction;
import sysmlinjava.connectors.SysMLBindingConnector;
import sysmlinjava.connectors.SysMLBindingConnectorFunction;

/**
 * {@code MotorInWheelEVDomain} is domain block of a SysMLinJava
 * model/simulation of an electric vehicle based on the electric motor-in-wheel
 * technology. The domain consists of:
 * <ul>
 * <li>the vehicle consists of a body, suspension, power supply, and wheels,
 * each of which consists of a tired wheel, electric motor, and brake</li>
 * <li>an operator who drives the vehicle by pushing against an accelerator or
 * decelerator pedal while monitoring its speed on a display (steering is not
 * part of the model)</li>
 * <li>a roadway upon which the vehicle's wheels place their weight and roll
 * with friction</li>
 * <li>the atmosphere through which the vehicle moves with air resistence</li>
 * </ul>
 * The vehicle's parts are connected to each other and to it's external
 * interfaces. The operator connects with the accelerator, decelerator, and
 * speed display. The wheels connect with the roadway, the vehicle suspension,
 * the braking system, and to the power supply. The body and suspension connect
 * with the atmosphere, the operator, and the wheels. The accelerator connects
 * with the power supply and the decelerator connects to the brake system. The
 * model is depicted as follows:<br>
 * <img src="doc-files/MotorInWheelEVDomainModel.png" alt="PNG file not
 * available" height="450"/><br>
 * The {@code MotorInWheelEVDomain} also includes a
 * {@code VehicleEnergyConstraintBlock}. This constraint block uses constraint
 * parameters of electrical power used and distance driven to calculate a
 * constraint parameter of energy use per distance driven. This constraint is
 * performed by producing a two-line chart that shows the energy and
 * energy-effieciency as a function of distance driven, as an example of how to
 * perform parametric analyses in SysMLinJava.
 * <p>
 * The {@code MotorInWheelEVDomain} is an executable model that executes in
 * accordance with the synchronous and asynchronous behaviors of an electric
 * vehicle. As such, the model uses state-machine-based behaviors for domain
 * parts such as the vehicle, operator, and roadway. Virtually all of the values
 * that are modeled for the domain parts are "flow" values with flow value types
 * of force and power being the most prevalent in the model.
 * 
 * @author ModelerOne
 *
 */
public class MotorInWheelEVDomain extends SysMLBlock
{
	/**
	 * Part for the vehicle operator
	 */
	@Part
	public Operator operator;
	/**
	 * Part for the vehicle, which includes parts of the vehicle such as the wheels,
	 * suspension, power supply, etc.
	 */
	@Part
	public Vehicle vehicle;
	/**
	 * Part for the roadway on which the vehicle rests and rolls
	 */
	@Part
	public Roadway roadway;
	/**
	 * Part for the atmosphere through which the vehicle moves
	 */
	@Part
	public Atmosphere atmosphere;

	/**
	 * Function to connect the operator to the vehicle
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction operatorToVehicleConnectorsFunction;
	/**
	 * Function to connect the roadway to the vehicle
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction roadwayToVehicleConnectorsFunction;
	/**
	 * Function to connect the atmosphere to the vehicle
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction atmosphereToVehicleConnectorsFunction;

	/**
	 * Connector that invokes the function to connect the operator to the vehicle
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector operatorToVehicleOperatorInterfaceConnectors;
	/**
	 * Connector that invokes the function to connect the roadway to the vehicle
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector roadwayToVehicleConnectors;
	/**
	 * Connector that invokes the function to connect the atmosphere to the vehicle
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector atmosphereToVehicleConnectors;

	/**
	 * Function to connect the operator actions counter to the constraint block
	 */
	@BindingConnectorFunction
	public SysMLBindingConnectorFunction operatorActionsCountConnectorFunctions;

	/**
	 * Binding connector that invokes the function to connect the operator actions
	 * counter to the constraint block
	 */
	@BindingConnector
	public SysMLBindingConnector operatorActionsCountConnectors;

	/**
	 * Constructor
	 */
	public MotorInWheelEVDomain()
	{
		super();
		enableInteractionMessageTransmissions(); // Enables use of COTS sequence diagram app.
	}

	@Override
	public void start()
	{
		atmosphere.start();
		roadway.start();
		vehicle.start();
		operator.start();
	}

	@Override
	public void stop()
	{
		operator.stop();
		vehicle.stop();
		roadway.stop();
		atmosphere.stop();
	}

	/**
	 * Enables the interaction message transmissions that produce a sequence diagram
	 */
	protected void enableInteractionMessageTransmissions()
	{
		InteractionMessageTransmitter transmitter = new InteractionMessageTransmitter(InteractionMessageSequenceDisplay.udpPort, true);
		InteractionMessageTransmitters interactionMessageTransmitters = new InteractionMessageTransmitters(transmitter);

		operator.leg.acceleratorPedal.messageUtility = Optional.of(interactionMessageTransmitters);
		operator.leg.brakePedal.messageUtility = Optional.of(interactionMessageTransmitters);

		vehicle.operatorDisplays.speedometer.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.accelerationSystem.electricalPowerControl.messageUtility = Optional.of(interactionMessageTransmitters);

		vehicle.decelerationSystem.brakeLeftFront.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.decelerationSystem.brakeRightFront.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.decelerationSystem.brakeLeftRear.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.decelerationSystem.brakeRightRear.messageUtility = Optional.of(interactionMessageTransmitters);

		vehicle.motorInWheelLeftFront.wheel.pulseTransmissionLine.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.motorInWheelRightFront.wheel.pulseTransmissionLine.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.motorInWheelLeftRear.wheel.pulseTransmissionLine.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.motorInWheelRightRear.wheel.pulseTransmissionLine.messageUtility = Optional.of(interactionMessageTransmitters);

		vehicle.motorInWheelLeftFront.wheel.tireSurface.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.motorInWheelRightFront.wheel.tireSurface.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.motorInWheelLeftRear.wheel.tireSurface.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.motorInWheelRightRear.wheel.tireSurface.messageUtility = Optional.of(interactionMessageTransmitters);

		vehicle.motorInWheelLeftFront.brake.wheelBrakeDisc.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.motorInWheelRightFront.brake.wheelBrakeDisc.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.motorInWheelLeftRear.brake.wheelBrakeDisc.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.motorInWheelRightRear.brake.wheelBrakeDisc.messageUtility = Optional.of(interactionMessageTransmitters);

		vehicle.motorInWheelLeftFront.motor.wheelMotorDisc.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.motorInWheelRightFront.motor.wheelMotorDisc.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.motorInWheelLeftRear.motor.wheelMotorDisc.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.motorInWheelRightRear.motor.wheelMotorDisc.messageUtility = Optional.of(interactionMessageTransmitters);

		vehicle.powerSupply.motorPowerLineLeftFront.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.powerSupply.motorPowerLineRightFront.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.powerSupply.motorPowerLineLeftRear.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.powerSupply.motorPowerLineRightRear.messageUtility = Optional.of(interactionMessageTransmitters);

		vehicle.suspensionChassisBody.wheelMountLeftFront.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.suspensionChassisBody.wheelMountRightFront.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.suspensionChassisBody.wheelMountLeftRear.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.suspensionChassisBody.wheelMountRightRear.messageUtility = Optional.of(interactionMessageTransmitters);

		vehicle.operatorDisplays.speedometer.messageUtility = Optional.of(interactionMessageTransmitters);
		vehicle.operatorDisplays.atmosphere.messageUtility = Optional.of(interactionMessageTransmitters);

		atmosphere.air.messageUtility = Optional.of(interactionMessageTransmitters);

		roadway.roadwaySurfaceLeftFront.messageUtility = Optional.of(interactionMessageTransmitters);
		roadway.roadwaySurfaceRightFront.messageUtility = Optional.of(interactionMessageTransmitters);
		roadway.roadwaySurfaceLeftRear.messageUtility = Optional.of(interactionMessageTransmitters);
		roadway.roadwaySurfaceRightRear.messageUtility = Optional.of(interactionMessageTransmitters);
	}

	@Override
	protected void createParts()
	{
		vehicle = new Vehicle(this);
		roadway = new Roadway(this);
		atmosphere = new Atmosphere(this, "Atmosphere", (long)0);
		operator = new Operator(this, "Operator", (long)0);
	}

	@Override
	protected void createConnectorFunctions()
	{
		operatorToVehicleConnectorsFunction = () ->
		{
			operator.leg.acceleratorPedal.addConnectedPortPeer(vehicle.accelerationSystem.acceleratorPedal);
			operator.leg.brakePedal.addConnectedPortPeer(vehicle.decelerationSystem.brakePedal);
			vehicle.operatorDisplays.speedometer.addConnectedPortPeer(operator.eyes.speedometerView);
		};
		roadwayToVehicleConnectorsFunction = () ->
		{
			roadway.roadwaySurfaceLeftFront.addConnectedPortPeer(vehicle.motorInWheelLeftFront.wheel.roadwaySurface);
			roadway.roadwaySurfaceRightFront.addConnectedPortPeer(vehicle.motorInWheelRightFront.wheel.roadwaySurface);
			roadway.roadwaySurfaceLeftRear.addConnectedPortPeer(vehicle.motorInWheelLeftRear.wheel.roadwaySurface);
			roadway.roadwaySurfaceRightRear.addConnectedPortPeer(vehicle.motorInWheelRightRear.wheel.roadwaySurface);

			vehicle.motorInWheelLeftFront.wheel.tireSurface.addConnectedPortPeer(roadway.wheelTireSurfaceLeftFront);
			vehicle.motorInWheelRightFront.wheel.tireSurface.addConnectedPortPeer(roadway.wheelTireSurfaceRightFront);
			vehicle.motorInWheelLeftRear.wheel.tireSurface.addConnectedPortPeer(roadway.wheelTireSurfaceLeftRear);
			vehicle.motorInWheelRightRear.wheel.tireSurface.addConnectedPortPeer(roadway.wheelTireSurfaceRightRear);
		};
		atmosphereToVehicleConnectorsFunction = () ->
		{
			atmosphere.air.addConnectedPortPeer(vehicle.suspensionChassisBody.airResistence);
			vehicle.operatorDisplays.atmosphere.addConnectedPortPeer(atmosphere.frontalArea);
		};
	}

	@Override
	protected void createConnectors()
	{
		operatorToVehicleOperatorInterfaceConnectors = new SysMLAssociationBlockConnector(operator, vehicle, operatorToVehicleConnectorsFunction);
		roadwayToVehicleConnectors = new SysMLAssociationBlockConnector(roadway, vehicle, roadwayToVehicleConnectorsFunction);
		atmosphereToVehicleConnectors = new SysMLAssociationBlockConnector(atmosphere, vehicle, atmosphereToVehicleConnectorsFunction);
	}

	@Override
	protected void createConstraintParameterConnectorFunctions()
	{
		operatorActionsCountConnectorFunctions = () ->
		{
			vehicle.vehicleEnergyConstraintBlock.operatorActionsCountPort.setParameterContextBlock(operator.leg);
			operator.leg.actionsCount.addValueChangeObserver(vehicle.vehicleEnergyConstraintBlock.operatorActionsCountPort);
			vehicle.vehicleEnergyConstraintBlock.operatorActionsCountPort.valueChanged();
		};
	}

	@Override
	protected void createConstraintParameterConnectors()
	{
		operatorActionsCountConnectors = new SysMLBindingConnector(List.of(operator.leg), vehicle.vehicleEnergyConstraintBlock, operatorActionsCountConnectorFunctions);
	}

	/**
	 * Main method for the process that constructs and executes the model. main()
	 * simply invokes the domain's constructor, starts the domain model, waits for
	 * it's completion, and stops it.
	 * 
	 * @param args null args
	 */
	public static void main(String[] args)
	{
		MotorInWheelEVDomain domain = new MotorInWheelEVDomain();
		domain.start();
		try
		{
			Thread.sleep(100 * 1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		domain.stop();
		System.exit(0);
	}
}