package c4s2.components.services.system;

import java.util.Optional;
import c4s2.common.messages.OperatorServiceMonitorMessage;
import c4s2.common.messages.RadarServiceMonitorMessage;
import c4s2.common.messages.StrikeServiceMonitorMessage;
import c4s2.common.messages.SystemControlMessage;
import c4s2.common.messages.SystemServiceControlMessage;
import c4s2.common.messages.SystemServiceMonitorMessage;
import c4s2.common.messages.TargetServiceMonitorMessage;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjavalibrary.common.events.MessageEvent;
import sysmlinjavalibrary.common.events.SNMPResponseEvent;
import sysmlinjavalibrary.common.messages.Message;
import sysmlinjavalibrary.common.objects.information.SNMPResponse;
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

	@EffectActivity
	public SysMLEffectActivity operationalOnSNMPResponseTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnSNMPResponseTransitionEffect;

	public SystemServicesStateMachine(SystemServices radarServices)
	{
		super(radarServices, "SystemServicesStateMachine");
	}

	@Override
	protected void createEffectActivities()
	{
		operationalOnSNMPResponseTransitionEffectActivity = (event, contextBlock) ->
		{
			SystemServices services = (SystemServices)contextBlock.get();
			SNMPResponse response = ((SNMPResponseEvent)event.get()).getResponse();
			services.onSNMResponse(response);
		};
		onMessageEffectActivity = (event, contextBlock) ->
		{
			SystemServices services = (SystemServices)contextBlock.get();
			Message message = ((MessageEvent)event.get()).getMessage();
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
		operationalOnSNMPResponseTransitionEffect = new SysMLEffect(contextBlock, operationalOnSNMPResponseTransitionEffectActivity, "OperationalOnSNMPResponseTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnSNMPResponseTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(SNMPResponseEvent.class), Optional.empty(), Optional.of(operationalOnSNMPResponseTransitionEffect),
			"OperationalOnSNMPResponse", SysMLTransitionKind.internal);
	}
}
