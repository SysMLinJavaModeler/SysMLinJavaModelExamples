package c4s2.domain;

import static c4s2.parametrics.C4S2ExecutionAnalysisCase.uidStrikePosition;
import static c4s2.parametrics.C4S2ExecutionAnalysisCase.uidStrikeVelocity;
import static c4s2.parametrics.C4S2ExecutionAnalysisCase.uidTargetPosition;
import static c4s2.parametrics.C4S2ExecutionAnalysisCase.uidTargetState;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import c4s2.parametrics.C4S2ExecutionAnalysisCase;
import c4s2.platforms.C4Platform;
import c4s2.systems.c4s2.C4S2System;
import c4s2.systems.radar.RadarSystem;
import c4s2.systems.strike.StrikeSystem;
import c4s2.systems.target.VehicleArmoredLargeTarget;
import c4s2.users.C4S2Operator;
import sysmlinjava.connectors.SysMLBindingConnector;
import sysmlinjava.connectors.SysMLFlowConnector;
import sysmlinjava.connectors.SysMLFlowConnector.TypesEnum;
import sysmlinjava.javaannotations.analysis.parametrics.ParametricAnalysis;
import sysmlinjava.javaannotations.connectors.BindingConnector;
import sysmlinjava.javaannotations.connectors.FlowConnector;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.views.animatedareadisplay.AnimatedAreaDisplay;
import sysmlinjava.views.interactionssequencediagram.InteractionMessageSequenceDisplay;
import sysmlinjava.views.interactionssequencediagram.InteractionMessageTransmitter;
import sysmlinjava.views.interactionssequencediagram.InteractionMessageTransmitters;
import sysmlinjava.views.statetransitionstables.StateTransitionTablesTransmitter;
import sysmlinjava.views.statetransitionstables.StateTransitionsDisplay;
import sysmlinjava.views.statetransitionstables.StateTransitionsTransmitters;

/**
 * The {@code C4S2Domain} is a SysMLinJava model of a Command/Control
 * Surveillance/Strike (C2S2) domain. It is a collection of systems that
 * performs the military standard process to find, fix, track, target, engage,
 * and assess (F2T2EA) specific types of targets within a specific area of
 * surveillance. The entire system-of-systems is automated and while very simple
 * in its capabilities, it serves as a good example of how SysMLinJava can be
 * used to accurately model and simulate a complex domain with high precision
 * and with relatively little effort.
 * <p>
 * The {@code C4S2Domain} model consists of a radar system to find,fix, track,
 * target and assess a target vehicle, a strike system to engage (destroy) the
 * target vehicle, and a C4S2 system to provide the operator with the displays
 * and services needed to monitor and control the systems as they perform the
 * entire F2T2EA operation.
 * <p>
 * The radar in the {@code C4S2Domain} is located at a fixed site and performs
 * scanning on a fixed radial section about a kilometer away. The target vehicle
 * is a large armored vehicle that travels at a slow speed across the scanning
 * area. The target vehicle reflects the radar signal back to the radar with a
 * unique signature that indicates it is the target of interest. When the
 * signature reflection is received, the operator directs an attached strike
 * vehicle, e.g. an armed drone, to engage/strike the target. After the strike
 * vehicle performs the strike, the operator redirects the radar system to
 * assess the target vehicle. The target vehicle now returns a reflection
 * signature that indicates it is destroyed, thereby completing the F2T2EA
 * operation. The operator then proceeds to detach the radar and strike systems
 * from the operation and shuts down the C4S2 system, thereby ending the
 * simulation.
 * <p>
 * The {@code C4S2Operator} behaves in accordance with the standard F2T2EA
 * process. It is modeled as a F2T2EA state machine and, in this respect, could
 * be a human or automated operator. While this model/simulation of the F2T2EA
 * process focuses on only the most basic (simple) operator tasks of the
 * process, it serves to demonstrate how the SysMLinJava model can achieve
 * arbitrarily high levels of precision in complex models and simulations.
 * <p>
 * The domain simulation is executed via execution of the {@code C4S2Domain}'s
 * {@code main()} method below. All construction of the domain's parts, their
 * connections, and execution of their individual behaviorss are performed via
 * this main() method.
 * 
 * @author ModelerOne
 */
