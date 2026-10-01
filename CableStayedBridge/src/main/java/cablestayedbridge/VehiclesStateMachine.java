package cablestayedbridge;

import java.util.Optional;

import sysmlinjava.attributetypes.DurationMilliseconds;
import sysmlinjava.events.SysMLChangeEvent;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.EntryActionFunction;
import sysmlinjava.javaannotations.statemachines.ExitActionFunction;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.FinalTransition;
import sysmlinjava.states.InitialTransition;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLEntryActionFunction;
import sysmlinjava.states.SysMLExitActionFunction;
import sysmlinjava.states.SysMLState;
import sysmlinjava.states.SysMLStateMachine;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;

/**
 * State machine for the collection of vehicles on the bridge. The state machine
 * represents the behavior of the set of vehicles on the bridge operating
 * asynchronously to (in separate thread from) the bridge iteself.
 * 
 * @author ModelerOne
 *
 */
@SuppressWarnings("unused")
public class VehiclesStateMachine extends SysMLStateMachine
{
	/**
	 * State for vehicles moving across bridge
	 */
	@State
	SysMLState moving;
	/**
	 * State for vehicles stopped
	 */
	@State
	SysMLState stopped;

	/**
	 * Action to be performed on entry into the moving state
	 */
	@EntryActionFunction
	SysMLEntryActionFunction onEnterMovingAction;
	/**
	 * Action to be performed on exit from the moving state
	 */
	@ExitActionFunction
	SysMLExitActionFunction onExitMovingAction;

	/**
	 * Transition from the initial to moving state
	 */
	@Transition
	InitialTransition initialToMoving;
	/**
	 * Transition (internal to moving state) to handle the time to move the vehicle
	 */
	@Transition
	SysMLTransition onMoveTime;
	/**
	 * Transition from the moving to stopped state
	 */
	@Transition
	SysMLTransition movingToStopped;
	/**
	 * Transition from the stopped to moving state
	 */
	@Transition
	SysMLTransition stoppedToMoving;
	/**
	 * Transition from the stopped to final state
	 */
	@Transition
	FinalTransition stoppedToFinal;

	/**
	 * Action to be performed upon a move time event
	 */
	@EffectActionFunction
	SysMLEffectActionFunction onMoveTimeEffectAction;

	/**
	 * Effect that performs the activity to be performed upon a move time event
	 */
	@Effect
	SysMLEffect onMoveTimeEffect;

	/**
	 * Constructor
	 * 
	 * @param context {@code Vehicles} in whose context this state machine
	 *                     operates
	 */
	public VehiclesStateMachine(Vehicles context)
	{
		super(Optional.of(context), true, "VehiclesStateMachine");
	}

	/**
	 * ID for the timer used to generate the next time for the vehicles to move
	 */
	final String vehiclesTimerID = "vehiclesTimer";

	@Override
	protected void createStateEntryActionFunctions()
	{
		onEnterMovingAction = (context) -> startTimer(vehiclesTimerID, DurationMilliseconds.of(1000), DurationMilliseconds.of(500));
	}

	@Override
	protected void createStateExitActionFunctions()
	{
		onExitMovingAction = (context) -> stopTimer(vehiclesTimerID);
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		moving = new SysMLState(context, Optional.of(onEnterMovingAction), Optional.empty(), Optional.of(onExitMovingAction), "Moving");
		stopped = new SysMLState(context, "Stopped");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		onMoveTimeEffectAction = (event, context) ->
		{
			((Vehicles)context.get()).onMoveTime();
		};
	}

	@Override
	protected void createEffects()
	{
		onMoveTimeEffect = new SysMLEffect(context, onMoveTimeEffectAction, "onMoveTime");
	}

	@Override
	protected void createTransitions()
	{
		initialToMoving = new InitialTransition(context, initialState, moving, "Initial");
		onMoveTime = new SysMLTransition(context, moving, moving, Optional.of(SysMLTimeEvent.class), Optional.empty(), Optional.of(onMoveTimeEffect), "OnMoveTime", SysMLTransitionKind.internal);
		movingToStopped = new SysMLTransition(context, moving, stopped, Optional.of(SysMLChangeEvent.class), Optional.empty(), Optional.empty(), "MovingToStopped", SysMLTransitionKind.external);
		stoppedToMoving = new SysMLTransition(context, stopped, moving, Optional.of(SysMLChangeEvent.class), Optional.empty(), Optional.empty(), "StoppedToMoving", SysMLTransitionKind.external);
		stoppedToFinal = new FinalTransition(context, stopped, finalState, "StoppedToFinal");
	}

}
