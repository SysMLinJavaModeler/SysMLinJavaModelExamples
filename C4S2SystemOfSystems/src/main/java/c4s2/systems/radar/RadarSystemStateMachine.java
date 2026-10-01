
package c4s2.systems.radar;

import java.util.Optional;

import c4s2.common.attributetypes.RadarSystemStatesEnum;
import c4s2.common.items.information.RadarSignalReturn;
import c4s2.common.items.information.RadarSystemControl;
import c4s2.common.messages.RadarSystemControlMessage;
import c4s2.common.signals.RadarSignalReturnSignal;
import sysmlinjava.attributetypes.DurationMilliseconds;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.EntryActionFunction;
import sysmlinjava.javaannotations.statemachines.ExitActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
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
 * The {@code RadarSystemStateMachine} is the SysMLinJava model of the state
 * machine for a radar system that participates in the C4S2 operation. The state
 * machine has 4 states - initializing, idle, F2T2 scanning, and EA scanning.
 * See the state machine's states and transitions for definition of this
 * behavior.
 * 
 * @author ModelerOne
 *
 * @see c4s2.systems.radar.RadarSystem
 */
public class RadarSystemStateMachine extends SysMLStateMachine
{
	@State
	public SysMLState initializingState;
	@State
	public SysMLState idleState;
	@State
	public SysMLState f2t2ScanningState;
	@State
	public SysMLState eaScanningState;

	@Transition
	public InitialTransition initialToInitializingTransition;
	@Transition
	public SysMLTransition initializingToIdleTransition;
	@Transition
	public SysMLTransition idleToF2T2ScanningTransition;
	@Transition
	public SysMLTransition idleToEAScanningTransition;
	@Transition
	public SysMLTransition f2t2ScanningToIdleTransition;
	@Transition
	public SysMLTransition eaScanningToIdleTransition;
	@Transition
	public SysMLTransition f2t2ScanningToEAScanningTransition;
	@Transition
	public SysMLTransition f2t2ScanTimeTransition;
	@Transition
	public SysMLTransition eaScanTimeTransition;
	@Transition
	public SysMLTransition f2t2ScanningRadarSignalReturnTransition;
	@Transition
	public SysMLTransition eaScanningRadarSignalReturnTransition;
	@Transition
	public SysMLTransition idleToFinalTransition;

	@EntryActionFunction
	public SysMLEntryActionFunction initializingStateOnEnterAction;
	@EntryActionFunction
	public SysMLEntryActionFunction f2t2ScanningStateOnEnterAction;
	@EntryActionFunction
	public SysMLEntryActionFunction eaScanningStateOnEnterAction;

	@ExitActionFunction
	public SysMLExitActionFunction f2t2ScanningStateOnExitAction;
	@ExitActionFunction
	public SysMLExitActionFunction eaScanningStateOnExitAction;

	@GuardCondition
	public SysMLGuardCondition isControlToIdleStateGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isControlToF2T2ScanningStateGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isControlToEAScanningStateGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isControlToFinalStateGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isF2T2ScanTimeGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isEAScanTimeGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isRadarSignalReturnGuardCondition;

	@Guard
	public SysMLGuard isControlToIdleStateGuard;
	@Guard
	public SysMLGuard isControlToF2T2ScanningStateGuard;
	@Guard
	public SysMLGuard isControlToEAScanningStateGuard;
	@Guard
	public SysMLGuard isControlToFinalStateGuard;
	@Guard
	public SysMLGuard isF2T2ScanTimeGuard;
	@Guard
	public SysMLGuard isEAScanTimeGuard;
	@Guard
	public SysMLGuard isRadarSignalReturnGuard;

	@EffectActionFunction
	public SysMLEffectActionFunction idleToF2T2ScanningTransitionEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction idleToEAScanningTransitionEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction idleToFinalTransitionEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction f2t2ScanningToIdleTransitionEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction eaScanningToIdleTransitionEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction f2t2ScanningToEAScanningTransitionEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction f2t2ScanTimeTransitionEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction eaScanTimeTransitionEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction f2t2ScanRadarSignalReturnEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction eaScanRadarSignalReturnEffectAction;
	@EffectActionFunction
	private SysMLEffectActionFunction initializingToIdleTransitionEffectAction;

