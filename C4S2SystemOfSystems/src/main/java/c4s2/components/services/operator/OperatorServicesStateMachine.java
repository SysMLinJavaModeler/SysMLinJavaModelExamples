package c4s2.components.services.operator;

import java.util.Optional;

import c4s2.common.messages.OperatorServiceControlMessage;
import c4s2.common.messages.RadarMonitorMessage;
import c4s2.common.messages.StrikeMonitorMessage;
import c4s2.common.messages.SystemMonitorMessage;
import c4s2.common.messages.TargetMonitorMessage;
import c4s2.common.signals.OperatorRadarControlViewSignal;
import c4s2.common.signals.OperatorStrikeControlViewSignal;
import c4s2.common.signals.OperatorSystemControlViewSignal;
import c4s2.common.signals.OperatorTargetControlViewSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLGuard;
import sysmlinjava.states.SysMLGuardCondition;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;
import sysmlinjavalibrary.common.messages.Message;
import sysmlinjavalibrary.common.signals.MessageSignal;
import sysmlinjavalibrary.components.services.common.MicroserviceStateMachine;

/**
 * The {@code OperatorServicesStateMachine} is the SysMLinJava model of the
 * state machine for the operator services that provide displays and services to
 * the C4S2 operator. The state machine is an extension of the standard
 * {@code MicroserviceStateMachine} which has only 2 state - initializing and
 * operational. See the state machine's states and transitions for definition of
 * this behavior.
 * 
 * @author ModelerOne
 *
 *         Known users:
 * @see OperatorServices
 * @see sysmlinjavalibrary.components.services.common.MicroserviceStateMachine
 */
@SuppressWarnings("javadoc")
public class OperatorServicesStateMachine extends MicroserviceStateMachine
{
	@Transition
	public SysMLTransition operationalOnOperatorRadarControlViewTransition;
	@Transition
	public SysMLTransition operationalOnOperatorStrikeControlViewTransition;
	@Transition
	public SysMLTransition operationalOnOperatorSystemControlViewTransition;
	@Transition
	public SysMLTransition operationalOnOperatorTargetControlViewTransition;

	@GuardCondition
	public SysMLGuardCondition isOperatorRadarControlViewGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isOperatorStrikeControlViewGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isOperatorSystemControlViewGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isOperatorTargetControlViewGuardCondition;

	@Guard
	public SysMLGuard isOperatorRadarControlViewGuard;
	@Guard
	public SysMLGuard isOperatorStrikeControlViewGuard;
	@Guard
	public SysMLGuard isOperatorSystemControlViewGuard;
	@Guard
	public SysMLGuard isOperatorTargetControlViewGuard;

	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnOperatorRadarControlViewTransitionEffectActivity;
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnOperatorStrikeControlViewTransitionEffectActivity;
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnOperatorSystemControlViewTransitionEffectActivity;
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnOperatorTargetControlViewTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnOperatorRadarControlViewTransitionEffect;
	@Effect
	public SysMLEffect operationalOnOperatorStrikeControlViewTransitionEffect;
	@Effect
	public SysMLEffect operationalOnOperatorSystemControlViewTransitionEffect;
	@Effect
	public SysMLEffect operationalOnOperatorTargetControlViewTransitionEffect;

	public OperatorServicesStateMachine(OperatorServices targetServices)
	{
		super(targetServices, "OperatorServicesStateMachine");
	}

	@Override
	protected void createStates()
	{
		super.createStates();
	}

