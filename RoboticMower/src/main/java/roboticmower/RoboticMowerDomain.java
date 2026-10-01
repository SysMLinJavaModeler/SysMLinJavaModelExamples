package roboticmower;

import static roboticmower.analysis.RoboticMowerAnimationAnalysisCase.uidMowerPosition;
import static roboticmower.analysis.RoboticMowerAnimationAnalysisCase.uidMowerVectorValue;
import static roboticmower.analysis.RoboticMowerAnimationAnalysisCase.uidMowerVelocity;
import static roboticmower.analysis.RoboticMowerAnimationAnalysisCase.uidPositionControllerPosition;
import static roboticmower.analysis.RoboticMowerWaypointErrorFrequencyAnalysisCase.uidWaypointActual;
import static roboticmower.analysis.RoboticMowerWaypointErrorFrequencyAnalysisCase.uidWaypointPlanned;

import java.util.Optional;

import roboticmower.analysis.RoboticMowerAnimationAnalysisCase;
import roboticmower.analysis.RoboticMowerWaypointErrorFrequencyAnalysisCase;
import roboticmower.analysis.RoboticMowerWaypointErrorPlotAnalysisCase;
import roboticmower.mower.Mower;
import roboticmower.positioncontroller.PositionController;
import sysmlinjava.connectors.SysMLBindingConnector;
import sysmlinjava.connectors.SysMLFlowConnector;
import sysmlinjava.connectors.SysMLFlowConnector.TypesEnum;
import sysmlinjava.javaannotations.analysis.parametrics.ParametricAnalysis;
import sysmlinjava.javaannotations.connectors.BindingConnector;
import sysmlinjava.javaannotations.connectors.FlowConnector;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.views.animatedareadisplay.AnimatedAreaDisplay;
import sysmlinjava.views.barcharts.BarChartsDisplay;
import sysmlinjava.views.interactionssequencediagram.InteractionMessageSequenceDisplay;
import sysmlinjava.views.interactionssequencediagram.InteractionMessageTransmitter;
import sysmlinjava.views.interactionssequencediagram.InteractionMessageTransmitters;
import sysmlinjava.views.scatterplots.ScatterPlotsDisplay;

/**
 * Domain model of the RoboticMower system's domain. The domain consists of an
 * instance of the mower and its associated position controller, both of which
 * are executable blocks that simulate mower operation. It also consists of an
 * assumed lawn area on which are a set of waypoints that the mower must
 * traverse to mow the lawn.
 * <p>
 * The domain also includes two constraint blocks. One constraint block
 * transmits data to an animated display of the domain, i.e. a moving mower
 * controlled in real-time by a controller operating on a simple plot of turf.
 * All ports in the model transmit interaction (message/signal) data to a
 * sequence diagram display. And all state machines are configured to transmit
 * state/event/transition data to a state tables display.
 * <p>
 * Another constraint block calculates a frequency distribution for the error
 * distance between the mower's planned and actual waypoint traversals. The
 * constraint block also transmits the frequency distribution to a bar chart
 * display for graphical visualization.
 * <p>
 * This executable model demonstrates the basic features and capabilities of the
 * SysMLinJava model. It is multi-threaded (components execute asynchronously
 * from one another) and of limited precision/detail. This first version of the
 * domain is limited to the modeling of the system's position control. Expansion
 * to modeling and simulation of the other components are "left to the reader".
 * 
 * @author ModelerOne
 */
public class RoboticMowerDomain extends SysMLPart
{
	/**
	 * Part representing the mower to include its controller and its drive and blade
	 * (not fully modeled) subsystems
	 */
	@Part
	Mower mower;
	/**
	 * Part representing the position controller which controls the position of the
	 * mower
	 */
	@Part
	PositionController positionController;

	/**
	 * Constraint block for the animated display of the domain including the mower
	 * and the psotion controller
	 */
	@ParametricAnalysis
	RoboticMowerAnimationAnalysisCase animation;
	/**
	 * Constraint block for the calculation and display of the frequency
	 * distribution of the mower's planned vs actual waypoint error
	 */
	@ParametricAnalysis
	RoboticMowerWaypointErrorFrequencyAnalysisCase waypointErrorFrequency;
	/**
	 * Constraint block for the plot of the mower's planned vs actual waypoint
	 * errors
	 */
	@ParametricAnalysis
	RoboticMowerWaypointErrorPlotAnalysisCase waypointErrorPlot;

	/**
	 * Connection of the controller's position receiver to the mower's position
	 * reflector
	 */
	@FlowConnector
	SysMLFlowConnector positionReceiverToPositionReflectorConnector;
	/**
	 * Connection of the mower's position reflector to the controller's position
	 * receiver
	 */
	@FlowConnector
	SysMLFlowConnector positionReflectorToPositionReceiverConnector;
	/**
	 * Connection of the controller's velocity transmitter to the mower's velocity
	 * receiver
	 */
	@FlowConnector
	SysMLFlowConnector velocityTransmitterToVelocityReceiverConnector;

