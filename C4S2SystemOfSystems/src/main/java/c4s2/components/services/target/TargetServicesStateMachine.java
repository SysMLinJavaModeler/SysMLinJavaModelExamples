package c4s2.components.services.target;

import c4s2.common.messages.RadarMonitorMessage;
import c4s2.common.messages.TargetControlMessage;
import c4s2.common.messages.TargetServiceControlMessage;
import sysmlinjavalibrary.common.events.MessageEvent;
import sysmlinjavalibrary.common.messages.Message;
import sysmlinjavalibrary.components.services.common.MicroserviceStateMachine;

/**
 * The {@code TargetServicesStateMachine} is the SysMLinJava model of the state
 * machine for the tracking/targeting algorithms of the target services that
 * provide the monitor and control data between the algorithms and the
 * {@code C4S2Operator}. The state machine is an extension of the standard
 * {@code MicroserviceStateMachine} which has only 2 states - initializing and
 * operational. See the state machine's states and transitions for definition of
 * this behavior.
 * 
 * @author ModelerOne
 *
 */
public class TargetServicesStateMachine extends MicroserviceStateMachine
{
	public TargetServicesStateMachine(TargetServices targetServices)
	{
		super(targetServices, "TargetServicesStateMachine");
	}

	@Override
	public void createEffectActivities()
	{
		onMessageEffectActivity = (event, contextBlock) ->
		{
			TargetServices service = (TargetServices)contextBlock.get();
			Message message = ((MessageEvent)event.get()).getMessage();
			if (message instanceof TargetServiceControlMessage serviceControlMessage)
				service.onTargetServiceControl(serviceControlMessage.control);
			else if (message instanceof TargetControlMessage targetControlMessage)
				service.onTargetControl(targetControlMessage.control);
			else if (message instanceof RadarMonitorMessage radarMonitorMessage)
				service.onRadarMonitor(radarMonitorMessage.monitor);
			else
				logger.warning("unrecognized message type: " + message.getClass().getSimpleName());
		};
	}
}
