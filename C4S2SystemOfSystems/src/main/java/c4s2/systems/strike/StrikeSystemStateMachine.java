package c4s2.systems.strike;

import java.util.Optional;
import c4s2.common.messages.StrikeSystemControlMessage;
import c4s2.common.messages.TargetMonitorMessage;
import c4s2.common.objects.information.StrikeSystemControl;
import c4s2.common.objects.information.TargetMonitor;
import c4s2.common.valueTypes.StrikeSystemStatesEnum;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Guard;
import sysmlinjava.annotations.statemachines.GuardCondition;
import sysmlinjava.annotations.statemachines.OnEnterActivity;
import sysmlinjava.annotations.statemachines.OnExitActivity;
import sysmlinjava.annotations.statemachines.State;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.statemachine.InitialTransition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLGuard;
import sysmlinjava.statemachine.SysMLGuardCondition;
import sysmlinjava.statemachine.SysMLOnEnterActivity;
import sysmlinjava.statemachine.SysMLOnExitActivity;
import sysmlinjava.statemachine.SysMLState;
import sysmlinjava.statemachine.SysMLStateMachine;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjava.valuetypes.DurationMilliseconds;
import sysmlinjavalibrary.common.events.MessageEvent;
import sysmlinjavalibrary.common.messages.Message;

/**
 * The {@code StrikeSystemStateMachine} is the SysMLinJava model of the state
 * machine for a strike system that participates in the C4S2 operation. The
 * state machine has 5 states - initializing, standing by, striking from base,
 * returning to base, and returned to base. See the state machine's states and
 * transitions for definition of this behavior.
 * 
 * @author ModelerOne
 *
 *         Known users:
 * @see StrikeSystem
 */
public class StrikeSystemStateMachine extends SysMLStateMachine
{
	@State
	public SysMLState initializingState;
	@State
	public SysMLState standingByState;
	@State
	public SysMLState strikingState;
	@State
	public SysMLState returningState;
	@State
	public SysMLState returnedState;

	@OnEnterActivity
	public SysMLOnEnterActivity initializingStateOnEnterActivity;
	@OnEnterActivity
	public SysMLOnEnterActivity standingByStateOnEnterActivity;
	@OnEnterActivity
	public SysMLOnEnterActivity strikingStateOnEnterActivity;
	@OnEnterActivity
	public SysMLOnEnterActivity returningStateOnEnterActivity;
	@OnEnterActivity
	public SysMLOnEnterActivity returnedStateOnEnterActivity;

	@OnExitActivity
	public SysMLOnExitActivity returnedStateOnExitActivity;

	@Transition
	public InitialTransition initialToInitializingTransition;
	@Transition
	public SysMLTransition initializingToStandbyTransition;
	@Transition
	public SysMLTransition standingByToStrikingTransition;
	@Transition
	public SysMLTransition strikingOnTargetMonitorTransition;
	@Transition
	public SysMLTransition strikingToReturningTransition;
	@Transition
	public SysMLTransition returningOnMonitorTimeTransition;
	@Transition
	public SysMLTransition returningToReturnedTransition;
	@Transition
	public SysMLTransition returnedToFinalTransition;

	@GuardCondition
	public SysMLGuardCondition isControlToStandbyGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isControlToStrikeGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isActionableTargetMonitorGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isControlToReturnGuardCondition;
	@GuardCondition
	public SysMLGuardCondition 	isNotReturnedGuardCondition;
	@GuardCondition
	public SysMLGuardCondition 	isReturnedGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isControlToDetachGuardCondition;

	@Guard
	public SysMLGuard isControlToStandbyGuard;
	@Guard
	public SysMLGuard isControlToStrikeGuard;
	@Guard
	public SysMLGuard isActionableTargetMonitorGuard;
	@Guard
	public SysMLGuard isControlToReturnGuard;
	@Guard
	public SysMLGuard isNotReturnedGuard;
	@Guard
	public SysMLGuard isReturnedGuard;
	@Guard
	public SysMLGuard isControlToDetachGuard;

	@EffectActivity
	public SysMLEffectActivity initializingToStandingByTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity standingByToStrikingTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity strikingOnTargetMonitorTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity strikingToReturningTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity returningOnReturningMonitorTimeEffectActivity;
	@EffectActivity
	public SysMLEffectActivity returningToReturnedEffectActivity;

	@Effect
	public SysMLEffect initializingToStandingByTransitionEffect;
	@Effect
	public SysMLEffect standingByToStrikingTransitionEffect;
	@Effect
	public SysMLEffect strikingOnTargetMonitorTransitionEffect;
	@Effect
	public SysMLEffect strikingToReturningTransitionEffect;
	@Effect
	public SysMLEffect returningOnReturningMonitorTimeEffect;
	@Effect
	public SysMLEffect returningToReturnedEffect;

	public StrikeSystemStateMachine(StrikeSystem strikeSystem)
	{
		super(Optional.of(strikeSystem), true, "StrikeSystemStateMachine");
	}

	static final String strikeReturnMonitorTimer = "strikeReturnMonitorTimer";

