package motorinwheel.domain;

import static motorinwheel.analysis.VehicleEnergyAnalysisCase.energyLeftFrontParamName;
import static motorinwheel.analysis.VehicleEnergyAnalysisCase.energyLeftRearParamName;
import static motorinwheel.analysis.VehicleEnergyAnalysisCase.energyRightFrontParamName;
import static motorinwheel.analysis.VehicleEnergyAnalysisCase.energyRightRearParamName;
import static motorinwheel.analysis.VehicleEnergyAnalysisCase.operatorActionsCountParamName;
import static motorinwheel.analysis.VehicleEnergyAnalysisCase.powerLeftFrontParamName;
import static motorinwheel.analysis.VehicleEnergyAnalysisCase.powerLeftRearParamName;
import static motorinwheel.analysis.VehicleEnergyAnalysisCase.powerRightFrontParamName;
import static motorinwheel.analysis.VehicleEnergyAnalysisCase.powerRightRearParamName;
import static motorinwheel.analysis.VehicleEnergyAnalysisCase.vehicleSpeedParamName;

import java.util.List;
import java.util.Optional;

import motorinwheel.analysis.VehicleEnergyAnalysisCase;
import motorinwheel.systems.atmosphere.Atmosphere;
import motorinwheel.systems.operator.Operator;
import motorinwheel.systems.roadway.Roadway;
import motorinwheel.systems.vehicle.Vehicle;
import sysmlinjava.connectors.SysMLBindingConnector;
import sysmlinjava.connectors.SysMLFlowConnector;
import sysmlinjava.connectors.SysMLFlowConnector.TypesEnum;
import sysmlinjava.javaannotations.analysis.parametrics.ParametricAnalysis;
import sysmlinjava.javaannotations.connectors.BindingConnector;
import sysmlinjava.javaannotations.connectors.FlowConnector;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.views.interactionssequencediagram.InteractionMessageSequenceDisplay;
import sysmlinjava.views.interactionssequencediagram.InteractionMessageTransmitter;
import sysmlinjava.views.interactionssequencediagram.InteractionMessageTransmitters;

/**
 * {@code MotorInWheelEVDomain} is domain part of a SysMLinJava model/simulation
 * of an electric vehicle based on the electric motor-in-wheel technology. The
 * domain consists of:
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
 * {@code VehicleEnergyConstraintBlock}. This constraint part uses constraint
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
 */
