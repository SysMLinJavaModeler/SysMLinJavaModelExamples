
package c4s2.systems.radar;

import java.util.Optional;
import c4s2.common.events.RadarSignalReturnEvent;
import c4s2.common.messages.RadarSystemControlMessage;
import c4s2.common.objects.information.RadarSignalReturn;
import c4s2.common.objects.information.RadarSystemControl;
import c4s2.common.valueTypes.RadarSystemStatesEnum;
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

	@OnEnterActivity
	public SysMLOnEnterActivity initializingStateOnEnterActivity;
	@OnEnterActivity
	public SysMLOnEnterActivity f2t2ScanningStateOnEnterActivity;
	@OnEnterActivity
	public SysMLOnEnterActivity eaScanningStateOnEnterActivity;

	@OnExitActivity
	public SysMLOnExitActivity f2t2ScanningStateOnExitActivity;
	@OnExitActivity
	public SysMLOnExitActivity eaScanningStateOnExitActivity;

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

	@EffectActivity
	public SysMLEffectActivity idleToF2T2ScanningTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity idleToEAScanningTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity idleToFinalTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity f2t2ScanningToIdleTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity eaScanningToIdleTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity f2t2ScanningToEAScanningTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity f2t2ScanTimeTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity eaScanTimeTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity f2t2ScanRadarSignalReturnEffectActivity;
	@EffectActivity
	public SysMLEffectActivity eaScanRadarSignalReturnEffectActivity;
	@EffectActivity
	private SysMLEffectActivity initializingToIdleTransitionEffectActivity;

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
	protected void createStateOnEnterActivities()
	{
		super.createStateOnEnterActivities();
		initializingStateOnEnterActivity = (contextBlock) ->
		{
			RadarSystem radarSystem = (RadarSystem)contextBlock.get();
			radarSystem.initialize();
		};
		f2t2ScanningStateOnEnterActivity = (contextBlock) ->
		{
			RadarSystem radarSystem = (RadarSystem)contextBlock.get();
			radarSystem.startF2T2Scanning();
			startTimer(RadarSystem.f2t2ScanTimerID, DurationMilliseconds.ofSeconds(2), ((RadarSystem)contextBlock.get()).f2t2ScanIntervalDuration);
		};
		eaScanningStateOnEnterActivity = (contextBlock) ->
		{
			RadarSystem radarSystem = (RadarSystem)contextBlock.get();
			radarSystem.startEAScanning();
			startTimer(RadarSystem.eaScanTimerID, DurationMilliseconds.ofSeconds(2), ((RadarSystem)contextBlock.get()).eaScanIntervalDuration);
		};
	}

	@Override
	protected void createStateOnExitActivities()
	{
		super.createStateOnExitActivities();
		f2t2ScanningStateOnExitActivity = (contextBlock) ->
		{
			RadarSystem radarSystem = (RadarSystem)contextBlock.get();
			radarSystem.stopF2T2Scanning();
			stopTimer(RadarSystem.f2t2ScanTimerID);
		};
		eaScanningStateOnExitActivity = (contextBlock) ->
		{
			RadarSystem radarSystem = (RadarSystem)contextBlock.get();
			radarSystem.stopEAScanning();
			stopTimer(RadarSystem.eaScanTimerID);
		};
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(contextBlock, Optional.of(initializingStateOnEnterActivity), Optional.empty(), Optional.empty(), "Initializing");
		idleState = new SysMLState(contextBlock, Optional.empty(), Optional.empty(), Optional.empty(), "Idle");
		f2t2ScanningState = new SysMLState(contextBlock, Optional.of(f2t2ScanningStateOnEnterActivity), Optional.empty(), Optional.of(f2t2ScanningStateOnExitActivity), "F2T2Scanning");
		eaScanningState = new SysMLState(contextBlock, Optional.of(eaScanningStateOnEnterActivity), Optional.empty(), Optional.of(eaScanningStateOnExitActivity), "EAScanning");
	}

	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isControlToIdleStateGuardCondition = (event, contextBlock) ->
		{
			Message message = ((MessageEvent)event.get()).getMessage();
			RadarSystemControl control = ((RadarSystemControlMessage)message).control;
			return (control.state == RadarSystemStatesEnum.Idle);
		};
		isControlToF2T2ScanningStateGuardCondition = (event, contextBlock) ->
		{
			Message message = ((MessageEvent)event.get()).getMessage();
			RadarSystemControl control = ((RadarSystemControlMessage)message).control;
			return (control.state == RadarSystemStatesEnum.F2T2Scanning);
		};
		isControlToEAScanningStateGuardCondition = (event, contextBlock) ->
		{
			Message message = ((MessageEvent)event.get()).getMessage();
			RadarSystemControl control = ((RadarSystemControlMessage)message).control;
			return (control.state == RadarSystemStatesEnum.EAScanning);
		};
		isControlToFinalStateGuardCondition = (event, contextBlock) ->
		{
			Message message = ((MessageEvent)event.get()).getMessage();
			RadarSystemControl control = ((RadarSystemControlMessage)message).control;
			return (control.state == RadarSystemStatesEnum.Detached);
		};
		isF2T2ScanTimeGuardCondition = (event, contextBlock) ->
		{
			SysMLTimeEvent timeEvent = (SysMLTimeEvent)event.get();
			return (timeEvent.timerID.equals(RadarSystem.f2t2ScanTimerID));
		};
		isEAScanTimeGuardCondition = (event, contextBlock) ->
		{
			SysMLTimeEvent timeEvent = (SysMLTimeEvent)event.get();
			return (timeEvent.timerID.equals(RadarSystem.eaScanTimerID));
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isControlToIdleStateGuard = new SysMLGuard(contextBlock, isControlToIdleStateGuardCondition, "IsControlToIdleState");
		isControlToF2T2ScanningStateGuard = new SysMLGuard(contextBlock, isControlToF2T2ScanningStateGuardCondition, "IsControlToF2T2ScanningState");
		isControlToEAScanningStateGuard = new SysMLGuard(contextBlock, isControlToEAScanningStateGuardCondition, "IsControlToEAScannineState");
		isControlToFinalStateGuard = new SysMLGuard(contextBlock, isControlToFinalStateGuardCondition, "IsControlToFinalState");
		isF2T2ScanTimeGuard = new SysMLGuard(contextBlock, isF2T2ScanTimeGuardCondition, "IsF2T2ScanTime");
		isEAScanTimeGuard = new SysMLGuard(contextBlock, isEAScanTimeGuardCondition, "IsEAScanTime");
	}

	@Override
	protected void createEffectActivities()
	{
		super.createEffectActivities();
		initializingToIdleTransitionEffectActivity = (event, contextBlock) ->
		{
			RadarSystem system = (RadarSystem)contextBlock.get();
			Message message = ((MessageEvent)event.get()).getMessage();
			RadarSystemControl control = ((RadarSystemControlMessage)message).control;
			system.onControlToIdle(control);
		};
		idleToF2T2ScanningTransitionEffectActivity = (event, contextBlock) ->
		{
			RadarSystem system = (RadarSystem)contextBlock.get();
			Message message = ((MessageEvent)event.get()).getMessage();
			RadarSystemControl control = ((RadarSystemControlMessage)message).control;
			system.onControlToF2T2Scanning(control);
		};
		idleToEAScanningTransitionEffectActivity = (event, contextBlock) ->
		{
			RadarSystem system = (RadarSystem)contextBlock.get();
			Message message = ((MessageEvent)event.get()).getMessage();
			RadarSystemControl control = ((RadarSystemControlMessage)message).control;
			system.onControlToEAScanning(control);
		};
		idleToFinalTransitionEffectActivity = (event, contextBlock) ->
		{
			RadarSystem system = (RadarSystem)contextBlock.get();
			Message message = ((MessageEvent)event.get()).getMessage();
			RadarSystemControl control = ((RadarSystemControlMessage)message).control;
			system.onControlToDetach(control);
		};
		f2t2ScanningToEAScanningTransitionEffectActivity = (event, contextBlock) ->
		{
			RadarSystem system = (RadarSystem)contextBlock.get();
			Message message = ((MessageEvent)event.get()).getMessage();
			RadarSystemControl control = ((RadarSystemControlMessage)message).control;
			system.onControlToEAScanning(control);
		};
		f2t2ScanningToIdleTransitionEffectActivity = (event, contextBlock) ->
		{
			RadarSystem system = (RadarSystem)contextBlock.get();
			Message message = ((MessageEvent)event.get()).getMessage();
			RadarSystemControl control = ((RadarSystemControlMessage)message).control;
			system.onControlToIdle(control);
		};
		eaScanningToIdleTransitionEffectActivity = (event, contextBlock) ->
		{
			RadarSystem system = (RadarSystem)contextBlock.get();
			Message message = ((MessageEvent)event.get()).getMessage();
			RadarSystemControl control = ((RadarSystemControlMessage)message).control;
			system.onControlToIdle(control);
		};
		f2t2ScanTimeTransitionEffectActivity = (event, contextBlock) ->
		{
			RadarSystem system = (RadarSystem)contextBlock.get();
			system.performNextF2T2Scan();
		};
		eaScanTimeTransitionEffectActivity = (event, contextBlock) ->
		{
			RadarSystem system = (RadarSystem)contextBlock.get();
			system.performNextEAScan();
		};
		f2t2ScanRadarSignalReturnEffectActivity = (event, contextBlock) ->
		{
			RadarSystem system = (RadarSystem)contextBlock.get();
			RadarSignalReturn reflection = ((RadarSignalReturnEvent)event.get()).getReflection();
			system.onF2T2RadarSignalReturn(reflection);
		};
		eaScanRadarSignalReturnEffectActivity = (event, contextBlock) ->
		{
			RadarSystem system = (RadarSystem)contextBlock.get();
			RadarSignalReturn reflection = ((RadarSignalReturnEvent)event.get()).getReflection();
			system.onEARadarSignalReturn(reflection);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		initializingToIdleTransitionEffect = new SysMLEffect(contextBlock, initializingToIdleTransitionEffectActivity, "InitializingToIdleTransition");
		idleToF2T2ScanningTransitionEffect = new SysMLEffect(contextBlock, idleToF2T2ScanningTransitionEffectActivity, "IdleToF2T2ScanningTransition");
		idleToEAScanningTransitionEffect = new SysMLEffect(contextBlock, idleToEAScanningTransitionEffectActivity, "IdleToEAScanningTransition");
		f2t2ScanningToEAScanningTransitionEffect = new SysMLEffect(contextBlock, f2t2ScanningToEAScanningTransitionEffectActivity, "F2T2ScanningToEAScanningTransition");
		f2t2ScanningToIdleTransitionEffect = new SysMLEffect(contextBlock, f2t2ScanningToIdleTransitionEffectActivity, "F2T2ScanningToIdleTransition");
		eaScanningToIdleTransitionEffect = new SysMLEffect(contextBlock, eaScanningToIdleTransitionEffectActivity, "EAScanningToIdle");
		f2t2ScanTimeTransitionEffect = new SysMLEffect(contextBlock, f2t2ScanTimeTransitionEffectActivity, "F2T2ScanTimeTransition");
		eaScanTimeTransitionEffect = new SysMLEffect(contextBlock, eaScanTimeTransitionEffectActivity, "EAScanTimeTransition");
		f2t2ScanRadarSignalReturnEffect = new SysMLEffect(contextBlock, f2t2ScanRadarSignalReturnEffectActivity, "F2T2ScanningRadarSignalReturnTransition");
		eaScanRadarSignalReturnEffect = new SysMLEffect(contextBlock, eaScanRadarSignalReturnEffectActivity, "EAScanningRadarSignalReturnTransition");
		idleToFinalTransitionEffect = new SysMLEffect(contextBlock, idleToFinalTransitionEffectActivity, "IdleToFinalTransition");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(contextBlock, initialState, initializingState, "InitialToInitializing");
		initializingToIdleTransition = new SysMLTransition(contextBlock, initializingState, idleState, Optional.of(MessageEvent.class), Optional.of(isControlToIdleStateGuard), Optional.of(initializingToIdleTransitionEffect),
			"InitializingToIdle", SysMLTransitionKind.external);
		idleToF2T2ScanningTransition = new SysMLTransition(contextBlock, idleState, f2t2ScanningState, Optional.of(MessageEvent.class), Optional.of(isControlToF2T2ScanningStateGuard), Optional.of(idleToF2T2ScanningTransitionEffect),
			"IdleToF2T2Scanning", SysMLTransitionKind.external);
		idleToEAScanningTransition = new SysMLTransition(contextBlock, idleState, eaScanningState, Optional.of(MessageEvent.class), Optional.of(isControlToEAScanningStateGuard), Optional.of(idleToEAScanningTransitionEffect),
			"IdleToF2T2Scanning", SysMLTransitionKind.external);
		f2t2ScanningToIdleTransition = new SysMLTransition(contextBlock, f2t2ScanningState, idleState, Optional.of(MessageEvent.class), Optional.of(isControlToIdleStateGuard), Optional.of(f2t2ScanningToIdleTransitionEffect),
			"F2T2ScanningToIdle", SysMLTransitionKind.external);
		eaScanningToIdleTransition = new SysMLTransition(contextBlock, eaScanningState, idleState, Optional.of(MessageEvent.class), Optional.of(isControlToIdleStateGuard), Optional.of(eaScanningToIdleTransitionEffect), "EAScanningToIdle",
			SysMLTransitionKind.external);
		f2t2ScanningToEAScanningTransition = new SysMLTransition(contextBlock, f2t2ScanningState, eaScanningState, Optional.of(MessageEvent.class), Optional.of(isControlToEAScanningStateGuard),
			Optional.of(f2t2ScanningToEAScanningTransitionEffect), "F2T2ScanningToEAScanning", SysMLTransitionKind.external);
		f2t2ScanTimeTransition = new SysMLTransition(contextBlock, f2t2ScanningState, f2t2ScanningState, Optional.of(SysMLTimeEvent.class), Optional.of(isF2T2ScanTimeGuard), Optional.of(f2t2ScanTimeTransitionEffect), "F2T2ScanTime",
			SysMLTransitionKind.internal);
		eaScanTimeTransition = new SysMLTransition(contextBlock, eaScanningState, eaScanningState, Optional.of(SysMLTimeEvent.class), Optional.of(isEAScanTimeGuard), Optional.of(eaScanTimeTransitionEffect), "EAScanTime",
			SysMLTransitionKind.internal);
		f2t2ScanningRadarSignalReturnTransition = new SysMLTransition(contextBlock, f2t2ScanningState, f2t2ScanningState, Optional.of(RadarSignalReturnEvent.class), Optional.empty(), Optional.of(f2t2ScanRadarSignalReturnEffect),
			"F2T2ScanningRadarSignalReturn", SysMLTransitionKind.internal);
		eaScanningRadarSignalReturnTransition = new SysMLTransition(contextBlock, eaScanningState, eaScanningState, Optional.of(RadarSignalReturnEvent.class), Optional.empty(), Optional.of(eaScanRadarSignalReturnEffect),
			"EAScanningRadarSignalReturn", SysMLTransitionKind.internal);
		idleToFinalTransition = new SysMLTransition(contextBlock, idleState, finalState, Optional.of(MessageEvent.class), Optional.of(isControlToFinalStateGuard), Optional.of(idleToFinalTransitionEffect), "IdleToFinal",
			SysMLTransitionKind.external);
	}
}