public class C4S2Domain extends SysMLPart
{
	/**
	 * The human or machine that operates the C4S2 system and its attached radar and
	 * strike systems to F2T2EA the target.
	 */
	@Part
	public C4S2Operator operator;
	/**
	 * The system that provides the operator with the displays and services needed
	 * to perform its F2T2EA tasks.
	 */
	@Part
	public C4S2System c4s2System;
	/**
	 * The radar system that the operator uses to find, fix, track, target (F2T2)
	 * and asses (A) the target.
	 */
	@Part
	public RadarSystem radarSystem;
	/**
	 * The strike system, e.g. an armed drone, that the operator uses to engage (E)
	 * the target.
	 */
	@Part
	public StrikeSystem strikeSystem;
	/**
	 * The target (a vehicle) of the F2T2EA process.
	 */
	@Part
	public VehicleArmoredLargeTarget vehicleTarget;
	/**
	 * The platform which provides space, weight, power, and cooling support for the
	 * C2S2 System, e.g. a command/control vehicle or a hovering aircraft.
	 */
	@Part
	public C4Platform platform;

	/**
	 * Analysis (parametric) for execution of the model
	 */
	@ParametricAnalysis
	public C4S2ExecutionAnalysisCase c4s2ExecutionAnalysisCase;

	/**
	 * boundContextFunction for the vehicle target position
	 */
	@BindingConnector
	public SysMLBindingConnector vehicleTargetPositionBindingConnector;
	/**
	 * Binding connector for the strike system position
	 */
	@BindingConnector
	public SysMLBindingConnector strikeSystemPositionBindingConnector;
	/**
	 * Binding connector for the vehicle target status
	 */
	@BindingConnector
	public SysMLBindingConnector vehicleTargetStatusBindingConnector;
	/**
	 * Binding connector for the strike system velocity
	 */
	@BindingConnector
	public SysMLBindingConnector strikeSystemVelocityBindingConnector;

	/**
	 * Connectors between the system components for purposes of messaging between them.
	 */
	@FlowConnector
	private SysMLFlowConnector messaging;
	/**
	 * Connector between the strike system and the target vehicle
	 */
	@FlowConnector
	private SysMLFlowConnector strikeSystemToVehicleTarget;
	/**
	 * Connector between the operator and the C4S2 system, to include the operator
	 * displays and services
	 */
	@FlowConnector
	private SysMLFlowConnector operatorToC4S2System;
	/**
	 * Connector between the radar system and the target vehicle
	 */
	@FlowConnector
	private SysMLFlowConnector radarToVehicle;
	/**
	 * Connector between the C4S2 services computer and the C4 platform rack
	 */
	@FlowConnector
	private SysMLFlowConnector c4s2ServicesToPlatformRackMounts;
	/**
	 * Connector between the operator services computer and the C4 platform rack
	 */
	@FlowConnector
	private SysMLFlowConnector operatorServicesToPlatformRackMounts;
	/**
	 * Connector between the ethernet switch/IP router and the C4 platform rack
	 */
	@FlowConnector
	private SysMLFlowConnector switchRouterToPlatformRackMounts;
	/**
	 * Connector between the SIPRNET router and the C4 platform rack
	 */
	@FlowConnector
	private SysMLFlowConnector siprnetRouterToPlatformRackMounts;
	/**
	 * Connector between the platform power source and the C4S2 system equipment
	 */
	@FlowConnector
	private SysMLFlowConnector platformPowerToEquipment;
	/**
	 * Connector between the C4S2 system equipment and the platform's heat sink
	 */
	@FlowConnector
	private SysMLFlowConnector equipmentHeatToPlatform;

	/**
	 * Constructor
	 */
	public C4S2Domain()
	{
		super();
		enableInteractionMessageTransmissions();
		enableStateTransitionTransmissions();
	}

	/**
	 * Starts the behavior of the domain by starting all of its "parts", i.e. the
	 * target vehicle, radar, strike, platform, c4S2 system, and the operator.
	 */
	@Override
	public void start()
	{
		logger.info("starting domain...");
		vehicleTarget.start();
		radarSystem.start();
		strikeSystem.start();
		platform.start();
		c4s2System.start();
		operator.start();
		c4s2ExecutionAnalysisCase.start();
	}