	@SuppressWarnings("unused")
	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isOperatorRadarControlViewGuardCondition = (event, context) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent signalEvent && signalEvent.signal instanceof OperatorRadarControlViewSignal;
		};
		isOperatorStrikeControlViewGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent signalEvent && signalEvent.signal instanceof OperatorStrikeControlViewSignal;
		};
		isOperatorSystemControlViewGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent signalEvent && signalEvent.signal instanceof OperatorSystemControlViewSignal;
		};
		isOperatorTargetControlViewGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent signalEvent && signalEvent.signal instanceof OperatorTargetControlViewSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isOperatorRadarControlViewGuard = new SysMLGuard(context, isOperatorRadarControlViewGuardCondition, "isOperatorRadarControlViewGuard");
		isOperatorStrikeControlViewGuard = new SysMLGuard(context, isOperatorStrikeControlViewGuardCondition, "isOperatorStrikeControlViewGuard");
		isOperatorSystemControlViewGuard = new SysMLGuard(context, isOperatorSystemControlViewGuardCondition, "isOperatorSystemControlViewGuard");
		isOperatorTargetControlViewGuard = new SysMLGuard(context, isOperatorTargetControlViewGuardCondition, "isOperatorTargetControlViewGuard");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		operationalOnOperatorRadarControlViewTransitionEffectActivity = (event, contextBlock) ->
		{
			OperatorServices services = (OperatorServices)contextBlock.get();
			SysMLSignalEvent signalEvent = (SysMLSignalEvent)event.get();
			OperatorRadarControlViewSignal controlViewSignal = (OperatorRadarControlViewSignal)signalEvent.signal;
			services.onRadarControlView(controlViewSignal.controlView);
		};
		operationalOnOperatorStrikeControlViewTransitionEffectActivity = (event, contextBlock) ->
		{
			OperatorServices services = (OperatorServices)contextBlock.get();
			SysMLSignalEvent signalEvent = (SysMLSignalEvent)event.get();
			OperatorStrikeControlViewSignal controlViewSignal = (OperatorStrikeControlViewSignal)signalEvent.signal;
			services.onStrikeControlView(controlViewSignal.controlView);
		};
		operationalOnOperatorSystemControlViewTransitionEffectActivity = (event, contextBlock) ->
		{
			OperatorServices services = (OperatorServices)contextBlock.get();
			SysMLSignalEvent signalEvent = (SysMLSignalEvent)event.get();
			OperatorSystemControlViewSignal controlViewSignal = (OperatorSystemControlViewSignal)signalEvent.signal;
			services.onSystemControlView(controlViewSignal.controlView);
		};
		operationalOnOperatorTargetControlViewTransitionEffectActivity = (event, contextBlock) ->
		{
			OperatorServices services = (OperatorServices)contextBlock.get();
			SysMLSignalEvent signalEvent = (SysMLSignalEvent)event.get();
			OperatorTargetControlViewSignal controlViewSignal = (OperatorTargetControlViewSignal)signalEvent.signal;
			services.onTargetControlView(controlViewSignal.controlView);
		};
		onMessageEffectActivity = (event, contextBlock) ->
		{
			OperatorServices services = (OperatorServices)contextBlock.get();
			try
			{
				Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
				if (message instanceof OperatorServiceControlMessage controlMessage)
					services.onOperatorServiceControl(controlMessage.control);
				else if (message instanceof SystemMonitorMessage monitorMessage)
					services.onSystemMonitor(monitorMessage.monitor);
				else if (message instanceof RadarMonitorMessage monitorMessage)
					services.onRadarMonitor(monitorMessage.monitor);
				else if (message instanceof TargetMonitorMessage monitorMessage)
					services.onTargetMonitor(monitorMessage.monitor);
				else if (message instanceof StrikeMonitorMessage monitorMessage)
					services.onStrikeMonitor(monitorMessage.monitor);
				else
					logger.warning("unrecognized message type: " + message.getClass().getSimpleName());
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnOperatorRadarControlViewTransitionEffect = new SysMLEffect(context, operationalOnOperatorRadarControlViewTransitionEffectActivity, "OperationalOnOperatorRadarControlViewTransition");
		operationalOnOperatorStrikeControlViewTransitionEffect = new SysMLEffect(context, operationalOnOperatorStrikeControlViewTransitionEffectActivity, "OperationalOnOperatorStrikeControlViewTransition");
		operationalOnOperatorRadarControlViewTransitionEffect = new SysMLEffect(context, operationalOnOperatorRadarControlViewTransitionEffectActivity, "OperationalOnOperatorSystemControlViewTransition");
		operationalOnOperatorSystemControlViewTransitionEffect = new SysMLEffect(context, operationalOnOperatorSystemControlViewTransitionEffectActivity, "OperationalOnOperatorSystemControlViewTransition");
		operationalOnOperatorTargetControlViewTransitionEffect = new SysMLEffect(context, operationalOnOperatorTargetControlViewTransitionEffectActivity, "OperationalOnOperatorTargetControlViewTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnOperatorRadarControlViewTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isOperatorRadarControlViewGuard),
			Optional.of(operationalOnOperatorRadarControlViewTransitionEffect), "OperationalOnOperatorRadarControlView", SysMLTransitionKind.internal);
		operationalOnOperatorStrikeControlViewTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isOperatorStrikeControlViewGuard),
			Optional.of(operationalOnOperatorStrikeControlViewTransitionEffect), "OperationalOnOperatorStrikeControlView", SysMLTransitionKind.internal);
		operationalOnOperatorSystemControlViewTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isOperatorSystemControlViewGuard),
			Optional.of(operationalOnOperatorSystemControlViewTransitionEffect), "OperationalOnOperatorSystemControlView", SysMLTransitionKind.internal);
		operationalOnOperatorTargetControlViewTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isOperatorTargetControlViewGuard),
			Optional.of(operationalOnOperatorTargetControlViewTransitionEffect), "OperationalOnOperatorTargetControlView", SysMLTransitionKind.internal);
	}
}
