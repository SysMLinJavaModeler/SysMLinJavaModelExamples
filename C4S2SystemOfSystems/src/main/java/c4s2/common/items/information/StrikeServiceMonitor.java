package c4s2.common.items.information;

import c4s2.common.attributetypes.ServiceStatesEnum;
import sysmlinjava.attributetypes.InstantMilliseconds;

@SuppressWarnings("javadoc")
public class StrikeServiceMonitor extends ServiceMonitor
{

	public StrikeServiceMonitor(ServiceStatesEnum state, InstantMilliseconds time)
	{
		super(state, time);
	}

	public StrikeServiceMonitor(StrikeServiceMonitor copyOf)
	{
		super(copyOf.state, new InstantMilliseconds(copyOf.time));
	}
	
	@Override
	public String stackNamesString()
	{
		return String.format("%s(%s)", getClass().getSimpleName(), super.stackNamesString());
	}

	@Override
	public String toString()
	{
		return String.format("StrikeServiceMonitor [state=%s, time=%s]", state, time);
	}
}
