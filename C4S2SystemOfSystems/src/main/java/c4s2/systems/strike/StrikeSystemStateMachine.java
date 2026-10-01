package c4s2.systems.strike;

import java.util.Optional;

import c4s2.common.attributetypes.StrikeSystemStatesEnum;
import c4s2.common.items.information.StrikeSystemControl;
import c4s2.common.items.information.TargetMonitor;
import c4s2.common.messages.StrikeSystemControlMessage;
import c4s2.common.messages.TargetMonitorMessage;
import sysmlinjava.attributetypes.DurationMilliseconds;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.EntryActionFunction;
import sysmlinjava.javaannotations.statemachines.ExitActionFunction;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.InitialTransition;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLEntryActionFunction;
import sysmlinjava.states.SysMLExitActionFunction;
import sysmlinjava.states.SysMLGuard;
import sysmlinjava.states.SysMLGuardCondition;
import sysmlinjava.states.SysMLState;
import sysmlinjava.states.SysMLStateMachine;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;
import sysmlinjavalibrary.common.messages.Message;
import sysmlinjavalibrary.common.signals.MessageSignal;

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

	@EntryActionFunction
	public SysMLEntryActionFunction initializingStateOnEnterAction;
	@EntryActionFunction
	public SysMLEntryActionFunction standingByStateOnEnterAction;
	@EntryActionFunction
	public SysMLEntryActionFunction strikingStateOnEnterAction;
	@EntryActionFunction
	public SysMLEntryActionFunction returningStateOnEnterAction;
	@EntryActionFunction
	public SysMLEntryActionFunction returnedStateOnEnterAction;

	@ExitActionFunction
	public SysMLExitActionFunction returnedStateOnExitAction;

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
	public SysMLGuardCondition isNotReturnedGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isReturnedGuardCondition;
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

	@EffectActionFunction
	public SysMLEffectActionFunction initializingToStandingByTransitionEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction standingByToStrikingTransitionEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction strikingOnTargetMonitorTransitionEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction strikingToReturningTransitionEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction returningOnReturningMonitorTimeEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction returningToReturnedEffectAction;

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
	protected void createStateEntryActionFunctions()
	{
		super.createStateEntryActionFunctions();
		initializingStateOnEnterAction = (context) ->
		{
			StrikeSystem system = (StrikeSystem)context.get();
			system.initialize();
		};
		standingByStateOnEnterAction = (context) ->
		{
			StrikeSystem system = (StrikeSystem)context.get();
			system.standby();
		};
		strikingStateOnEnterAction = (context) ->
		{
			StrikeSystem system = (StrikeSystem)context.get();
			system.departFromBase();
		};
		returningStateOnEnterAction = (context) ->
		{
			StrikeSystem system = (StrikeSystem)context.get();
			system.returnToBase();
			startTimer(new SysMLTimeEvent(strikeReturnMonitorTimer, DurationMilliseconds.ofSeconds(2), Optional.of(DurationMilliseconds.ofSeconds(2))));
		};
		returnedStateOnEnterAction = (context) ->
		{
			stopTimer(strikeReturnMonitorTimer);
			StrikeSystem system = (StrikeSystem)context.get();
			system.arriveAtBase();
		};
	}

	@Override
	protected void createStateExitActionFunctions()
	{
		super.createStateExitActionFunctions();
		returnedStateOnExitAction = (context) ->
		{
			StrikeSystem system = (StrikeSystem)context.get();
			system.detach();
		};
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(context, Optional.of(initializingStateOnEnterAction), Optional.empty(), Optional.empty(), "Initializing");
		standingByState = new SysMLState(context, Optional.of(standingByStateOnEnterAction), Optional.empty(), Optional.empty(), "StandingBy");
		strikingState = new SysMLState(context, Optional.of(strikingStateOnEnterAction), Optional.empty(), Optional.empty(), "Striking");
		returningState = new SysMLState(context, Optional.of(returningStateOnEnterAction), Optional.empty(), Optional.empty(), "Returning");
		returnedState = new SysMLState(context, Optional.of(returnedStateOnEnterAction), Optional.empty(), Optional.of(returnedStateOnExitAction), "Returned");
	}

	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isControlToStandbyGuardCondition = (event, context) ->
		{
			boolean result = false;
			if (event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof MessageSignal)
			{
				Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
				result = message instanceof StrikeSystemControlMessage controlMessage && controlMessage.control.state == StrikeSystemStatesEnum.standingby;
			}
			return result;
		};
		isControlToStrikeGuardCondition = (event, context) ->
		{
			boolean result = false;
			if (event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof MessageSignal)
			{
				Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
				result = message instanceof StrikeSystemControlMessage controlMessage && controlMessage.control.state == StrikeSystemStatesEnum.striking;
			}
			return result;
		};
		isActionableTargetMonitorGuardCondition = (event, context) ->
		{
			boolean result = false;
			if (event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof MessageSignal)
			{
				Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
				StrikeSystem system = (StrikeSystem)context.get();
				boolean isTargetMonitor = message instanceof TargetMonitorMessage;
				boolean wasTargetEngaged = system.currentMonitor.confirmTargetEngaged.isPresent() && system.currentMonitor.confirmTargetEngaged.get().isTrue();
				result = !wasTargetEngaged && isTargetMonitor;
			}
			return result;
		};
		isControlToReturnGuardCondition = (event, context) ->
		{
			boolean result = false;
			if (event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof MessageSignal)
			{
				Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
				result = message instanceof StrikeSystemControlMessage controlMessage && controlMessage.control.state == StrikeSystemStatesEnum.returned;
			}
			return result;
		};
		isNotReturnedGuardCondition = (event, context) ->
		{
			return !((StrikeSystem)context.get()).isReturnedToBase();
		};
		isReturnedGuardCondition = (event, context) ->
		{
			return ((StrikeSystem)context.get()).isReturnedToBase();
		};
		isControlToDetachGuardCondition = (event, context) ->
		{
			boolean result = false;
			if (event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof MessageSignal)
			{
				Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
				result = message instanceof StrikeSystemControlMessage controlMessage && controlMessage.control.state == StrikeSystemStatesEnum.detached;
			}
			return result;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isControlToStandbyGuard = new SysMLGuard(context, isControlToStandbyGuardCondition, "IsControlToStandby");
		isControlToStrikeGuard = new SysMLGuard(context, isControlToStrikeGuardCondition, "IsControlToStrike");
		isActionableTargetMonitorGuard = new SysMLGuard(context, isActionableTargetMonitorGuardCondition, "IsActionableTargetMonitor");
		isControlToReturnGuard = new SysMLGuard(context, isControlToReturnGuardCondition, "IsControlToReturn");
		isNotReturnedGuard = new SysMLGuard(context, isNotReturnedGuardCondition, "IsNotReturned");
		isReturnedGuard = new SysMLGuard(context, isReturnedGuardCondition, "IsReturned");
		isControlToDetachGuard = new SysMLGuard(context, isControlToDetachGuardCondition, "IsControlToDetach");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		initializingToStandingByTransitionEffectAction = (event, context) ->
		{
			StrikeSystem system = (StrikeSystem)context.get();
			Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
			StrikeSystemControl control = ((StrikeSystemControlMessage)message).control;
			system.onInitializingToStandingByControl(control);
		};
		standingByToStrikingTransitionEffectAction = (event, context) ->
		{
			StrikeSystem system = (StrikeSystem)context.get();
			Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
			StrikeSystemControl control = ((StrikeSystemControlMessage)message).control;
			system.onStandingByToStrikingControl(control);
		};
		strikingOnTargetMonitorTransitionEffectAction = (event, context) ->
		{
			StrikeSystem system = (StrikeSystem)context.get();
			Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
			TargetMonitor targetMonitor = ((TargetMonitorMessage)message).monitor;
			system.onTargetMonitorDuringStrike(targetMonitor);
		};
		strikingToReturningTransitionEffectAction = (event, context) ->
		{
			StrikeSystem system = (StrikeSystem)context.get();
			Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
			StrikeSystemControl control = ((StrikeSystemControlMessage)message).control;
			system.onStrikingToReturningControl(control);
		};
		returningOnReturningMonitorTimeEffectAction = (event, context) ->
		{
			StrikeSystem system = (StrikeSystem)context.get();
			system.onReturningMonitorTime();
		};
		returningToReturnedEffectAction = (event, context) ->
		{
			StrikeSystem system = (StrikeSystem)context.get();
			system.onReturnedToBase();
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		initializingToStandingByTransitionEffect = new SysMLEffect(context, initializingToStandingByTransitionEffectAction, "InitializingToStandingBy");
		standingByToStrikingTransitionEffect = new SysMLEffect(context, standingByToStrikingTransitionEffectAction, "StandingByToStriking");
		strikingOnTargetMonitorTransitionEffect = new SysMLEffect(context, strikingOnTargetMonitorTransitionEffectAction, "StrikingOnTargetMonitor");
		strikingToReturningTransitionEffect = new SysMLEffect(context, strikingToReturningTransitionEffectAction, "StrikingToReturning");
		returningOnReturningMonitorTimeEffect = new SysMLEffect(context, returningOnReturningMonitorTimeEffectAction, "ReturningOnMonitorTime");
		returningToReturnedEffect = new SysMLEffect(context, returningToReturnedEffectAction, "ReturningToReturned");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(context, initialState, initializingState, "InitialToInitializing");
		initializingToStandbyTransition = new SysMLTransition(context, initializingState, standingByState, Optional.of(SysMLSignalEvent.class), Optional.of(isControlToStandbyGuard),
			Optional.of(initializingToStandingByTransitionEffect), "InitializingToStandingBy", SysMLTransitionKind.external);
		standingByToStrikingTransition = new SysMLTransition(context, standingByState, strikingState, Optional.of(SysMLSignalEvent.class), Optional.of(isControlToStrikeGuard), Optional.of(standingByToStrikingTransitionEffect),
			"StandingByToStriking", SysMLTransitionKind.external);
		strikingOnTargetMonitorTransition = new SysMLTransition(context, strikingState, strikingState, Optional.of(SysMLSignalEvent.class), Optional.of(isActionableTargetMonitorGuard),
			Optional.of(strikingOnTargetMonitorTransitionEffect), "StrikingOnTargetMonitor", SysMLTransitionKind.internal);
		strikingToReturningTransition = new SysMLTransition(context, strikingState, returningState, Optional.of(SysMLSignalEvent.class), Optional.of(isControlToReturnGuard), Optional.of(strikingToReturningTransitionEffect),
			"StrikingToReturning", SysMLTransitionKind.external);
		returningOnMonitorTimeTransition = new SysMLTransition(context, returningState, returningState, Optional.of(SysMLTimeEvent.class), Optional.of(isNotReturnedGuard), Optional.of(returningOnReturningMonitorTimeEffect),
			"ReturningOnMonitorTime", SysMLTransitionKind.internal);
		returningToReturnedTransition = new SysMLTransition(context, returningState, returnedState, Optional.of(SysMLTimeEvent.class), Optional.of(isReturnedGuard), Optional.of(returningToReturnedEffect), "ReturningToReturned",
			SysMLTransitionKind.external);
		returnedToFinalTransition = new SysMLTransition(context, returnedState, finalState, Optional.of(SysMLSignalEvent.class), Optional.of(isControlToDetachGuard), Optional.empty(), "ReturnedToFinal", SysMLTransitionKind.external);
	}
}