	/**
	 * Connector that "binds" the position controller position value to its
	 * constraint parameter
	 */
	@BindingConnector
	public SysMLBindingConnector positionControllerPositionBindingConnector;
	/**
	 * Connector that "binds" the mower velocity value to its constraint parameter
	 */
	@BindingConnector
	public SysMLBindingConnector mowerPositionBindingConnector;
	/**
	 * Function that "binds" the mower vector value to its constraint parameter
	 */
	@BindingConnector
	public SysMLBindingConnector mowerVectorBindingConnector;
	/**
	 * Connector that "binds" the mower velocity value to its constraint parameter
	 */
	@BindingConnector
	public SysMLBindingConnector mowerVelocityBindingConnector;
	/**
	 * Connector that "binds" the actual mower waypoint value to its constraint
	 * parameter
	 */
	@BindingConnector
	public SysMLBindingConnector mowerWaypointActualBindingConnector;
	/**
	 * Connector that "binds" the planned mower waypoint value to its constraint
	 * parameter
	 */
	@BindingConnector
	public SysMLBindingConnector mowerWaypointPlannedBindingConnector;

	/**
	 * Constructor which invokes all of the creation/initialization of the domain
	 * properties
	 */
	public RoboticMowerDomain()
	{
		super("Domain", 0L);
	}

	@Override
	public void start()
	{
		logger.info("starting");
		waypointErrorFrequency.start();
		waypointErrorPlot.start();
		animation.start();
		mower.start();
		positionController.start();
	}

	@Override
	public void stop()
	{
		logger.info("stopping");
		positionController.stop();
		mower.stop();
		animation.stop();
		waypointErrorPlot.stop();
		waypointErrorFrequency.stop();
	}

	@Override
	protected void createParts()
	{
		mower = new Mower();
		positionController = new PositionController();
	}

	@Override
	protected void createFlowConnectors()
	{
		positionReceiverToPositionReflectorConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false,
			positionController.vectorReceiver, mower.controller.reflector, "", 0L);
		positionReflectorToPositionReceiverConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false,
			mower.controller.reflector, positionController.vectorReceiver, "", 0L);
		velocityTransmitterToVelocityReceiverConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false,
			positionController.velocityTransmitter, mower.controller.velocityReceiver, "", 0L);
	}

	@Override
	protected void createAnalysisCases()
	{
		animation = new RoboticMowerAnimationAnalysisCase(AnimatedAreaDisplay.udpPort);
		waypointErrorFrequency = new RoboticMowerWaypointErrorFrequencyAnalysisCase(BarChartsDisplay.udpPort);
		waypointErrorPlot = new RoboticMowerWaypointErrorPlotAnalysisCase(waypointErrorFrequency, ScatterPlotsDisplay.udpPort);
	}

	@Override
	protected void createBindingConnectors()
	{
		positionControllerPositionBindingConnector = new SysMLBindingConnector(positionController.controllerPosition, animation, uidPositionControllerPosition);
		mowerVectorBindingConnector = new SysMLBindingConnector(positionController.mowerVector, animation, uidMowerVectorValue);

		mowerPositionBindingConnector = new SysMLBindingConnector(mower.controller.lastPosition, animation, uidMowerPosition);
		mowerVelocityBindingConnector = new SysMLBindingConnector(mower.controller.velocity, animation, uidMowerVelocity);

		mowerWaypointActualBindingConnector = new SysMLBindingConnector(positionController.currWaypointActual, waypointErrorFrequency, uidWaypointActual);
		mowerWaypointPlannedBindingConnector = new SysMLBindingConnector(positionController.currWaypointPlanned, waypointErrorFrequency, uidWaypointPlanned);

		animation.paramConnectors.put(uidMowerPosition, mowerPositionBindingConnector);
		animation.paramConnectors.put(uidPositionControllerPosition, positionControllerPositionBindingConnector);
		animation.paramConnectors.put(uidMowerVelocity, mowerVelocityBindingConnector);
		animation.paramConnectors.put(uidMowerVectorValue, mowerVectorBindingConnector);

		waypointErrorFrequency.paramConnectors.put(uidWaypointActual, mowerWaypointActualBindingConnector);
		waypointErrorFrequency.paramConnectors.put(uidWaypointPlanned, mowerWaypointPlannedBindingConnector);
	}

	@Override
	protected void enableInteractionMessageTransmissions()
	{
		InteractionMessageTransmitter transmitter = new InteractionMessageTransmitter(InteractionMessageSequenceDisplay.udpPort, false);
		InteractionMessageTransmitters interactionMessageTransmitters = new InteractionMessageTransmitters(transmitter, false);

		positionController.vectorReceiver.messageUtility = Optional.of(interactionMessageTransmitters);
		positionController.velocityTransmitter.messageUtility = Optional.of(interactionMessageTransmitters);
		mower.controller.reflector.messageUtility = Optional.of(interactionMessageTransmitters);
		mower.controller.velocityReceiver.messageUtility = Optional.of(interactionMessageTransmitters);
		mower.controller.wheelsControlTransmitter.messageUtility = Optional.of(interactionMessageTransmitters);
	}

	/**
	 * Main operation that instantiates and starts the domain and waits for about 6
	 * minutes for the simulation to complete before stopping the domain. This main
	 * operation constitutes the execution of the executable SysMLinJava model.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args)
	{
		RoboticMowerDomain domain = new RoboticMowerDomain();
		domain.start();
		try
		{
			Thread.sleep(6 * 60_000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		domain.stop();
		System.exit(1);
	}
}
