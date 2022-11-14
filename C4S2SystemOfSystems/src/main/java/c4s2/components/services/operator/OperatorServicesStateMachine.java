package c4s2.components.services.operator;

import java.util.Optional;
import c4s2.common.events.OperatorRadarControlViewEvent;
import c4s2.common.events.OperatorStrikeControlViewEvent;
import c4s2.common.events.OperatorSystemControlViewEvent;
import c4s2.common.events.OperatorTargetControlViewEvent;
import c4s2.common.messages.OperatorServiceControlMessage;
import c4s2.common.messages.RadarMonitorMessage;
import c4s2.common.messages.StrikeMonitorMessage;
import c4s2.common.messages.SystemMonitorMessage;
import c4s2.common.messages.TargetMonitorMessage;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjavalibrary.common.events.MessageEvent;
import sysmlinjavalibrary.common.messages.Message;
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

	@EffectActivity
	public SysMLEffectActivity operationalOnOperatorRadarControlViewTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity operationalOnOperatorStrikeControlViewTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity operationalOnOperatorSystemControlViewTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity operationalOnOperatorTargetControlViewTransitionEffectActivity;

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

	@Override
	protected void createEffectActivities()
	{
		operationalOnOperatorRadarControlViewTransitionEffectActivity = (event, contextBlock) ->
		{
			OperatorServices services = (OperatorServices)contextBlock.get();
			services.onRadarControlView(((OperatorRadarControlViewEvent)event.get()).getControlView());
		};
		operationalOnOperatorStrikeControlViewTransitionEffectActivity = (event, contextBlock) ->
		{
			OperatorServices services = (OperatorServices)contextBlock.get();
			services.onStrikeControlView(((OperatorStrikeControlViewEvent)event.get()).getControlView());
		};
		operationalOnOperatorSystemControlViewTransitionEffectActivity = (event, contextBlock) ->
		{
			OperatorServices services = (OperatorServices)contextBlock.get();
			services.onSystemControlView(((OperatorSystemControlViewEvent)event.get()).getControlView());
		};
		operationalOnOperatorTargetControlViewTransitionEffectActivity = (event, contextBlock) ->
		{
			OperatorServices services = (OperatorServices)contextBlock.get();
			services.onTargetControlView(((OperatorTargetControlViewEvent)event.get()).getControlView());
		};
		onMessageEffectActivity = (event, contextBlock) ->
		{
			OperatorServices services = (OperatorServices)contextBlock.get();
			try {
			Message message = ((MessageEvent)event.get()).getMessage();
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
			}catch(Exception e) {e.printStackTrace();}
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnOperatorRadarControlViewTransitionEffect = new SysMLEffect(contextBlock, operationalOnOperatorRadarControlViewTransitionEffectActivity, "OperationalOnOperatorRadarControlViewTransition");
		operationalOnOperatorStrikeControlViewTransitionEffect = new SysMLEffect(contextBlock, operationalOnOperatorStrikeControlViewTransitionEffectActivity, "OperationalOnOperatorStrikeControlViewTransition");
		operationalOnOperatorRadarControlViewTransitionEffect = new SysMLEffect(contextBlock, operationalOnOperatorRadarControlViewTransitionEffectActivity, "OperationalOnOperatorSystemControlViewTransition");
		operationalOnOperatorSystemControlViewTransitionEffect = new SysMLEffect(contextBlock, operationalOnOperatorSystemControlViewTransitionEffectActivity, "OperationalOnOperatorSystemControlViewTransition");
		operationalOnOperatorTargetControlViewTransitionEffect = new SysMLEffect(contextBlock, operationalOnOperatorTargetControlViewTransitionEffectActivity, "OperationalOnOperatorTargetControlViewTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnOperatorRadarControlViewTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(OperatorRadarControlViewEvent.class), Optional.empty(),
			Optional.of(operationalOnOperatorRadarControlViewTransitionEffect), "OperationalOnOperatorRadarControlView", SysMLTransitionKind.internal);
		operationalOnOperatorStrikeControlViewTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(OperatorStrikeControlViewEvent.class), Optional.empty(),
			Optional.of(operationalOnOperatorStrikeControlViewTransitionEffect), "OperationalOnOperatorStrikeControlView", SysMLTransitionKind.internal);
		operationalOnOperatorSystemControlViewTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(OperatorSystemControlViewEvent.class), Optional.empty(),
			Optional.of(operationalOnOperatorSystemControlViewTransitionEffect), "OperationalOnOperatorSystemControlView", SysMLTransitionKind.internal);
		operationalOnOperatorTargetControlViewTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(OperatorTargetControlViewEvent.class), Optional.empty(),
			Optional.of(operationalOnOperatorTargetControlViewTransitionEffect), "OperationalOnOperatorTargetControlView", SysMLTransitionKind.internal);
	}
}
