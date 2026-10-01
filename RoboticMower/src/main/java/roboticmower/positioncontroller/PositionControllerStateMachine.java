package roboticmower.positioncontroller;

import java.util.Optional;

import roboticmower.signals.VectorSignal;
import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.attributetypes.Vector2D;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.EntryActionFunction;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.FinalTransition;
import sysmlinjava.states.InitialTransition;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLEntryActionFunction;
import sysmlinjava.states.SysMLState;
import sysmlinjava.states.SysMLStateMachine;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;
import sysmlinjava.views.statetransitionstables.StateTransitionTablesTransmitter;
import sysmlinjava.views.statetransitionstables.StateTransitionsDisplay;
import sysmlinjava.views.statetransitionstables.StateTransitionsTransmitters;

/**
 * State machine behavior of the Position Controller. The state machine consists
 * of only two states - initializing and operating. While in the operating
 * state, the state machine performs internal transitions whose effects are to
 * initiate sensing of the powers position and to complete the position sensing
 * (calculate the mower's position and transmit mower's new velocity).
 * 
 * @author ModelerOne
 *
 */
public class PositionControllerStateMachine extends SysMLStateMachine
{
	/**
	 * State for initializing the subsystem
	 */
	@State
	SysMLState initializing;
	/**
	 * State for normal operation, i.e. responding to position sensing time and
	 * position sensing completion events
	 */
	@State
	SysMLState controlling;

	/**
	 * Transition from initial to initializing state
	 */
	@Transition
	InitialTransition initialToInitializing;
	/**
	 * Transition from initializing to controlling state
	 */
	@Transition
	SysMLTransition initializingToControlling;
	/**
	 * Transition within controlling state for position sensing time event
	 */
	@Transition
	SysMLTransition controllingOnPositionSensingTime;
	/**
	 * Transition within controlling state for position sensed event
	 */
	@Transition
	SysMLTransition controllingOnPositionSensed;
	/**
	 * Transition from operating to final state
	 */
	@Transition
	FinalTransition controllingToFinal;

	/**
	 * Action performed upon entering the initializing state
	 */
	@EntryActionFunction
	SysMLEntryActionFunction initializingOnEnterAction;

	/**
	 * Action performed as the effect of internal transition upon position sensing
	 * time event
	 */
	@EffectActionFunction
	SysMLEffectActionFunction controllingOnPositionSensingTimeEffectAction;
	/**
	 * Action performed as the effect of internal transition upon position sensed
	 * event
	 */
	@EffectActionFunction
	SysMLEffectActionFunction controllingOnPositionSensedEffectAction;

	/**
	 * Effect of internal transition upon position sensing time event
	 */
	@Effect
	SysMLEffect controllingOnPositionSensingTimeEffect;
	/**
	 * Effect of internal transition upon position sensed event
	 */
	@Effect
	SysMLEffect controllingOnPositionSensedEffect;

	/**
	 * Constructor
	 * 
	 * @param context Position Control Subsystem in whose context this state
	 *                     machine is to execute
	 */
	public PositionControllerStateMachine(PositionController context)
	{
		super(Optional.of(context), true, "PositionControlSubsystemStateMachine");
	}

	@Override
	protected void createStateEntryActionFunctions()
	{
		initializingOnEnterAction = (context) ->
		{
			((PositionController)context.get()).initialize();
		};
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializing = new SysMLState(context, Optional.of(initializingOnEnterAction), Optional.empty(), Optional.empty(), "Initializing");
		controlling = new SysMLState(context, "Controlling");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		controllingOnPositionSensingTimeEffectAction = (event, context) ->
		{
			System.out.println(getClass().getSimpleName() + ".OnPositionSensingTime():");
			PositionController controller = (PositionController)context.get();
			controller.onPositionSensingTime();
		};
		controllingOnPositionSensedEffectAction = (event, context) ->
		{
			System.out.println(getClass().getSimpleName() + ".OnPositionSensed():");
			PositionController controller = (PositionController)context.get();
			VectorSignal signal = (VectorSignal)((SysMLSignalEvent)event.get()).signal;
			Vector2D vector = signal.vector;
			controller.onPositionSensed(vector, InstantMilliseconds.now());
		};
	}

	@Override
	protected void createEffects()
	{
		controllingOnPositionSensingTimeEffect = new SysMLEffect(context, controllingOnPositionSensingTimeEffectAction, "PositionSensing");
		controllingOnPositionSensedEffect = new SysMLEffect(context, controllingOnPositionSensedEffectAction, "PositionSensed");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializing = new InitialTransition(context, initialState, initializing, "InitialToInitializing");
		initializingToControlling = new SysMLTransition(context, initializing, controlling, Optional.empty(), Optional.empty(), Optional.empty(), "InitializingToControlling", SysMLTransitionKind.external);
		controllingOnPositionSensingTime = new SysMLTransition(context, controlling, controlling, Optional.of(SysMLTimeEvent.class), Optional.empty(), Optional.of(controllingOnPositionSensingTimeEffect), "OnPositionSensingTime",
			SysMLTransitionKind.internal);
		controllingOnPositionSensed = new SysMLTransition(context, controlling, controlling, Optional.of(SysMLSignalEvent.class), Optional.empty(), Optional.of(controllingOnPositionSensedEffect), "OnPositionSensed",
			SysMLTransitionKind.internal);
		controllingToFinal = new FinalTransition(context, controlling, finalState, "ControllingToFinal");
	}

	@Override
	protected void createTransitionsUtility()
	{
		transitionsUtility = Optional.of(new StateTransitionsTransmitters(Optional.of(new StateTransitionTablesTransmitter(StateTransitionsDisplay.udpPort, false)), Optional.empty(), false));
	}
}
