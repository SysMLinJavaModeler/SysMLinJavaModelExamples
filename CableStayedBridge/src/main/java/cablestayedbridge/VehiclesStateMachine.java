package cablestayedbridge;

import java.util.Optional;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.OnEnterActivity;
import sysmlinjava.annotations.statemachines.OnExitActivity;
import sysmlinjava.annotations.statemachines.State;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.statemachine.FinalTransition;
import sysmlinjava.statemachine.InitialTransition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLOnEnterActivity;
import sysmlinjava.statemachine.SysMLOnExitActivity;
import sysmlinjava.statemachine.SysMLState;
import sysmlinjava.statemachine.SysMLStateMachine;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjava.valuetypes.DurationMilliseconds;

/**
 * State machine for the collection of vehicles on the bridge. The state machine
 * represents the behavior of the set of vehicles on the bridge operating
 * asynchronously to (in separate thread from) the bridge iteself.
 * 
 * @author ModelerOne
 *
 */
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
	 * Activity to be performed on entry into the moving state
	 */
	@OnEnterActivity
	SysMLOnEnterActivity onEnterMovingActivity;
	/**
	 * Activity to be performed on exit from the moving state
	 */
	@OnExitActivity
	SysMLOnExitActivity onExitMovingActivity;

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
	 * Activity to be performed upon a move time event
	 */
	@EffectActivity
	SysMLEffectActivity onMoveTimeEffectActivity;

	/**
	 * Effect that performs the activity to be performed upon a move time event
	 */
	@Effect
	SysMLEffect onMoveTimeEffect;

	/**
	 * Constructor
	 * 
	 * @param contextBlock {@code Vehicles} in whose context this state machine
	 *                     operates
	 */
	public VehiclesStateMachine(Vehicles contextBlock)
	{
		super(Optional.of(contextBlock), true, "VehiclesStateMachine");
	}

	/**
	 * ID for the timer used to generate the next time for the vehicles to move
	 */
	final String vehiclesTimerID = "vehiclesTimer";

	@Override
	protected void createStateOnEnterActivities()
	{
		onEnterMovingActivity = (contextBlock) -> startTimer(vehiclesTimerID, DurationMilliseconds.of(1000), DurationMilliseconds.of(500));
	}

	@Override
	protected void createStateOnExitActivities()
	{
		onExitMovingActivity = (contextBlock) -> stopTimer(vehiclesTimerID);
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		moving = new SysMLState(contextBlock, Optional.of(onEnterMovingActivity), Optional.empty(), Optional.of(onExitMovingActivity), "Moving");
		stopped = new SysMLState(contextBlock, "Stopped");
	}

	@Override
	protected void createEffectActivities()
	{
		onMoveTimeEffectActivity = (event, contextBlock) ->
		{
			((Vehicles)contextBlock.get()).onMoveTime();
		};
	}

	@Override
	protected void createEffects()
	{
		onMoveTimeEffect = new SysMLEffect(contextBlock, onMoveTimeEffectActivity, "onMoveTime");
	}

	@Override
	protected void createTransitions()
	{
		initialToMoving = new InitialTransition(contextBlock, initialState, moving, "Initial");
		onMoveTime = new SysMLTransition(contextBlock, moving, moving, Optional.of(SysMLTimeEvent.class), Optional.empty(), Optional.of(onMoveTimeEffect), "OnMoveTime", SysMLTransitionKind.internal);
		movingToStopped = new SysMLTransition(contextBlock, moving, stopped, Optional.of(VehiclesEvent.class), Optional.empty(), Optional.empty(), "MovingToStopped", SysMLTransitionKind.external);
		stoppedToMoving = new SysMLTransition(contextBlock, stopped, moving, Optional.of(VehiclesEvent.class), Optional.empty(), Optional.empty(), "StoppedToMoving", SysMLTransitionKind.external);
		stoppedToFinal = new FinalTransition(contextBlock, stopped, finalState, "StoppedToFinal");
	}

}