public class MotorInWheelEVDomain extends SysMLPart
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
	 * Parametric analysis case for the calculation of a graph of vehicle energy
	 * used per distance traveled
	 */
	@ParametricAnalysis
	public VehicleEnergyAnalysisCase vehicleEnergyAnalysis;

	/**
	 * Connector between the operator and vehicle's accelerator
	 */
	@FlowConnector
	private SysMLFlowConnector operatorToAccelerator;
	/**
	 * Connector between the operator and vehicle's brake pedal
	 */
	@FlowConnector
	private SysMLFlowConnector operatorToBrake;
	/**
	 * Connector between the speedometer and the operator
	 */
	@FlowConnector
	private SysMLFlowConnector speedometerToOperator;
	/**
	 * Connector between the roadway and the vehicle's wheels
	 */
	@FlowConnector
	private SysMLFlowConnector roadwayToWheels;
	/**
	 * Connector between the vehicle's wheels and the roadway
	 */
	@FlowConnector
	private SysMLFlowConnector wheelsToRoadway;
	/**
	 * Connector between the atmosphere the vehicle
	 */
	@FlowConnector
	private SysMLFlowConnector atmosphereToVehicle;

	/**
	 * Connector that binds the operator actions count to the analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector operatorActionsCountBindingConnector;
	/**
	 * Connector that binds the vehicle speed to the analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector vehicleSpeedBindingConnector;

	/**
	 * Connector that binds the energy used by the left front wheel to the vehicle
	 * analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector energyLeftFrontBindingConnector;
	/**
	 * Connector that binds the energy used by the right front wheel to the vehicle
	 * analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector energyRightFrontBindingConnector;
	/**
	 * Connector that binds the energy used by the left rear wheel to the vehicle
	 * analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector energyLeftRearBindingConnector;
	/**
	 * Connector that binds the energy used by the right rear wheel to the vehicle
	 * analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector energyRightRearBindingConnector;
	@BindingConnector
	SysMLBindingConnector motorPowerLeftFrontBindingConnector;
	@BindingConnector
	SysMLBindingConnector motorPowerRightFrontBindingConnector;
	@BindingConnector
	SysMLBindingConnector motorPowerLeftRearBindingConnector;
	@BindingConnector
	SysMLBindingConnector motorPowerRightRearBindingConnector;

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
		vehicleEnergyAnalysis.start();
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
		vehicleEnergyAnalysis.stop();
	}

	/**
	 * Enables the interaction message transmissions that produce a sequence diagram
	 */
	@Override
	protected void enableInteractionMessageTransmissions()
	{
		InteractionMessageTransmitter transmitter = new InteractionMessageTransmitter(InteractionMessageSequenceDisplay.udpPort, true);
		InteractionMessageTransmitters interactionMessageTransmitters = new InteractionMessageTransmitters(transmitter, false);

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
		atmosphere = new Atmosphere(this, "Atmosphere", 0);
		operator = new Operator(this, "Operator", 0);
	}

	@Override
	protected void createAnalysisCases()
	{
		vehicleEnergyAnalysis = new VehicleEnergyAnalysisCase();
	}

	@Override
	protected void createFlowConnectors()
	{
		operatorToAccelerator = new SysMLFlowConnector(TypesEnum.peertopeer, false, operator.leg.acceleratorPedal, vehicle.accelerationSystem.acceleratorPedal, "", 0L);
		operatorToBrake = new SysMLFlowConnector(TypesEnum.peertopeer, false, operator.leg.brakePedal, vehicle.decelerationSystem.brakePedal, "", 0L);
		speedometerToOperator = new SysMLFlowConnector(TypesEnum.peertopeer, false, vehicle.operatorDisplays.speedometer, operator.eyes.speedometerView, "", 0L);

		roadwayToWheels = new SysMLFlowConnector(TypesEnum.peertopeer, false,
		List.of(roadway.roadwaySurfaceLeftFront, roadway.roadwaySurfaceRightFront, roadway.roadwaySurfaceLeftRear, roadway.roadwaySurfaceRightRear),
		List.of(vehicle.motorInWheelLeftFront.wheel.roadwaySurface, vehicle.motorInWheelRightFront.wheel.roadwaySurface, vehicle.motorInWheelLeftRear.wheel.roadwaySurface, vehicle.motorInWheelRightRear.wheel.roadwaySurface),
		"", 0L);

		wheelsToRoadway = new SysMLFlowConnector(TypesEnum.peertopeer, false,
		List.of(vehicle.motorInWheelLeftFront.wheel.tireSurface, vehicle.motorInWheelRightFront.wheel.tireSurface, vehicle.motorInWheelLeftRear.wheel.tireSurface, vehicle.motorInWheelRightRear.wheel.tireSurface),
		List.of(roadway.wheelTireSurfaceLeftFront, roadway.wheelTireSurfaceRightFront, roadway.wheelTireSurfaceLeftRear, roadway.wheelTireSurfaceRightRear), "", 0L);

		atmosphereToVehicle = new SysMLFlowConnector(TypesEnum.peertopeer, false, List.of(atmosphere.air, vehicle.suspensionChassisBody.airResistence),
		List.of(vehicle.operatorDisplays.atmosphere, atmosphere.frontalArea), "", 0L);
	}

	@Override
	protected void createBindingConnectors()
	{
		operatorActionsCountBindingConnector = new SysMLBindingConnector(operator.leg.actionsCount, vehicleEnergyAnalysis, operatorActionsCountParamName);
		vehicleEnergyAnalysis.paramConnectors.put(operatorActionsCountParamName, operatorActionsCountBindingConnector);

		motorPowerLeftFrontBindingConnector = new SysMLBindingConnector(vehicle.motorInWheelLeftFront.motor.electricalPowerIn, vehicleEnergyAnalysis.wheelEnergyInLeftFront,
		powerLeftFrontParamName);
		vehicleEnergyAnalysis.wheelEnergyInLeftFront.paramConnectors.put(powerLeftFrontParamName, motorPowerLeftFrontBindingConnector);

		motorPowerRightFrontBindingConnector = new SysMLBindingConnector(vehicle.motorInWheelRightFront.motor.electricalPowerIn, vehicleEnergyAnalysis.wheelEnergyInRightFront,
		powerRightFrontParamName);
		vehicleEnergyAnalysis.wheelEnergyInRightFront.paramConnectors.put(powerRightFrontParamName, motorPowerRightFrontBindingConnector);

		motorPowerLeftRearBindingConnector = new SysMLBindingConnector(vehicle.motorInWheelLeftRear.motor.electricalPowerIn, vehicleEnergyAnalysis.wheelEnergyInLeftRear,
		powerLeftRearParamName);
		vehicleEnergyAnalysis.wheelEnergyInLeftRear.paramConnectors.put(powerLeftRearParamName, motorPowerLeftRearBindingConnector);

		motorPowerRightRearBindingConnector = new SysMLBindingConnector(vehicle.motorInWheelRightRear.motor.electricalPowerIn, vehicleEnergyAnalysis.wheelEnergyInRightRear,
		powerRightRearParamName);
		vehicleEnergyAnalysis.wheelEnergyInRightRear.paramConnectors.put(powerRightRearParamName, motorPowerRightRearBindingConnector);

		energyLeftFrontBindingConnector = new SysMLBindingConnector(vehicleEnergyAnalysis.wheelEnergyInLeftFront.energyKilowattHours, vehicleEnergyAnalysis,
		energyLeftFrontParamName);
		vehicleEnergyAnalysis.paramConnectors.put(energyLeftFrontParamName, energyLeftFrontBindingConnector);

		energyRightFrontBindingConnector = new SysMLBindingConnector(vehicleEnergyAnalysis.wheelEnergyInRightFront.energyKilowattHours, vehicleEnergyAnalysis,
		energyRightFrontParamName);
		vehicleEnergyAnalysis.paramConnectors.put(energyRightFrontParamName, energyRightFrontBindingConnector);

		energyLeftRearBindingConnector = new SysMLBindingConnector(vehicleEnergyAnalysis.wheelEnergyInLeftRear.energyKilowattHours, vehicleEnergyAnalysis, energyLeftRearParamName);
		vehicleEnergyAnalysis.paramConnectors.put(energyLeftRearParamName, energyLeftRearBindingConnector);

		energyRightRearBindingConnector = new SysMLBindingConnector(vehicleEnergyAnalysis.wheelEnergyInRightRear.energyKilowattHours, vehicleEnergyAnalysis,
		energyRightRearParamName);
		vehicleEnergyAnalysis.paramConnectors.put(energyRightRearParamName, energyRightRearBindingConnector);

		vehicleSpeedBindingConnector = new SysMLBindingConnector(vehicle.operatorDisplays.speedViewOut, vehicleEnergyAnalysis, vehicleSpeedParamName);
		vehicleEnergyAnalysis.paramConnectors.put(vehicleSpeedParamName, vehicleSpeedBindingConnector);
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
			Thread.sleep(4800 * 1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		domain.stop();
		System.exit(0);
	}
}