	@Effect
	public SysMLEffect idleToF2T2ScanningTransitionEffect;
	@Effect
	public SysMLEffect idleToEAScanningTransitionEffect;
	@Effect
	public SysMLEffect idleToFinalTransitionEffect;
	@Effect
	public SysMLEffect f2t2ScanningToIdleTransitionEffect;
	@Effect
	public SysMLEffect eaScanningToIdleTransitionEffect;
	@Effect
	public SysMLEffect f2t2ScanningToEAScanningTransitionEffect;
	@Effect
	public SysMLEffect f2t2ScanTimeTransitionEffect;
	@Effect
	public SysMLEffect eaScanTimeTransitionEffect;
	@Effect
	public SysMLEffect f2t2ScanRadarSignalReturnEffect;
	@Effect
	public SysMLEffect eaScanRadarSignalReturnEffect;
	@Effect
	private SysMLEffect initializingToIdleTransitionEffect;

	public RadarSystemStateMachine(RadarSystem radarSystem)
	{
		super(Optional.of(radarSystem), true, "RadarSystemStateMachine");
	}

	@Override
	protected void createStateEntryActionFunctions()
	{
		super.createStateEntryActionFunctions();
		initializingStateOnEnterAction = (context) ->
		{
			RadarSystem radarSystem = (RadarSystem)context.get();
			radarSystem.initialize();
		};
		f2t2ScanningStateOnEnterAction = (context) ->
		{
			RadarSystem radarSystem = (RadarSystem)context.get();
			radarSystem.startF2T2Scanning();
			startTimer(RadarSystem.f2t2ScanTimerID, DurationMilliseconds.ofSeconds(2), ((RadarSystem)context.get()).f2t2ScanIntervalDuration);
		};
		eaScanningStateOnEnterAction = (context) ->
		{
			RadarSystem radarSystem = (RadarSystem)context.get();
			radarSystem.startEAScanning();
			startTimer(RadarSystem.eaScanTimerID, DurationMilliseconds.ofSeconds(2), ((RadarSystem)context.get()).eaScanIntervalDuration);
		};
	}