	/**
	 * Waits on each system's and the analysis case's execution to terminate, i.e.
	 * for the execution threads to run to completion. Max wait is 30 minutes -
	 * which is way more than expected.
	 */
	public void awaitCompletion()
	{
		try
		{
			vehicleTarget.concurrentExecutionThreads.awaitTermination(30, TimeUnit.MINUTES);
			strikeSystem.concurrentExecutionThreads.awaitTermination(30, TimeUnit.MINUTES);
			radarSystem.concurrentExecutionThreads.awaitTermination(30, TimeUnit.MINUTES);
			c4s2System.awaitTermination();
			operator.concurrentExecutionThreads.awaitTermination(30, TimeUnit.MINUTES);
			platform.concurrentExecutionThreads.awaitTermination(30, TimeUnit.MINUTES);
			c4s2ExecutionAnalysisCase.concurrentExecutionThreads.awaitTermination(30, TimeUnit.MINUTES);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Stops the behavior of the domain by stopping all of its "parts", i.e. the
	 * target vehicle, radar, strike, platform, c4S2 system, and the operator.
	 */
	@Override
	public void stop()
	{
		logger.info("stopping domain...");
		platform.stop();
		operator.stop();
		c4s2System.stop();
		strikeSystem.stop();
		radarSystem.stop();
		vehicleTarget.stop();
		logger.info("domain stopped, stopping execution display constraint block...");
		c4s2ExecutionAnalysisCase.stop();
		logger.info("execution display constraint block stopped");
	}

	/**
	 * Enable the transmission from the simulation to an interaction sequence
	 * (sequence diagram) display of the interaction messages that occur during the
	 * simulation. This operation enables a grapical display of the real-time
	 * sequence diagram of the interactions between the specified parts of the model
	 * throughout the simulation, to include the powering up/initializing of the
	 * systems, the radar scanning and target reflections, the strike vehicle
	 * engaging (shooting at) the target, and the radar assessment of target.
	 */
	@Override
	protected void enableInteractionMessageTransmissions()
	{
		InteractionMessageTransmitter transmitter = new InteractionMessageTransmitter(InteractionMessageSequenceDisplay.udpPort, false);
		InteractionMessageTransmitters interactionMessageTransmitters = new InteractionMessageTransmitters(transmitter, false);

		operator.controlViewPort.messageUtility = Optional.of(interactionMessageTransmitters);
		operator.onOffSwitchC4S2ServicesComputer.messageUtility = Optional.of(interactionMessageTransmitters);
		operator.onOffSwitchEthernetSwitchIPRouter.messageUtility = Optional.of(interactionMessageTransmitters);
		operator.onOffSwitchOperatorServicesComputer.messageUtility = Optional.of(interactionMessageTransmitters);
		operator.onOffSwitchSIPRNetRouter.messageUtility = Optional.of(interactionMessageTransmitters);

		platform.powerC4S2ServicesComputer.messageUtility = Optional.of(interactionMessageTransmitters);
		platform.powerEthernetSwitchIPRouter.messageUtility = Optional.of(interactionMessageTransmitters);
		platform.powerOperatorServicesComputer.messageUtility = Optional.of(interactionMessageTransmitters);
		platform.powerSIPRNetRouter.messageUtility = Optional.of(interactionMessageTransmitters);

		c4s2System.c4s2ServicesComputer.electricalPower.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.c4s2ServicesComputer.convectiveHeat.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.c4s2ServicesComputer.rackMount.mountLeftFront.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.c4s2ServicesComputer.rackMount.mountRightFront.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.c4s2ServicesComputer.rackMount.mountLeftRear.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.c4s2ServicesComputer.rackMount.mountRightRear.messageUtility = Optional.of(interactionMessageTransmitters);

		c4s2System.c4s2ServicesComputer.systemServices.snmpC4S2ServicesComputer.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.c4s2ServicesComputer.systemServices.snmpOperatorServicesComputer.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.c4s2ServicesComputer.systemServices.snmpSwitchRouter.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.c4s2ServicesComputer.systemServices.snmpSIPRNetRouter.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.c4s2ServicesComputer.snmpAgent.messageUtility = Optional.of(interactionMessageTransmitters);

		c4s2System.operatorServicesComputer.electricalPower.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.operatorServicesComputer.convectiveHeat.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.operatorServicesComputer.rackMount.mountLeftFront.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.operatorServicesComputer.rackMount.mountRightFront.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.operatorServicesComputer.rackMount.mountLeftRear.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.operatorServicesComputer.rackMount.mountRightRear.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.operatorServicesComputer.snmpAgent.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.operatorServicesComputer.c4s2OperatorServices.monitorView.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.operatorServicesComputer.snmpAgent.messageUtility = Optional.of(interactionMessageTransmitters);

		c4s2System.siprnetRouter.electricalPower.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.siprnetRouter.convectiveHeat.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.siprnetRouter.rackMount.mountLeftFront.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.siprnetRouter.rackMount.mountRightFront.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.siprnetRouter.rackMount.mountLeftRear.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.siprnetRouter.rackMount.mountRightRear.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.siprnetRouter.snmpAgent.messageUtility = Optional.of(interactionMessageTransmitters);

		c4s2System.switchRouter.electricalPower.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.switchRouter.convectiveHeat.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.switchRouter.rackMount.mountLeftFront.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.switchRouter.rackMount.mountRightFront.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.switchRouter.rackMount.mountLeftRear.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.switchRouter.rackMount.mountRightRear.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.switchRouter.snmpAgent.messageUtility = Optional.of(interactionMessageTransmitters);

		radarSystem.messaging.radarServicesMessaging.messageUtility = Optional.of(interactionMessageTransmitters);
		radarSystem.radarSignalTransmitter.messageUtility = Optional.of(interactionMessageTransmitters);

		strikeSystem.messaging.strikeServicesMessaging.messageUtility = Optional.of(interactionMessageTransmitters);
		strikeSystem.ordnanceTransmitter.messageUtility = Optional.of(interactionMessageTransmitters);

		vehicleTarget.radarSignalReturn.messageUtility = Optional.of(interactionMessageTransmitters);

		c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.radarServicesMessaging.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.strikeServicesMessaging.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.targetServicesMessaging.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.systemServicesMessaging.messageUtility = Optional.of(interactionMessageTransmitters);

		c4s2System.c4s2ServicesComputer.radarServices.messaging.radarSystemMessaging.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.c4s2ServicesComputer.radarServices.messaging.targetServicesMessaging.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.c4s2ServicesComputer.radarServices.messaging.operatorServicesMessaging.messageUtility = Optional.of(interactionMessageTransmitters);

		c4s2System.c4s2ServicesComputer.strikeServices.messaging.strikeSystemMessaging.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.c4s2ServicesComputer.strikeServices.messaging.operatorServicesMessaging.messageUtility = Optional.of(interactionMessageTransmitters);

		c4s2System.c4s2ServicesComputer.systemServices.messaging.radarServicesMessaging.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.c4s2ServicesComputer.systemServices.messaging.strikeServicesMessaging.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.c4s2ServicesComputer.systemServices.messaging.targetServicesMessaging.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.c4s2ServicesComputer.systemServices.messaging.systemServicesMessaging.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.c4s2ServicesComputer.systemServices.messaging.operatorServicesMessaging.messageUtility = Optional.of(interactionMessageTransmitters);

		c4s2System.c4s2ServicesComputer.targetServices.messaging.strikeSystemMessaging.messageUtility = Optional.of(interactionMessageTransmitters);
		c4s2System.c4s2ServicesComputer.targetServices.messaging.operatorServicesMessaging.messageUtility = Optional.of(interactionMessageTransmitters);
	}

	/**
	 * Enable the transmission from the simulation to a state transitions table
	 * display of the states, events, transitions, guards, effects, and next states
	 * for each state transition. This operation enables a grapical display of the
	 * real-time state machines of the domain components (parts) to include the
	 * operator, radar and strike systems, and the computers of the C4S2System. The
	 * state transitions table is oftentimes quite useful for model debugging and
	 * test analysis. Each state machine whose state transitions table is to be
	 * displayed has its {@code enableStateTransitionStringsTransmission()}
	 * operation called here.
	 * 
	 * @see sysmlinjava.views.statetransitionstables.StateTransitionsDisplay
	 */
	public void enableStateTransitionTransmissions()
	{
		StateTransitionTablesTransmitter tablesTransmitter = new StateTransitionTablesTransmitter(StateTransitionsDisplay.udpPort, false);
		StateTransitionsTransmitters transitionsTransmitters = new StateTransitionsTransmitters(Optional.of(tablesTransmitter), Optional.empty(), false);

		operator.stateMachine.get().transitionsUtility = Optional.of(transitionsTransmitters);
		radarSystem.stateMachine.get().transitionsUtility = Optional.of(transitionsTransmitters);
		strikeSystem.stateMachine.get().transitionsUtility = Optional.of(transitionsTransmitters);
		vehicleTarget.stateMachine.get().transitionsUtility = Optional.of(transitionsTransmitters);
		c4s2System.c4s2ServicesComputer.stateMachine.get().transitionsUtility = Optional.of(transitionsTransmitters);
		c4s2System.operatorServicesComputer.stateMachine.get().transitionsUtility = Optional.of(transitionsTransmitters);
	}

	@Override
	protected void createParts()
	{
		vehicleTarget = new VehicleArmoredLargeTarget();
		radarSystem = new RadarSystem();
		strikeSystem = new StrikeSystem();
		c4s2System = new C4S2System();
		operator = new C4S2Operator();
		platform = new C4Platform();
	}

	@Override
	protected void createFlowConnectors()
	{
		messaging = new SysMLFlowConnector(TypesEnum.peertopeer, false,
			List.of(
			c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.radarServicesMessaging,
			c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.strikeServicesMessaging,
			c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.targetServicesMessaging,
			c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.systemServicesMessaging,
			c4s2System.c4s2ServicesComputer.systemServices.messaging.operatorServicesMessaging,
			c4s2System.c4s2ServicesComputer.radarServices.messaging.operatorServicesMessaging,
			c4s2System.c4s2ServicesComputer.strikeServices.messaging.operatorServicesMessaging,
			c4s2System.c4s2ServicesComputer.targetServices.messaging.operatorServicesMessaging,
			c4s2System.c4s2ServicesComputer.systemServices.messaging.radarServicesMessaging,
			c4s2System.c4s2ServicesComputer.systemServices.messaging.strikeServicesMessaging,
			c4s2System.c4s2ServicesComputer.systemServices.messaging.targetServicesMessaging,
			c4s2System.c4s2ServicesComputer.systemServices.messaging.systemServicesMessaging,
			c4s2System.c4s2ServicesComputer.radarServices.messaging.targetServicesMessaging,
			c4s2System.c4s2ServicesComputer.radarServices.messaging.systemServicesMessaging,
			c4s2System.c4s2ServicesComputer.targetServices.messaging.systemServicesMessaging,
			c4s2System.c4s2ServicesComputer.strikeServices.messaging.systemServicesMessaging,
			c4s2System.c4s2ServicesComputer.radarServices.messaging.radarSystemMessaging,
			radarSystem.messaging.radarServicesMessaging,
			c4s2System.c4s2ServicesComputer.strikeServices.messaging.strikeSystemMessaging,
			c4s2System.c4s2ServicesComputer.targetServices.messaging.strikeSystemMessaging,
			strikeSystem.messaging.strikeServicesMessaging),
			List.of(
			c4s2System.c4s2ServicesComputer.radarServices.messaging.operatorServicesMessaging,
			c4s2System.c4s2ServicesComputer.strikeServices.messaging.operatorServicesMessaging,
			c4s2System.c4s2ServicesComputer.targetServices.messaging.operatorServicesMessaging,
			c4s2System.c4s2ServicesComputer.systemServices.messaging.operatorServicesMessaging,
			c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.systemServicesMessaging,
			c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.radarServicesMessaging,
			c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.strikeServicesMessaging,
			c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.targetServicesMessaging,
			c4s2System.c4s2ServicesComputer.radarServices.messaging.systemServicesMessaging,
			c4s2System.c4s2ServicesComputer.strikeServices.messaging.systemServicesMessaging,
			c4s2System.c4s2ServicesComputer.targetServices.messaging.systemServicesMessaging,
			c4s2System.c4s2ServicesComputer.systemServices.messaging.systemServicesMessaging,
			c4s2System.c4s2ServicesComputer.targetServices.messaging.radarServicesMessaging,
			c4s2System.c4s2ServicesComputer.systemServices.messaging.radarServicesMessaging,
			c4s2System.c4s2ServicesComputer.systemServices.messaging.targetServicesMessaging,
			c4s2System.c4s2ServicesComputer.systemServices.messaging.strikeServicesMessaging,
			radarSystem.messaging.radarServicesMessaging,
			c4s2System.c4s2ServicesComputer.radarServices.messaging.radarSystemMessaging,
			strikeSystem.messaging.strikeServicesMessaging,
			strikeSystem.messaging.targetServicesMessaging,
			c4s2System.c4s2ServicesComputer.strikeServices.messaging.strikeSystemMessaging), "", 0L);
			
		strikeSystemToVehicleTarget = new SysMLFlowConnector(TypesEnum.peertopeer, false,
			strikeSystem.ordnanceTransmitter, vehicleTarget.ordnanceReceiver, "", 0L);

		operatorToC4S2System = new SysMLFlowConnector(TypesEnum.peertopeer, false,
			List.of(
			operator.onOffSwitchC4S2ServicesComputer,
			operator.onOffSwitchOperatorServicesComputer,
			operator.onOffSwitchEthernetSwitchIPRouter,
			operator.onOffSwitchSIPRNetRouter,
			c4s2System.operatorServicesComputer.c4s2OperatorServices.monitorView,
			operator.controlViewPort),
			List.of(
			c4s2System.c4s2ServicesComputer.mechanicalPowerOnOffSwitch,
			c4s2System.operatorServicesComputer.mechanicalPowerOnOffSwitch,
			c4s2System.switchRouter.mechanicalPowerOnOffSwitch,
			c4s2System.siprnetRouter.mechanicalPowerOnOffSwitch,
			operator.monitorViewPort,
			c4s2System.operatorServicesComputer.c4s2OperatorServices.controlView), "", 0L);

		radarToVehicle = new SysMLFlowConnector(TypesEnum.peertopeer, false,
			List.of(radarSystem.radarSignalTransmitter, vehicleTarget.radarSignalReturn),
			List.of(vehicleTarget.radarSignalReceiver, radarSystem.radarSignalReturnReceiver), "", 0L);
			
		c4s2ServicesToPlatformRackMounts = new SysMLFlowConnector(TypesEnum.peertopeer, false,
			List.of(
			c4s2System.c4s2ServicesComputer.rackMount.mountLeftFront,
			c4s2System.c4s2ServicesComputer.rackMount.mountRightFront,
			c4s2System.c4s2ServicesComputer.rackMount.mountLeftRear,
			c4s2System.c4s2ServicesComputer.rackMount.mountRightRear),
			List.of(
			platform.rackMount.railLeftFront.get(0),
			platform.rackMount.railRightFront.get(0),
			platform.rackMount.railLeftRear.get(0),
			platform.rackMount.railRightRear.get(0)), "", 0L);

		operatorServicesToPlatformRackMounts = new SysMLFlowConnector(TypesEnum.peertopeer, false,
			List.of(
			c4s2System.operatorServicesComputer.rackMount.mountLeftFront,
			c4s2System.operatorServicesComputer.rackMount.mountRightFront,
			c4s2System.operatorServicesComputer.rackMount.mountLeftRear,
			c4s2System.operatorServicesComputer.rackMount.mountRightRear),
			List.of(
			platform.rackMount.railLeftFront.get(1),
			platform.rackMount.railRightFront.get(1),
			platform.rackMount.railLeftRear.get(1),
			platform.rackMount.railRightRear.get(1)), "", 0L);

		switchRouterToPlatformRackMounts = new SysMLFlowConnector(TypesEnum.peertopeer, false,
			List.of(
			c4s2System.switchRouter.rackMount.mountLeftFront,
			c4s2System.switchRouter.rackMount.mountRightFront,
			c4s2System.switchRouter.rackMount.mountLeftRear,
			c4s2System.switchRouter.rackMount.mountRightRear),
			List.of(
			platform.rackMount.railLeftFront.get(2),
			platform.rackMount.railRightFront.get(2),
			platform.rackMount.railLeftRear.get(2),
			platform.rackMount.railRightRear.get(2)), "", 0L);

		siprnetRouterToPlatformRackMounts = new SysMLFlowConnector(TypesEnum.peertopeer, false,
			List.of(
			c4s2System.siprnetRouter.rackMount.mountLeftFront,
			c4s2System.siprnetRouter.rackMount.mountRightFront,
			c4s2System.siprnetRouter.rackMount.mountLeftRear,
			c4s2System.siprnetRouter.rackMount.mountRightRear),
			List.of(
			platform.rackMount.railLeftFront.get(2),
			platform.rackMount.railRightFront.get(2),
			platform.rackMount.railLeftRear.get(2),
			platform.rackMount.railRightRear.get(2)), "", 0L);

		platformPowerToEquipment = new SysMLFlowConnector(TypesEnum.peertopeer, true,
			List.of(
			platform.powerC4S2ServicesComputer,
			platform.powerOperatorServicesComputer,
			platform.powerEthernetSwitchIPRouter,
			platform.powerSIPRNetRouter),
			List.of(
			c4s2System.c4s2ServicesComputer.electricalPower,
			c4s2System.operatorServicesComputer.electricalPower,
			c4s2System.switchRouter.electricalPower,
			c4s2System.siprnetRouter.electricalPower), "", 0L);

		equipmentHeatToPlatform = new SysMLFlowConnector(TypesEnum.peertopeer, false,
			List.of(
			c4s2System.c4s2ServicesComputer.convectiveHeat,
			c4s2System.operatorServicesComputer.convectiveHeat,
			c4s2System.switchRouter.convectiveHeat,
			c4s2System.siprnetRouter.convectiveHeat),
			List.of(
			platform.heatC4S2ServicesComputer,
			platform.heatC4S2OperatorServicesComputer,
			platform.heatEthernetSwitchIPRouter,
			platform.heatSIPRNetRouter), "", 0L);
	}

	@Override
	protected void createAnalysisCases()
	{
		c4s2ExecutionAnalysisCase = new C4S2ExecutionAnalysisCase(AnimatedAreaDisplay.udpPort);
	}

	@Override
	protected void createBindingConnectors()
	{
		vehicleTargetPositionBindingConnector = new SysMLBindingConnector(vehicleTarget.currentPosition, c4s2ExecutionAnalysisCase, uidTargetPosition);
		strikeSystemPositionBindingConnector = new SysMLBindingConnector(strikeSystem.currentPosition, c4s2ExecutionAnalysisCase, uidStrikePosition);
		vehicleTargetStatusBindingConnector = new SysMLBindingConnector(vehicleTarget.operatingStatus, c4s2ExecutionAnalysisCase, uidTargetState);
		strikeSystemVelocityBindingConnector = new SysMLBindingConnector(strikeSystem.currentVelocity, c4s2ExecutionAnalysisCase, uidStrikeVelocity);

		//Add the connectors to the analysis case so it can automatically update parameter values
		c4s2ExecutionAnalysisCase.paramConnectors.put(uidTargetPosition, vehicleTargetPositionBindingConnector);
		c4s2ExecutionAnalysisCase.paramConnectors.put(uidStrikePosition, strikeSystemPositionBindingConnector);
		c4s2ExecutionAnalysisCase.paramConnectors.put(uidTargetState, vehicleTargetStatusBindingConnector);
		c4s2ExecutionAnalysisCase.paramConnectors.put(uidStrikeVelocity, strikeSystemVelocityBindingConnector);
	}

	/**
	 * The executable main operation for building and executing the C4S2 domain
	 * model.
	 * 
	 * @param args null, i.e. no arguments required
	 */
	public static void main(String[] args)
	{
		C4S2Domain domain = new C4S2Domain();
		domain.start();
		domain.awaitCompletion();
		domain.stop();
	}
}
