package c4s2.domain;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import c4s2.parametrics.C4S2ExecutionDisplayConstraintBlock;
import c4s2.platforms.C4Platform;
import c4s2.systems.c4s2.C4S2System;
import c4s2.systems.radar.RadarSystem;
import c4s2.systems.strike.StrikeSystem;
import c4s2.systems.target.VehicleArmoredLargeTarget;
import c4s2.users.C4S2Operator;
import sysmlinjava.analysis.animatedareadisplay.AnimatedAreaDisplay;
import sysmlinjava.analysis.interactionmessagetransmitter.InteractionMessageTransmitters;
import sysmlinjava.analysis.interactionsequence.InteractionMessageSequenceDisplay;
import sysmlinjava.analysis.interactionsequence.InteractionMessageTransmitter;
import sysmlinjava.analysis.statetransitions.StateTransitionTablesTransmitter;
import sysmlinjava.analysis.statetransitions.StateTransitionsDisplay;
import sysmlinjava.analysis.statetransitionstransmitters.StateTransitionsTransmitters;
import sysmlinjava.annotations.AssociationConnector;
import sysmlinjava.annotations.AssociationConnectorFunction;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.parametrics.BindingConnector;
import sysmlinjava.annotations.parametrics.BindingConnectorFunction;
import sysmlinjava.annotations.parametrics.ConstraintBlock;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.connectors.SysMLAssociationBlockConnector;
import sysmlinjava.connectors.SysMLAssociationBlockConnectorFunction;
import sysmlinjava.connectors.SysMLBindingConnector;
import sysmlinjava.connectors.SysMLBindingConnectorFunction;
import sysmlinjava.ports.SysMLConstraintParameterPort;

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
 *
 */
