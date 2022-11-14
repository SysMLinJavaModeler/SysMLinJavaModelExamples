package c4s2.components.services.strike;

import c4s2.common.messages.StrikeControlMessage;
import c4s2.common.messages.StrikeServiceControlMessage;
import c4s2.common.messages.StrikeSystemMonitorMessage;
import sysmlinjavalibrary.common.events.MessageEvent;
import sysmlinjavalibrary.common.messages.Message;
import sysmlinjavalibrary.components.services.common.MicroserviceStateMachine;

/**
 * The {@code StrikeServicesStateMachine} is the SysMLinJava model of the state
 * machine for the strike services that provide the monitor and control data
 * between the {@code StrikeSystem} and the {@code C4S2Operator}. The state
 * machine is an extension of the standard {@code MicroserviceStateMachine}
 * which has only 2 state - initializing and operational. See the state
 * machine's states and transitions for definition of this behavior.
 * 
 * @author ModelerOne
 *
 *         Known users:
 * @see StrikeServices
 * @see sysmlinjavalibrary.components.services.common.MicroserviceStateMachine
 */
public class StrikeServicesStateMachine extends MicroserviceStateMachine
{
	public StrikeServicesStateMachine(StrikeServices service)
	{
		super(service, "StrikeServicesStateMachine");
	}

	@Override
	protected void createEffectActivities()
	{
		onMessageEffectActivity = (event, contextBlock) ->
		{
			StrikeServices services = (StrikeServices)contextBlock.get();
			Message message = ((MessageEvent)event.get()).getMessage();
			if(message instanceof StrikeServiceControlMessage controlMessage)
				services.onStrikeServiceControl(controlMessage.control);
			else if(message instanceof StrikeControlMessage controlMessage)
				services.onStrikeControl(controlMessage.control);
			else if(message instanceof StrikeSystemMonitorMessage monitorMessage)
				services.onStrikeSystemMonitor(monitorMessage.monitor);
			else
				logger.warning("unrecognized message type: " + message.getClass().getSimpleName());
		};
	}
}
