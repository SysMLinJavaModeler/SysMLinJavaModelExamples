package c4s2.components.services.system;

import java.util.Optional;
import c4s2.common.messages.OperatorServiceMonitorMessage;
import c4s2.common.messages.RadarServiceMonitorMessage;
import c4s2.common.messages.StrikeServiceMonitorMessage;
import c4s2.common.messages.SystemControlMessage;
import c4s2.common.messages.SystemServiceControlMessage;
import c4s2.common.messages.SystemServiceMonitorMessage;
import c4s2.common.messages.TargetServiceMonitorMessage;
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
import sysmlinjavalibrary.common.objects.information.SNMPResponse;
import sysmlinjavalibrary.common.signals.MessageSignal;
import sysmlinjavalibrary.common.signals.SNMPResponseSignal;
import sysmlinjavalibrary.components.services.common.MicroserviceStateMachine;

/**
 * The {@code SystemServicesStateMachine} is the SysMLinJava model of the state
 * machine for the system services that provide the monitor and control data
 * between the components of the {@code C4S2System} and the
 * {@code C4S2Operator}. The state machine is an extension of the standard
 * {@code MicroserviceStateMachine} which has only 2 state - initializing and
 * operational. See the state machine's states and transitions for definition of
 * this behavior.
 * 
 * @author ModelerOne
 *
 */
public class SystemServicesStateMachine extends MicroserviceStateMachine
{
	@Transition
	public SysMLTransition operationalOnSNMPResponseTransition;

	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnSNMPResponseTransitionEffectActivity;

	@GuardCondition
	public SysMLGuardCondition isSNMPResponseGuardCondition;
	
	@Guard
	public SysMLGuard isSNMPResponseGuard;
	
	@Effect
	public SysMLEffect operationalOnSNMPResponseTransitionEffect;

	public SystemServicesStateMachine(SystemServices radarServices)
	{
		super(radarServices, "SystemServicesStateMachine");
	}

	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isSNMPResponseGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() &&
				event.get() instanceof SysMLSignalEvent &&
				((SysMLSignalEvent)event.get()).signal instanceof SNMPResponseSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isSNMPResponseGuard = new SysMLGuard(context, isSNMPResponseGuardCondition, "isControl");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		operationalOnSNMPResponseTransitionEffectActivity = (event, contextBlock) ->
		{
			SystemServices services = (SystemServices)contextBlock.get();
			SNMPResponse response = ((SNMPResponseSignal)((SysMLSignalEvent)event.get()).signal).response;
			services.onSNMResponse(response);
		};
		onMessageEffectActivity = (event, contextBlock) ->
		{
			SystemServices services = (SystemServices)contextBlock.get();
			Message message = ((MessageSignal)((SysMLSignalEvent)event.get()).signal).message;
			if (message instanceof RadarServiceMonitorMessage monitorMessage)
				services.onRadarServicesMonitor(monitorMessage.monitor);
			else if (message instanceof StrikeServiceMonitorMessage monitorMessage)
				services.onStrikeServicesMonitor(monitorMessage.monitor);
			else if (message instanceof TargetServiceMonitorMessage monitorMessage)
				services.onTargetServicesMonitor(monitorMessage.monitor);
			else if (message instanceof SystemServiceMonitorMessage monitorMessage)
				services.onSystemServicesMonitor(monitorMessage.monitor);
			else if (message instanceof OperatorServiceMonitorMessage monitorMessage)
				services.onOperatorServicesMonitor(monitorMessage.monitor);
			else if (message instanceof SystemServiceControlMessage controlMessage)
				services.onSystemServicesControl(controlMessage.control);
			else if (message instanceof SystemControlMessage controlMessage)
				services.onSystemControl(controlMessage.control);
			else
				logger.warning("unrecognized type of service monitor message: " + message.getClass().getSimpleName());
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnSNMPResponseTransitionEffect = new SysMLEffect(context, operationalOnSNMPResponseTransitionEffectActivity, "OperationalOnSNMPResponseTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnSNMPResponseTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isSNMPResponseGuard),
			Optional.of(operationalOnSNMPResponseTransitionEffect), "OperationalOnSNMPResponse", SysMLTransitionKind.internal);
	}
}