public class C4S2Domain extends SysMLBlock
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
	 * Functions that make connectors between the {@code C4S2ServicesComputer} and
	 * the {@code C4S2OperatorServicesComputer} parts for the purposes of messaging
	 * between them.
	 */
	@AssociationConnectorFunction
	private SysMLAssociationBlockConnectorFunction c4S2ToC4S2OperatorMessagingConnectorFunction;
	/**
	 * Functions that make connectors between the services of the
	 * {@code C4S2ServicesComputer} for the purposes of messaging between them.
	 */
	@AssociationConnectorFunction
	private SysMLAssociationBlockConnectorFunction c4S2ToC4S2MessagingConnectorFunction;
	/**
	 * Functions that make connectors between the {@code C4S2ServicesComputer} and
	 * the {@code RadarSystem} parts for the purposes of messaging
	 * between them.
	 */
	@AssociationConnectorFunction
	private SysMLAssociationBlockConnectorFunction c4S2ToRadarMessagingConnectorFunction;
	/**
	 * Functions that make connectors between the {@code C4S2ServicesComputer} and
	 * the {@code StrikeSystem} parts for the purposes of messaging
	 * between them.
	 */
	@AssociationConnectorFunction
	private SysMLAssociationBlockConnectorFunction c4S2ToStrikeMessagingConnectorFunction;
	/**
	 * Functions that make connectors between the operator and the C4S2 system, to
	 * include the operator displays and services
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction operatorToC4S2SystemConnectorFunction;
	/**
	 * Functions that make connectors between the radar system and the target
	 * vehicle
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction radarSystemToVehicleTargetConnectorFunction;
	/**
	 * Functions that make connectors between the strike system and the target
	 * vehicle
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction strikeSystemToVehicleTargetConnectorFunction;
	/**
	 * Functions that make connectors between the C4S2 system and the C4 platform
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction c4s2SystemToC4PlatformConnectorFunction;

	/**
	 * Connectors between the {@code C4S2ServicesComputer} and
	 * the {@code C4S2OperatorServicesComputer} parts for the purposes of messaging
	 * between them.
	 */
	@AssociationConnector
	private SysMLAssociationBlockConnector c4S2ToC4S2OperatorMessagingConnector;
	/**
	 * Connectors between the services of the
	 * {@code C4S2ServicesComputer} for the purposes of messaging between them.
	 */
	@AssociationConnector
	private SysMLAssociationBlockConnector c4S2ToC4S2MessagingConnector;
	/**
	 * Connectors between the {@code C4S2ServicesComputer} and
	 * the {@code RadarSystem} parts for the purposes of messaging
	 * between them.
	 */
	@AssociationConnector
	private SysMLAssociationBlockConnector c4S2ToRadarMessagingConnector;
	/**
	 * Connectors between the {@code C4S2ServicesComputer} and
	 * the {@code StrikeSystem} parts for the purposes of messaging
	 * between them.
	 */
	@AssociationConnector
	private SysMLAssociationBlockConnector c4S2ToStrikeMessagingConnector;
	/**
	 * Connector between the operator and the C4S2 system, to include the operator
	 * displays and services
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector operatorToC4S2SystemConnector;
	/**
	 * Connector between the radar system and the target vehicle
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector radarSystemToVehicleTargetConnector;
	/**
	 * Connector between the strike system and the target vehicle
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector strikeSystemToVehicleTargetConnector;
	/**
	 * Connector between the C4S2 system and the C4 platform
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector c4s2SystemToC4PlatformConnector;

	@ConstraintBlock
	public C4S2ExecutionDisplayConstraintBlock c4s2ExecutionDisplayConstraintBlock;

	@BindingConnectorFunction
	public SysMLBindingConnectorFunction c4s2ExecutionDisplayConstraintParameterConnectorFunction;

	@BindingConnector
	public SysMLBindingConnector c4s2ExecutionDisplayConstraintParameterConnector;

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
		logger.info("domain started, starting execution display constraint block...");
		c4s2ExecutionDisplayConstraintBlock.start();
		logger.info("execution display constraint block started");
	}

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
			c4s2ExecutionDisplayConstraintBlock.concurrentExecutionThreads.awaitTermination(30, TimeUnit.MINUTES);
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
		c4s2ExecutionDisplayConstraintBlock.stop();
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
		InteractionMessageTransmitter transmitter = new InteractionMessageTransmitter(InteractionMessageSequenceDisplay.udpPort, true);
		InteractionMessageTransmitters interactionMessageTransmitters = new InteractionMessageTransmitters(transmitter);

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
	 * @see sysmlinjava.analysis.statetransitions.StateTransitionsDisplay
	 */
	public void enableStateTransitionTransmissions()
	{
		StateTransitionTablesTransmitter tablesTransmitter = new StateTransitionTablesTransmitter(StateTransitionsDisplay.udpPort, true);
		StateTransitionsTransmitters transitionsTransmitters = new StateTransitionsTransmitters(Optional.of(tablesTransmitter), Optional.empty());

		operator.stateMachine.get().transitionsUtility = Optional.of(transitionsTransmitters);
		radarSystem.stateMachine.get().transitionsUtility = Optional.of(transitionsTransmitters);
		strikeSystem.stateMachine.get().transitionsUtility = Optional.of(transitionsTransmitters);
		vehicleTarget.stateMachine.get().transitionsUtility = Optional.of(transitionsTransmitters);
		c4s2System.c4s2ServicesComputer.stateMachine.get().transitionsUtility = Optional.of(transitionsTransmitters);
		c4s2System.operatorServicesComputer.stateMachine.get().transitionsUtility = Optional.of(transitionsTransmitters);
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.empty();
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
	protected void createConnectorFunctions()
	{
		c4S2ToC4S2OperatorMessagingConnectorFunction = () ->
		{
			c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.radarServicesMessaging.addConnectedPortPeer(c4s2System.c4s2ServicesComputer.radarServices.messaging.operatorServicesMessaging);
			c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.strikeServicesMessaging.addConnectedPortPeer(c4s2System.c4s2ServicesComputer.strikeServices.messaging.operatorServicesMessaging);
			c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.targetServicesMessaging.addConnectedPortPeer(c4s2System.c4s2ServicesComputer.targetServices.messaging.operatorServicesMessaging);
			c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.systemServicesMessaging.addConnectedPortPeer(c4s2System.c4s2ServicesComputer.systemServices.messaging.operatorServicesMessaging);
			c4s2System.c4s2ServicesComputer.systemServices.messaging.operatorServicesMessaging.addConnectedPortPeer(c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.systemServicesMessaging);
			c4s2System.c4s2ServicesComputer.radarServices.messaging.operatorServicesMessaging.addConnectedPortPeer(c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.radarServicesMessaging);
			c4s2System.c4s2ServicesComputer.strikeServices.messaging.operatorServicesMessaging.addConnectedPortPeer(c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.strikeServicesMessaging);
			c4s2System.c4s2ServicesComputer.targetServices.messaging.operatorServicesMessaging.addConnectedPortPeer(c4s2System.operatorServicesComputer.c4s2OperatorServices.messaging.targetServicesMessaging);
		};
		c4S2ToC4S2MessagingConnectorFunction = () ->
		{
			c4s2System.c4s2ServicesComputer.systemServices.messaging.radarServicesMessaging.addConnectedPortPeer(c4s2System.c4s2ServicesComputer.radarServices.messaging.systemServicesMessaging);
			c4s2System.c4s2ServicesComputer.systemServices.messaging.strikeServicesMessaging.addConnectedPortPeer(c4s2System.c4s2ServicesComputer.strikeServices.messaging.systemServicesMessaging);
			c4s2System.c4s2ServicesComputer.systemServices.messaging.targetServicesMessaging.addConnectedPortPeer(c4s2System.c4s2ServicesComputer.targetServices.messaging.systemServicesMessaging);
			c4s2System.c4s2ServicesComputer.systemServices.messaging.systemServicesMessaging.addConnectedPortPeer(c4s2System.c4s2ServicesComputer.systemServices.messaging.systemServicesMessaging);
			c4s2System.c4s2ServicesComputer.radarServices.messaging.targetServicesMessaging.addConnectedPortPeer(c4s2System.c4s2ServicesComputer.targetServices.messaging.radarServicesMessaging);
			c4s2System.c4s2ServicesComputer.radarServices.messaging.systemServicesMessaging.addConnectedPortPeer(c4s2System.c4s2ServicesComputer.systemServices.messaging.radarServicesMessaging);
			c4s2System.c4s2ServicesComputer.targetServices.messaging.systemServicesMessaging.addConnectedPortPeer(c4s2System.c4s2ServicesComputer.systemServices.messaging.targetServicesMessaging);
			c4s2System.c4s2ServicesComputer.strikeServices.messaging.systemServicesMessaging.addConnectedPortPeer(c4s2System.c4s2ServicesComputer.systemServices.messaging.strikeServicesMessaging);

		};
		c4S2ToRadarMessagingConnectorFunction = () ->
		{
			c4s2System.c4s2ServicesComputer.radarServices.messaging.radarSystemMessaging.addConnectedPortPeer(radarSystem.messaging.radarServicesMessaging);
			radarSystem.messaging.radarServicesMessaging.addConnectedPortPeer(c4s2System.c4s2ServicesComputer.radarServices.messaging.radarSystemMessaging);

		};
		c4S2ToStrikeMessagingConnectorFunction = () ->
		{
			c4s2System.c4s2ServicesComputer.strikeServices.messaging.strikeSystemMessaging.addConnectedPortPeer(strikeSystem.messaging.strikeServicesMessaging);
			c4s2System.c4s2ServicesComputer.targetServices.messaging.strikeSystemMessaging.addConnectedPortPeer(strikeSystem.messaging.targetServicesMessaging);
			strikeSystem.messaging.strikeServicesMessaging.addConnectedPortPeer(c4s2System.c4s2ServicesComputer.strikeServices.messaging.strikeSystemMessaging);
		};
		operatorToC4S2SystemConnectorFunction = () ->
		{
			operator.onOffSwitchC4S2ServicesComputer.addConnectedPortPeer(c4s2System.c4s2ServicesComputer.mechanicalPowerOnOffSwitch);
			operator.onOffSwitchOperatorServicesComputer.addConnectedPortPeer(c4s2System.operatorServicesComputer.mechanicalPowerOnOffSwitch);
			operator.onOffSwitchEthernetSwitchIPRouter.addConnectedPortPeer(c4s2System.switchRouter.mechanicalPowerOnOffSwitch);
			operator.onOffSwitchSIPRNetRouter.addConnectedPortPeer(c4s2System.siprnetRouter.mechanicalPowerOnOffSwitch);
			c4s2System.operatorServicesComputer.c4s2OperatorServices.monitorView.addConnectedPortPeer(operator.monitorViewPort);
			operator.controlViewPort.addConnectedPortPeer(c4s2System.operatorServicesComputer.c4s2OperatorServices.controlView);
		};
		radarSystemToVehicleTargetConnectorFunction = () ->
		{
			radarSystem.radarSignalTransmitter.addConnectedPortPeer(vehicleTarget.radarSignalReceiver);
			vehicleTarget.radarSignalReturn.addConnectedPortPeer(radarSystem.radarSignalReturnReceiver);
		};
		strikeSystemToVehicleTargetConnectorFunction = () ->
		{
			strikeSystem.ordnanceTransmitter.addConnectedPortPeer(vehicleTarget.ordnanceReceiver);
		};
		c4s2SystemToC4PlatformConnectorFunction = () ->
		{
			c4s2System.c4s2ServicesComputer.rackMount.mountLeftFront.addConnectedPortPeer(platform.rackMount.railLeftFront.get(0));
			c4s2System.c4s2ServicesComputer.rackMount.mountRightFront.addConnectedPortPeer(platform.rackMount.railRightFront.get(0));
			c4s2System.c4s2ServicesComputer.rackMount.mountLeftRear.addConnectedPortPeer(platform.rackMount.railLeftRear.get(0));
			c4s2System.c4s2ServicesComputer.rackMount.mountRightRear.addConnectedPortPeer(platform.rackMount.railRightRear.get(0));

			c4s2System.operatorServicesComputer.rackMount.mountLeftFront.addConnectedPortPeer(platform.rackMount.railLeftFront.get(1));
			c4s2System.operatorServicesComputer.rackMount.mountRightFront.addConnectedPortPeer(platform.rackMount.railRightFront.get(1));
			c4s2System.operatorServicesComputer.rackMount.mountLeftRear.addConnectedPortPeer(platform.rackMount.railLeftRear.get(1));
			c4s2System.operatorServicesComputer.rackMount.mountRightRear.addConnectedPortPeer(platform.rackMount.railRightRear.get(1));

			c4s2System.switchRouter.rackMount.mountLeftFront.addConnectedPortPeer(platform.rackMount.railLeftFront.get(2));
			c4s2System.switchRouter.rackMount.mountRightFront.addConnectedPortPeer(platform.rackMount.railRightFront.get(2));
			c4s2System.switchRouter.rackMount.mountLeftRear.addConnectedPortPeer(platform.rackMount.railLeftRear.get(2));
			c4s2System.switchRouter.rackMount.mountRightRear.addConnectedPortPeer(platform.rackMount.railRightRear.get(2));

			c4s2System.siprnetRouter.rackMount.mountLeftFront.addConnectedPortPeer(platform.rackMount.railLeftFront.get(3));
			c4s2System.siprnetRouter.rackMount.mountRightFront.addConnectedPortPeer(platform.rackMount.railRightFront.get(3));
			c4s2System.siprnetRouter.rackMount.mountLeftRear.addConnectedPortPeer(platform.rackMount.railLeftRear.get(3));
			c4s2System.siprnetRouter.rackMount.mountRightRear.addConnectedPortPeer(platform.rackMount.railRightRear.get(3));

			platform.powerC4S2ServicesComputer.addConnectedPortPeer(c4s2System.c4s2ServicesComputer.electricalPower);
			c4s2System.c4s2ServicesComputer.electricalPower.addConnectedPortPeer(platform.powerC4S2ServicesComputer);
			platform.powerOperatorServicesComputer.addConnectedPortPeer(c4s2System.operatorServicesComputer.electricalPower);
			c4s2System.operatorServicesComputer.electricalPower.addConnectedPortPeer(platform.powerOperatorServicesComputer);
			platform.powerEthernetSwitchIPRouter.addConnectedPortPeer(c4s2System.switchRouter.electricalPower);
			c4s2System.switchRouter.electricalPower.addConnectedPortPeer(platform.powerEthernetSwitchIPRouter);
			platform.powerSIPRNetRouter.addConnectedPortPeer(c4s2System.siprnetRouter.electricalPower);
			c4s2System.siprnetRouter.electricalPower.addConnectedPortPeer(platform.powerSIPRNetRouter);

			c4s2System.c4s2ServicesComputer.convectiveHeat.addConnectedPortPeer(platform.heatC4S2ServicesComputer);
			c4s2System.operatorServicesComputer.convectiveHeat.addConnectedPortPeer(platform.heatC4S2OperatorServicesComputer);
			c4s2System.switchRouter.convectiveHeat.addConnectedPortPeer(platform.heatEthernetSwitchIPRouter);
			c4s2System.siprnetRouter.convectiveHeat.addConnectedPortPeer(platform.heatSIPRNetRouter);
		};
	}

	@Override
	protected void createConnectors()
	{
		c4S2ToC4S2OperatorMessagingConnector = new SysMLAssociationBlockConnector(c4s2System.operatorServicesComputer, c4s2System.c4s2ServicesComputer, c4S2ToC4S2OperatorMessagingConnectorFunction);
		c4S2ToC4S2MessagingConnector = new SysMLAssociationBlockConnector(c4s2System.c4s2ServicesComputer, c4s2System.c4s2ServicesComputer, c4S2ToC4S2MessagingConnectorFunction);
		c4S2ToRadarMessagingConnector = new SysMLAssociationBlockConnector(c4s2System, radarSystem, c4S2ToRadarMessagingConnectorFunction);
		c4S2ToStrikeMessagingConnector = new SysMLAssociationBlockConnector(c4s2System, strikeSystem, c4S2ToStrikeMessagingConnectorFunction);
		
		operatorToC4S2SystemConnector = new SysMLAssociationBlockConnector(operator, c4s2System, operatorToC4S2SystemConnectorFunction);
		radarSystemToVehicleTargetConnector = new SysMLAssociationBlockConnector(radarSystem, vehicleTarget, radarSystemToVehicleTargetConnectorFunction);
		strikeSystemToVehicleTargetConnector = new SysMLAssociationBlockConnector(strikeSystem, vehicleTarget, strikeSystemToVehicleTargetConnectorFunction);
		c4s2SystemToC4PlatformConnector = new SysMLAssociationBlockConnector(c4s2System, platform, c4s2SystemToC4PlatformConnectorFunction);
	}

	@Override
	protected void createConstraintBlocks()
	{
		c4s2ExecutionDisplayConstraintBlock = new C4S2ExecutionDisplayConstraintBlock(AnimatedAreaDisplay.udpPort);
	}

	@Override
	protected void createConstraintParameterConnectorFunctions()
	{
		c4s2ExecutionDisplayConstraintParameterConnectorFunction = () ->
		{
			SysMLConstraintParameterPort targetPositionPort = c4s2ExecutionDisplayConstraintBlock.paramPorts.get(C4S2ExecutionDisplayConstraintBlock.uidTargetPosition);
			targetPositionPort.parameterContextBlock = vehicleTarget;
			vehicleTarget.currentPosition.bindTo(targetPositionPort);

			SysMLConstraintParameterPort strikePositionPort = c4s2ExecutionDisplayConstraintBlock.paramPorts.get(C4S2ExecutionDisplayConstraintBlock.uidStrikePosition);
			strikePositionPort.parameterContextBlock = strikeSystem;
			strikeSystem.currentPosition.bindTo(strikePositionPort);

			SysMLConstraintParameterPort targetStatusPort = c4s2ExecutionDisplayConstraintBlock.paramPorts.get(C4S2ExecutionDisplayConstraintBlock.uidTargetState);
			targetStatusPort.parameterContextBlock = vehicleTarget;
			vehicleTarget.operatingStatus.bindTo(targetStatusPort);

			SysMLConstraintParameterPort strikeVelocityPort = c4s2ExecutionDisplayConstraintBlock.paramPorts.get(C4S2ExecutionDisplayConstraintBlock.uidStrikeVelocity);
			strikeVelocityPort.parameterContextBlock = strikeSystem;
			strikeSystem.currentVelocity.bindTo(strikeVelocityPort);
		};
	}

	@Override
	protected void createConstraintParameterConnectors()
	{
		c4s2ExecutionDisplayConstraintParameterConnector = new SysMLBindingConnector(Arrays.asList(strikeSystem, vehicleTarget), c4s2ExecutionDisplayConstraintBlock, c4s2ExecutionDisplayConstraintParameterConnectorFunction);
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