	@Override
	protected void createStateOnEnterActivities()
	{
		super.createStateOnEnterActivities();
		initializingStateOnEnterActivity = (contextBlock) ->
		{
			StrikeSystem system = (StrikeSystem)contextBlock.get();
			system.initialize();
		};
		standingByStateOnEnterActivity = (contextBlock) ->
		{
			StrikeSystem system = (StrikeSystem)contextBlock.get();
			system.standby();
		};
		strikingStateOnEnterActivity = (contextBlock) ->
		{
			StrikeSystem system = (StrikeSystem)contextBlock.get();
			system.departFromBase();
		};
		returningStateOnEnterActivity = (contextBlock) ->
		{
			StrikeSystem system = (StrikeSystem)contextBlock.get();
			system.returnToBase();
			startTimer(new SysMLTimeEvent(strikeReturnMonitorTimer, DurationMilliseconds.ofSeconds(2), Optional.of(DurationMilliseconds.ofSeconds(2))));
		};
		returnedStateOnEnterActivity = (contextBlock) ->
		{
			stopTimer(strikeReturnMonitorTimer);
			StrikeSystem system = (StrikeSystem)contextBlock.get();
			system.arriveAtBase();
		};
	}

	@Override
	protected void createStateOnExitActivities()
	{
		super.createStateOnExitActivities();
		returnedStateOnExitActivity = (contextBlock) ->
		{
			StrikeSystem system = (StrikeSystem)contextBlock.get();
			system.detach();
		};
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(contextBlock, Optional.of(initializingStateOnEnterActivity), Optional.empty(), Optional.empty(), "Initializing");
		standingByState = new SysMLState(contextBlock, Optional.of(standingByStateOnEnterActivity), Optional.empty(), Optional.empty(), "StandingBy");
		strikingState = new SysMLState(contextBlock, Optional.of(strikingStateOnEnterActivity), Optional.empty(), Optional.empty(), "Striking");
		returningState = new SysMLState(contextBlock, Optional.of(returningStateOnEnterActivity), Optional.empty(), Optional.empty(), "Returning");
		returnedState = new SysMLState(contextBlock, Optional.of(returnedStateOnEnterActivity), Optional.empty(), Optional.of(returnedStateOnExitActivity), "Returned");
	}

	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isControlToStandbyGuardCondition = (event, contextBlock) ->
		{
			Message message = ((MessageEvent)event.get()).getMessage();
			return message instanceof StrikeSystemControlMessage controlMessage && controlMessage.control.state == StrikeSystemStatesEnum.standingby;
		};
		isControlToStrikeGuardCondition = (event, contextBlock) ->
		{
			Message message = ((MessageEvent)event.get()).getMessage();
			return message instanceof StrikeSystemControlMessage controlMessage && controlMessage.control.state == StrikeSystemStatesEnum.striking;
		};
		isActionableTargetMonitorGuardCondition = (event, contextBlock) ->
		{
			Message message = ((MessageEvent)event.get()).getMessage();
			StrikeSystem system = (StrikeSystem)contextBlock.get();
			boolean isTargetMonitor = message instanceof TargetMonitorMessage;
			boolean wasTargetEngaged =
				system.currentMonitor.confirmTargetEngaged.isPresent() &&
				system.currentMonitor.confirmTargetEngaged.get().isTrue();
			return !wasTargetEngaged && isTargetMonitor;
		};
		isControlToReturnGuardCondition = (event, contextBlock) ->
		{
			Message message = ((MessageEvent)event.get()).getMessage();
			return message instanceof StrikeSystemControlMessage controlMessage && controlMessage.control.state == StrikeSystemStatesEnum.returned;
		};
		isNotReturnedGuardCondition = (event, contextBlock) ->
		{
			return !((StrikeSystem)contextBlock.get()).isReturnedToBase();
		};
		isReturnedGuardCondition = (event, contextBlock) ->
		{
			return ((StrikeSystem)contextBlock.get()).isReturnedToBase();
		};
		isControlToDetachGuardCondition = (event, contextBlock) ->
		{
			Message message = ((MessageEvent)event.get()).getMessage();
			return message instanceof StrikeSystemControlMessage controlMessage && controlMessage.control.state == StrikeSystemStatesEnum.detached;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isControlToStandbyGuard = new SysMLGuard(contextBlock, isControlToStandbyGuardCondition, "IsControlToStandby");
		isControlToStrikeGuard = new SysMLGuard(contextBlock, isControlToStrikeGuardCondition, "IsControlToStrike");
		isActionableTargetMonitorGuard = new SysMLGuard(contextBlock, isActionableTargetMonitorGuardCondition, "IsActionableTargetMonitor");
		isControlToReturnGuard = new SysMLGuard(contextBlock, isControlToReturnGuardCondition, "IsControlToReturn");
		isNotReturnedGuard = new SysMLGuard(contextBlock, isNotReturnedGuardCondition, "IsNotReturned");
		isReturnedGuard = new SysMLGuard(contextBlock, isReturnedGuardCondition, "IsReturned");
		isControlToDetachGuard = new SysMLGuard(contextBlock, isControlToDetachGuardCondition, "IsControlToDetach");
	}

