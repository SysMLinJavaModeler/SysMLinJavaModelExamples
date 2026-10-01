package hflink.systems.gps;

import java.util.Optional;

import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.FinalTransition;
import sysmlinjava.states.InitialTransition;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
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
 * State machine for the GPS model/simulation. The state machine consists of
 * states to initialize and operate, with the key transition being the one that
 * occurs at the time to transmit a GPS time message. See the model of the state
 * machine below for more detail.
 * 
 * @author ModelerOne
 *
 */
@SuppressWarnings({"javadoc", "unused"})
public class GPSStateMachine extends SysMLStateMachine
{
	@State
	private SysMLState initializingState;
	@State
	private SysMLState operationalState;

	@Transition
	private InitialTransition initialToInitializingTransition;
	@Transition
	private SysMLTransition initializingToOperationalTransition;
	@Transition
	private SysMLTransition operationalOnGPSMessageTimeTransition;
	@Transition
	private FinalTransition operationalToFinalTransition;

	@GuardCondition
	private SysMLGuardCondition isGPSMessageTimeEventGuardCondition;
	@Guard
	private SysMLGuard isGPSMessageTimeEventGuard;

	@EffectActionFunction
	private SysMLEffectActionFunction onGPSMessageTimeEffectAction;
	@Effect
	private SysMLEffect onGPSMessageTimeEffect;

	public GPSStateMachine(GPS gps)
	{
		super(Optional.of(gps), true, "GPSStateMachine");
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(context, "Initializing");
		operationalState = new SysMLState(context, "Operational");
	}

	@Override
	protected void createGuardConditions()
	{
		isGPSMessageTimeEventGuardCondition = (currentEvent, contextBlock) ->
		{
			boolean result = false;
			if (currentEvent.get() instanceof SysMLTimeEvent timeEvent &&
				timeEvent.timerID.equals(GPS.timerID))
					result = true;
			return result;
		};
	}

	@Override
	protected void createGuards()
	{
		isGPSMessageTimeEventGuard = new SysMLGuard(context, isGPSMessageTimeEventGuardCondition, "isGPSMessageTime");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		onGPSMessageTimeEffectAction = (event, contextBlock) ->
		{
			GPS gps = (GPS)contextBlock.get();
			gps.onGPSMessageTime(InstantMilliseconds.now());
		};
	}

	@Override
	protected void createEffects()
	{
		onGPSMessageTimeEffect = new SysMLEffect(context, onGPSMessageTimeEffectAction, "onGPSMessageTimeEffect");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(context, initialState, initializingState, "IntialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(context, initializingState, operationalState, "IntializingToOperational");
		operationalOnGPSMessageTimeTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLTimeEvent.class), Optional.of(isGPSMessageTimeEventGuard), Optional.of(onGPSMessageTimeEffect),
			"OperationalOnGPSMessageTime", SysMLTransitionKind.internal);
		operationalToFinalTransition = new FinalTransition(context, operationalState, finalState, "OperationalToFinal");
	}

	@Override
	protected void createTransitionsUtility()
	{
		transitionsUtility = Optional.of(new StateTransitionsTransmitters(Optional.of(new StateTransitionTablesTransmitter(StateTransitionsDisplay.udpPort, false)), Optional.empty(), false));
	}
}
