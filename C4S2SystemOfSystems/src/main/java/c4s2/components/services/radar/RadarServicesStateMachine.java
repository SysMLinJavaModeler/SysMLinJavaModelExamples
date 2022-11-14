package c4s2.components.services.radar;

import c4s2.common.messages.RadarControlMessage;
import c4s2.common.messages.RadarServiceControlMessage;
import c4s2.common.messages.RadarSystemMonitorMessage;
import sysmlinjavalibrary.common.events.MessageEvent;
import sysmlinjavalibrary.common.messages.Message;
import sysmlinjavalibrary.components.services.common.MicroserviceStateMachine;

/**
 * The {@code RadarServicesStateMachine} is the SysMLinJava model of the state
 * machine for the radar services that provide the monitor and control data
 * between the {@code RadarSystem} and the {@code C4S2Operator}. The state
 * machine is an extension of the standard {@code MicroserviceStateMachine}
 * which has only 2 state - initializing and operational. See the state
 * machine's states and transitions for definition of this behavior.
 * 
 * @author ModelerOne
 *
 *         Known users:
 * @see RadarServices
 * @see sysmlinjavalibrary.components.services.common.MicroserviceStateMachine
 */
public class RadarServicesStateMachine extends MicroserviceStateMachine
{

	public RadarServicesStateMachine(RadarServices services)
	{
		super(services, "RadarServicesStateMachine");
	}

	@Override
	protected void createEffectActivities()
	{
		onMessageEffectActivity = (event, contextBlock) ->
		{
			RadarServices services = (RadarServices)contextBlock.get();
			Message message = ((MessageEvent)event.get()).getMessage();
			if (message instanceof RadarServiceControlMessage controlMessage)
				services.onRadarServiceControl(controlMessage.control);
			else if (message instanceof RadarControlMessage controlMessage)
				services.onRadarControl(controlMessage.control);
			else if (message instanceof RadarSystemMonitorMessage monitorMessage)
				services.onRadarSystemMonitor(monitorMessage.monitor);
			else
				logger.warning("unrecognized message type: " + message.getClass().getSimpleName());

		};
	}
}