	@Override
	protected void createEffectActivities()
	{
		super.createEffectActivities();
		initializingToStandingByTransitionEffectActivity = (event, contextBlock) ->
		{
			StrikeSystem system = (StrikeSystem)contextBlock.get();
			Message message = ((MessageEvent)event.get()).getMessage();
			StrikeSystemControl control = ((StrikeSystemControlMessage)message).control;
			system.onInitializingToStandingByControl(control);
		};
		standingByToStrikingTransitionEffectActivity = (event, contextBlock) ->
		{
			StrikeSystem system = (StrikeSystem)contextBlock.get();
			Message message = ((MessageEvent)event.get()).getMessage();
			StrikeSystemControl control = ((StrikeSystemControlMessage)message).control;
			system.onStandingByToStrikingControl(control);
		};
		strikingOnTargetMonitorTransitionEffectActivity = (event, contextBlock) ->
		{
			StrikeSystem system = (StrikeSystem)contextBlock.get();
			Message message = ((MessageEvent)event.get()).getMessage();
			TargetMonitor targetMonitor = ((TargetMonitorMessage)message).monitor;
			system.onTargetMonitorDuringStrike(targetMonitor);
		};
		strikingToReturningTransitionEffectActivity = (event, contextBlock) ->
		{
			StrikeSystem system = (StrikeSystem)contextBlock.get();
			Message message = ((MessageEvent)event.get()).getMessage();
			StrikeSystemControl control = ((StrikeSystemControlMessage)message).control;
			system.onStrikingToReturningControl(control);
		};
		returningOnReturningMonitorTimeEffectActivity = (event, contextBlock) ->
		{
			StrikeSystem system = (StrikeSystem)contextBlock.get();
			system.onReturningMonitorTime();
		};
		returningToReturnedEffectActivity = (event, contextBlock) ->
		{
			StrikeSystem system = (StrikeSystem)contextBlock.get();
			system.onReturnedToBase();
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		initializingToStandingByTransitionEffect = new SysMLEffect(contextBlock, initializingToStandingByTransitionEffectActivity, "InitializingToStandingBy");
		standingByToStrikingTransitionEffect = new SysMLEffect(contextBlock, standingByToStrikingTransitionEffectActivity, "StandingByToStriking");
		strikingOnTargetMonitorTransitionEffect = new SysMLEffect(contextBlock, strikingOnTargetMonitorTransitionEffectActivity, "StrikingOnTargetMonitor");
		strikingToReturningTransitionEffect = new SysMLEffect(contextBlock, strikingToReturningTransitionEffectActivity, "StrikingToReturning");
		returningOnReturningMonitorTimeEffect = new SysMLEffect(contextBlock, returningOnReturningMonitorTimeEffectActivity, "ReturningOnMonitorTime");
		returningToReturnedEffect = new SysMLEffect(contextBlock, returningToReturnedEffectActivity, "ReturningToReturned");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(contextBlock, initialState, initializingState, "InitialToInitializing");
		initializingToStandbyTransition = new SysMLTransition(contextBlock, initializingState, standingByState, Optional.of(MessageEvent.class), Optional.of(isControlToStandbyGuard), Optional.of(initializingToStandingByTransitionEffect),
			"InitializingToStandingBy", SysMLTransitionKind.external);
		standingByToStrikingTransition = new SysMLTransition(contextBlock, standingByState, strikingState, Optional.of(MessageEvent.class), Optional.of(isControlToStrikeGuard), Optional.of(standingByToStrikingTransitionEffect),
			"StandingByToStriking", SysMLTransitionKind.external);
		strikingOnTargetMonitorTransition = new SysMLTransition(contextBlock, strikingState, strikingState, Optional.of(MessageEvent.class), Optional.of(isActionableTargetMonitorGuard), Optional.of(strikingOnTargetMonitorTransitionEffect),
			"StrikingOnTargetMonitor", SysMLTransitionKind.internal);
		strikingToReturningTransition = new SysMLTransition(contextBlock, strikingState, returningState, Optional.of(MessageEvent.class), Optional.of(isControlToReturnGuard), Optional.of(strikingToReturningTransitionEffect),
			"StrikingToReturning", SysMLTransitionKind.external);
		returningOnMonitorTimeTransition = new SysMLTransition(contextBlock, returningState, returningState, Optional.of(SysMLTimeEvent.class), Optional.of(isNotReturnedGuard), Optional.of(returningOnReturningMonitorTimeEffect), "ReturningOnMonitorTime",
			SysMLTransitionKind.internal);
		returningToReturnedTransition = new SysMLTransition(contextBlock, returningState, returnedState, Optional.of(SysMLTimeEvent.class), Optional.of(isReturnedGuard), Optional.of(returningToReturnedEffect), "ReturningToReturned",
			SysMLTransitionKind.external);
		returnedToFinalTransition = new SysMLTransition(contextBlock, returnedState, finalState, Optional.of(MessageEvent.class), Optional.of(isControlToDetachGuard), Optional.empty(), "ReturnedToFinal", SysMLTransitionKind.external);
	}
}