	@Override
	protected void createStateExitActionFunctions()
	{
		super.createStateExitActionFunctions();
		f2t2ScanningStateOnExitAction = (context) ->
		{
			RadarSystem radarSystem = (RadarSystem)context.get();
			radarSystem.stopF2T2Scanning();
			stopTimer(RadarSystem.f2t2ScanTimerID);
		};
		eaScanningStateOnExitAction = (context) ->
		{
			RadarSystem radarSystem = (RadarSystem)context.get();
			radarSystem.stopEAScanning();
			stopTimer(RadarSystem.eaScanTimerID);
		};
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(context, Optional.of(initializingStateOnEnterAction), Optional.empty(), Optional.empty(), "Initializing");
		idleState = new SysMLState(context, Optional.empty(), Optional.empty(), Optional.empty(), "Idle");
		f2t2ScanningState = new SysMLState(context, Optional.of(f2t2ScanningStateOnEnterAction), Optional.empty(), Optional.of(f2t2ScanningStateOnExitAction), "F2T2Scanning");
		eaScanningState = new SysMLState(context, Optional.of(eaScanningStateOnEnterAction), Optional.empty(), Optional.of(eaScanningStateOnExitAction), "EAScanning");
	}

	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isControlToIdleStateGuardCondition = (event, context) ->
		{
			boolean result = false;
			if (event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof MessageSignal)
			{
				Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
				RadarSystemControl control = ((RadarSystemControlMessage)message).control;
				result = control.state == RadarSystemStatesEnum.Idle;
			}
			return result;
		};
		isControlToF2T2ScanningStateGuardCondition = (event, context) ->
		{
			boolean result = false;
			if (event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof MessageSignal)
			{
				Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
				RadarSystemControl control = ((RadarSystemControlMessage)message).control;
				result = control.state == RadarSystemStatesEnum.F2T2Scanning;
			}
			return result;
		};
		isControlToEAScanningStateGuardCondition = (event, context) ->
		{
			boolean result = false;
			if (event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof MessageSignal)
			{
				Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
				RadarSystemControl control = ((RadarSystemControlMessage)message).control;
				result = control.state == RadarSystemStatesEnum.EAScanning;
			}
			return result;
		};
		isControlToFinalStateGuardCondition = (event, context) ->
		{
			boolean result = false;
			if (event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof MessageSignal)
			{
				Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
				RadarSystemControl control = ((RadarSystemControlMessage)message).control;
				result = control.state == RadarSystemStatesEnum.Detached;
			}
			return result;
		};
		isF2T2ScanTimeGuardCondition = (event, context) ->
		{
			return event.isPresent() && event.get() instanceof SysMLTimeEvent && ((SysMLTimeEvent)event.get()).timerID.equals(RadarSystem.f2t2ScanTimerID);
		};
		isEAScanTimeGuardCondition = (event, context) ->
		{
			return event.isPresent() && event.get() instanceof SysMLTimeEvent && ((SysMLTimeEvent)event.get()).timerID.equals(RadarSystem.eaScanTimerID);
		};
		isRadarSignalReturnGuardCondition = (event, context) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof RadarSignalReturnSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isControlToIdleStateGuard = new SysMLGuard(context, isControlToIdleStateGuardCondition, "IsControlToIdleState");
		isControlToF2T2ScanningStateGuard = new SysMLGuard(context, isControlToF2T2ScanningStateGuardCondition, "IsControlToF2T2ScanningState");
		isControlToEAScanningStateGuard = new SysMLGuard(context, isControlToEAScanningStateGuardCondition, "IsControlToEAScannineState");
		isControlToFinalStateGuard = new SysMLGuard(context, isControlToFinalStateGuardCondition, "IsControlToFinalState");
		isF2T2ScanTimeGuard = new SysMLGuard(context, isF2T2ScanTimeGuardCondition, "IsF2T2ScanTime");
		isEAScanTimeGuard = new SysMLGuard(context, isEAScanTimeGuardCondition, "IsEAScanTime");
		isRadarSignalReturnGuard = new SysMLGuard(context, isRadarSignalReturnGuardCondition, "IsRadarSignalReturn");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		initializingToIdleTransitionEffectAction = (event, context) ->
		{
			RadarSystem system = (RadarSystem)context.get();
			Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
			RadarSystemControl control = ((RadarSystemControlMessage)message).control;
			system.onControlToIdle(control);
		};
		idleToF2T2ScanningTransitionEffectAction = (event, context) ->
		{
			RadarSystem system = (RadarSystem)context.get();
			Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
			RadarSystemControl control = ((RadarSystemControlMessage)message).control;
			system.onControlToF2T2Scanning(control);
		};
		idleToEAScanningTransitionEffectAction = (event, context) ->
		{
			RadarSystem system = (RadarSystem)context.get();
			Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
			RadarSystemControl control = ((RadarSystemControlMessage)message).control;
			system.onControlToEAScanning(control);
		};
		idleToFinalTransitionEffectAction = (event, context) ->
		{
			RadarSystem system = (RadarSystem)context.get();
			Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
			RadarSystemControl control = ((RadarSystemControlMessage)message).control;
			system.onControlToDetach(control);
		};
		f2t2ScanningToEAScanningTransitionEffectAction = (event, context) ->
		{
			RadarSystem system = (RadarSystem)context.get();
			Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
			RadarSystemControl control = ((RadarSystemControlMessage)message).control;
			system.onControlToEAScanning(control);
		};
		f2t2ScanningToIdleTransitionEffectAction = (event, context) ->
		{
			RadarSystem system = (RadarSystem)context.get();
			Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
			RadarSystemControl control = ((RadarSystemControlMessage)message).control;
			system.onControlToIdle(control);
		};
		eaScanningToIdleTransitionEffectAction = (event, context) ->
		{
			RadarSystem system = (RadarSystem)context.get();
			Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
			RadarSystemControl control = ((RadarSystemControlMessage)message).control;
			system.onControlToIdle(control);
		};
		f2t2ScanTimeTransitionEffectAction = (event, context) ->
		{
			RadarSystem system = (RadarSystem)context.get();
			system.performNextF2T2Scan();
		};
		eaScanTimeTransitionEffectAction = (event, context) ->
		{
			RadarSystem system = (RadarSystem)context.get();
			system.performNextEAScan();
		};
		f2t2ScanRadarSignalReturnEffectAction = (event, context) ->
		{
			RadarSystem system = (RadarSystem)context.get();
			RadarSignalReturn reflection = ((RadarSignalReturnSignal)((SysMLSignalEvent)event.get()).signal).radarReturn;
			system.onF2T2RadarSignalReturn(reflection);
		};
		eaScanRadarSignalReturnEffectAction = (event, context) ->
		{
			RadarSystem system = (RadarSystem)context.get();
			RadarSignalReturn reflection = ((RadarSignalReturnSignal)((SysMLSignalEvent)event.get()).signal).radarReturn;
			system.onEARadarSignalReturn(reflection);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		initializingToIdleTransitionEffect = new SysMLEffect(context, initializingToIdleTransitionEffectAction, "InitializingToIdleTransition");
		idleToF2T2ScanningTransitionEffect = new SysMLEffect(context, idleToF2T2ScanningTransitionEffectAction, "IdleToF2T2ScanningTransition");
		idleToEAScanningTransitionEffect = new SysMLEffect(context, idleToEAScanningTransitionEffectAction, "IdleToEAScanningTransition");
		f2t2ScanningToEAScanningTransitionEffect = new SysMLEffect(context, f2t2ScanningToEAScanningTransitionEffectAction, "F2T2ScanningToEAScanningTransition");
		f2t2ScanningToIdleTransitionEffect = new SysMLEffect(context, f2t2ScanningToIdleTransitionEffectAction, "F2T2ScanningToIdleTransition");
		eaScanningToIdleTransitionEffect = new SysMLEffect(context, eaScanningToIdleTransitionEffectAction, "EAScanningToIdle");
		f2t2ScanTimeTransitionEffect = new SysMLEffect(context, f2t2ScanTimeTransitionEffectAction, "F2T2ScanTimeTransition");
		eaScanTimeTransitionEffect = new SysMLEffect(context, eaScanTimeTransitionEffectAction, "EAScanTimeTransition");
		f2t2ScanRadarSignalReturnEffect = new SysMLEffect(context, f2t2ScanRadarSignalReturnEffectAction, "F2T2ScanningRadarSignalReturnTransition");
		eaScanRadarSignalReturnEffect = new SysMLEffect(context, eaScanRadarSignalReturnEffectAction, "EAScanningRadarSignalReturnTransition");
		idleToFinalTransitionEffect = new SysMLEffect(context, idleToFinalTransitionEffectAction, "IdleToFinalTransition");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(context, initialState, initializingState, "InitialToInitializing");
		initializingToIdleTransition = new SysMLTransition(context, initializingState, idleState, Optional.of(SysMLSignalEvent.class), Optional.of(isControlToIdleStateGuard), Optional.of(initializingToIdleTransitionEffect),
			"InitializingToIdle", SysMLTransitionKind.external);
		idleToF2T2ScanningTransition = new SysMLTransition(context, idleState, f2t2ScanningState, Optional.of(SysMLSignalEvent.class), Optional.of(isControlToF2T2ScanningStateGuard), Optional.of(idleToF2T2ScanningTransitionEffect),
			"IdleToF2T2Scanning", SysMLTransitionKind.external);
		idleToEAScanningTransition = new SysMLTransition(context, idleState, eaScanningState, Optional.of(SysMLSignalEvent.class), Optional.of(isControlToEAScanningStateGuard), Optional.of(idleToEAScanningTransitionEffect),
			"IdleToF2T2Scanning", SysMLTransitionKind.external);
		f2t2ScanningToIdleTransition = new SysMLTransition(context, f2t2ScanningState, idleState, Optional.of(SysMLSignalEvent.class), Optional.of(isControlToIdleStateGuard), Optional.of(f2t2ScanningToIdleTransitionEffect),
			"F2T2ScanningToIdle", SysMLTransitionKind.external);
		eaScanningToIdleTransition = new SysMLTransition(context, eaScanningState, idleState, Optional.of(SysMLSignalEvent.class), Optional.of(isControlToIdleStateGuard), Optional.of(eaScanningToIdleTransitionEffect),
			"EAScanningToIdle", SysMLTransitionKind.external);
		f2t2ScanningToEAScanningTransition = new SysMLTransition(context, f2t2ScanningState, eaScanningState, Optional.of(SysMLSignalEvent.class), Optional.of(isControlToEAScanningStateGuard),
			Optional.of(f2t2ScanningToEAScanningTransitionEffect), "F2T2ScanningToEAScanning", SysMLTransitionKind.external);
		f2t2ScanTimeTransition = new SysMLTransition(context, f2t2ScanningState, f2t2ScanningState, Optional.of(SysMLTimeEvent.class), Optional.of(isF2T2ScanTimeGuard), Optional.of(f2t2ScanTimeTransitionEffect), "F2T2ScanTime",
			SysMLTransitionKind.internal);
		eaScanTimeTransition = new SysMLTransition(context, eaScanningState, eaScanningState, Optional.of(SysMLTimeEvent.class), Optional.of(isEAScanTimeGuard), Optional.of(eaScanTimeTransitionEffect), "EAScanTime",
			SysMLTransitionKind.internal);
		f2t2ScanningRadarSignalReturnTransition = new SysMLTransition(context, f2t2ScanningState, f2t2ScanningState, Optional.of(SysMLSignalEvent.class), Optional.of(isRadarSignalReturnGuard),
			Optional.of(f2t2ScanRadarSignalReturnEffect), "F2T2ScanningRadarSignalReturn", SysMLTransitionKind.internal);
		eaScanningRadarSignalReturnTransition = new SysMLTransition(context, eaScanningState, eaScanningState, Optional.of(SysMLSignalEvent.class), Optional.of(isRadarSignalReturnGuard), Optional.of(eaScanRadarSignalReturnEffect),
			"EAScanningRadarSignalReturn", SysMLTransitionKind.internal);
		idleToFinalTransition = new SysMLTransition(context, idleState, finalState, Optional.of(SysMLSignalEvent.class), Optional.of(isControlToFinalStateGuard), Optional.of(idleToFinalTransitionEffect), "IdleToFinal",
			SysMLTransitionKind.external);
	}
}
