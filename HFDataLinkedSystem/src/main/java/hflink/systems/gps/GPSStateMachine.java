package hflink.systems.gps;

import java.time.LocalTime;
import java.util.Optional;
import sysmlinjava.analysis.statetransitions.StateTransitionTablesTransmitter;
import sysmlinjava.analysis.statetransitions.StateTransitionsDisplay;
import sysmlinjava.analysis.statetransitionstransmitters.StateTransitionsTransmitters;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Guard;
import sysmlinjava.annotations.statemachines.GuardCondition;
import sysmlinjava.annotations.statemachines.State;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.statemachine.FinalTransition;
import sysmlinjava.statemachine.InitialTransition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLGuard;
import sysmlinjava.statemachine.SysMLGuardCondition;
import sysmlinjava.statemachine.SysMLState;
import sysmlinjava.statemachine.SysMLStateMachine;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;

/**
 * State machine for the GPS model/simulation. The state machine consists of
 * states to initialize and operate, with the key transition being the one that
 * occurs at the time to transmit a GPS time message. See the model of the state
 * machine below for more detail.
 * 
 * @author ModelerOne
 *
 */
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

	@EffectActivity
	private SysMLEffectActivity onGPSMessageTimeEffectActivity;
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
		initializingState = new SysMLState(contextBlock, "Initializing");
		operationalState = new SysMLState(contextBlock, "Operational");
	}

	@Override
	protected void createGuardConditions()
	{
		isGPSMessageTimeEventGuardCondition = (currentEvent, contextBlock) ->
		{
			boolean result = false;
			if (currentEvent.get() instanceof SysMLTimeEvent)
			{
				SysMLTimeEvent timeEvent = (SysMLTimeEvent)currentEvent.get();
				if (timeEvent.timerID.equals(GPS.timerID))
					result = true;
			}
			return result;
		};
	}

	@Override
	protected void createGuards()
	{
		isGPSMessageTimeEventGuard = new SysMLGuard(contextBlock, isGPSMessageTimeEventGuardCondition, "isGPSMessageTime");
	}

	@Override
	protected void createEffectActivities()
	{
		onGPSMessageTimeEffectActivity = (event, contextBlock) ->
		{
			GPS gps = (GPS)contextBlock.get();
			gps.onGPSMessageTime(LocalTime.now());
		};
	}

	@Override
	protected void createEffects()
	{
		onGPSMessageTimeEffect = new SysMLEffect(contextBlock, onGPSMessageTimeEffectActivity, "onGPSMessageTimeEffect");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(contextBlock, initialState, initializingState, "IntialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(contextBlock, initializingState, operationalState, "IntializingToOperational");
		operationalOnGPSMessageTimeTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(SysMLTimeEvent.class), Optional.of(isGPSMessageTimeEventGuard), Optional.of(onGPSMessageTimeEffect),
			"OperationalOnGPSMessageTime", SysMLTransitionKind.internal);
		operationalToFinalTransition = new FinalTransition(contextBlock, operationalState, finalState, "OperationalToFinal");
	}

	@Override
	protected void createTransitionsUtility()
	{
		transitionsUtility = Optional.of(new StateTransitionsTransmitters(Optional.of(new StateTransitionTablesTransmitter(StateTransitionsDisplay.udpPort, false)), Optional.empty()));
	}
}
