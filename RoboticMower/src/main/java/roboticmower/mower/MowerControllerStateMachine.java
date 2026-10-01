package roboticmower.mower;

import java.util.Optional;

import roboticmower.signals.Point2DSignal;
import roboticmower.signals.VelocityControlSignal;
import sysmlinjava.attributetypes.Point2D;
import sysmlinjava.attributetypes.VelocityMetersPerSecondRadians;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.EntryActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.FinalTransition;
import sysmlinjava.states.InitialTransition;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLEntryActionFunction;
import sysmlinjava.states.SysMLGuard;
import sysmlinjava.states.SysMLGuardCondition;
import sysmlinjava.states.SysMLState;
import sysmlinjava.states.SysMLStateMachine;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;
import sysmlinjava.views.statetransitionstables.StateTransitionTablesTransmitter;
import sysmlinjava.views.statetransitionstables.StateTransitionsDisplay;
import sysmlinjava.views.statetransitionstables.StateTransitionsTransmitters;

/**
 * State machine behavior of the Mower Controller. The state machine consists of
 * only two states - initializing and operating. While in the operating state,
 * the state machine performs internal transitions whose effects are to respond
 * to velocity control and controller position sensing events.
 * 
 * @author ModelerOne
 *
 */
public class MowerControllerStateMachine extends SysMLStateMachine
{
	/**
	 * State for initializing the subsystem
	 */
	@State
	SysMLState initializing;
	/**
	 * State for normal operation, i.e. responding to position and velocity control
	 * events
	 */
	@State
	SysMLState operating;

	/**
	 * Transition from initial to initializing state
	 */
	@Transition
	InitialTransition initialToInitializing;
	/**
	 * Transition from initializing to operating state
	 */
	@Transition
	SysMLTransition initializingToOperating;
	/**
	 * Transition within operating state for velocity control event
	 */
	@Transition
	SysMLTransition operatingOnVelocityControl;
	/**
	 * Transition within operating state for controller position (sensing) event
	 */
	@Transition
	SysMLTransition operatingOnControllerPosition;
	/**
	 * Transition from operating to final state
	 */
	@Transition
	FinalTransition operatingToFinal;

	/**
	 * Action performed upon entering the initializing state
	 */
	@EntryActionFunction
	SysMLEntryActionFunction initializingOnEnterAction;

	/**
	 * Condition for whether an event is a velocity control
	 */
	@GuardCondition
	SysMLGuardCondition isVelocityControlGuardCondition;
	/**
	 * Condition for whether an event is a controller position
	 */
	@GuardCondition
	SysMLGuardCondition isControllerPositionGuardCondition;

	/**
	 * Guard for whether an event is a velocity control
	 */
	@Guard
	SysMLGuard isVelocityControlGuard;
	/**
	 * Guard for whether an event is a controller position
	 */
	@Guard
	SysMLGuard isControllerPositionGuard;

	/**
	 * Action performed as the effect of internal transition upon velocity control
	 * event
	 */
	@EffectActionFunction
	SysMLEffectActionFunction operatingOnVelocityControlEffectAction;
	/**
	 * Action performed as the effect of internal transition upon controller
	 * position (sensing) event
	 */
	@EffectActionFunction
	SysMLEffectActionFunction operatingOnControllerPositionEffectAction;

	/**
	 * Effect of internal transition upon velocity control event
	 */
	@Effect
	SysMLEffect operatingOnVelocityControlEffect;
	/**
	 * Effect of internal transition upon controller position (sensing) event
	 */
	@Effect
	SysMLEffect operatingOnControllerPositionEffect;

	/**
	 * Constructor
	 * 
	 * @param contextBlock block in whose context this state machine is to execute.
	 */
	public MowerControllerStateMachine(MowerController contextBlock)
	{
		super(Optional.of(contextBlock), true, "MowerControllerStateMachine");
	}

	@Override
	protected void createStateEntryActionFunctions()
	{
		initializingOnEnterAction = (contextBlock) ->
		{
			MowerController subsystem = (MowerController)contextBlock.get();
			subsystem.initialize();
		};
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializing = new SysMLState(context, Optional.of(initializingOnEnterAction), Optional.empty(), Optional.empty(), "Initializing");
		operating = new SysMLState(context, "Operating");
	}

	@Override
	protected void createGuardConditions()
	{
		isVelocityControlGuardCondition = (event, contextBlock) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof VelocityControlSignal;
		};
		isControllerPositionGuardCondition = (event, contextBlock) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof Point2DSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		isVelocityControlGuard = new SysMLGuard(context, isVelocityControlGuardCondition, "isVelocityControl");
		isControllerPositionGuard = new SysMLGuard(context, isControllerPositionGuardCondition, "isControllerPosition");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		operatingOnVelocityControlEffectAction = (event, context) ->
		{
			MowerController subsystem = (MowerController)context.get();
			VelocityControlSignal signal = (VelocityControlSignal)((SysMLSignalEvent)event.get()).signal;
			VelocityMetersPerSecondRadians velocity = signal.velocity;

			subsystem.onVelocityControl(velocity);
		};
		operatingOnControllerPositionEffectAction = (event, context) ->
		{
			MowerController subsystem = (MowerController)context.get();
			Point2DSignal signal = (Point2DSignal)((SysMLSignalEvent)event.get()).signal;
			Point2D position = signal.point;
			logger.info(position.toString());

			subsystem.onPositionSensing(position);
		};
	}

	@Override
	protected void createEffects()
	{
		operatingOnVelocityControlEffect = new SysMLEffect(context, operatingOnVelocityControlEffectAction, "OnVelocityControlEffect");
		operatingOnControllerPositionEffect = new SysMLEffect(context, operatingOnControllerPositionEffectAction, "OnControllerPositionEffect");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializing = new InitialTransition(context, initialState, initializing, "InitialToInitializing");
		initializingToOperating = new SysMLTransition(context, initializing, operating, Optional.empty(), Optional.empty(), Optional.empty(), "InitializingToOperating", SysMLTransitionKind.external);
		operatingOnVelocityControl = new SysMLTransition(context, operating, operating, Optional.of(SysMLSignalEvent.class), Optional.of(isVelocityControlGuard), Optional.of(operatingOnVelocityControlEffect),
			"OperatingOnVelocityControl", SysMLTransitionKind.internal);
		operatingOnControllerPosition = new SysMLTransition(context, operating, operating, Optional.of(SysMLSignalEvent.class), Optional.of(isControllerPositionGuard), Optional.of(operatingOnControllerPositionEffect),
			"OperatingOnControllerPosition", SysMLTransitionKind.internal);
		operatingToFinal = new FinalTransition(context, operating, finalState, "OperatingToFinal");
	}

	@Override
	protected void createTransitionsUtility()
	{
		transitionsUtility = Optional.of(new StateTransitionsTransmitters(Optional.of(new StateTransitionTablesTransmitter(StateTransitionsDisplay.udpPort, false)), Optional.empty(), false));
	}
}
