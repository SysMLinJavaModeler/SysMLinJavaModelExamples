package c4s2.common.items.information;

import c4s2.common.attributetypes.ServiceStatesEnum;
import sysmlinjava.attributetypes.InstantMilliseconds;

@SuppressWarnings("javadoc")
public class SystemServiceMonitor extends ServiceMonitor
{
	public SystemServiceMonitor(ServiceStatesEnum state, InstantMilliseconds time)
	{
		super(state, time);
	}
	
	@Override
	public String stackNamesString()
	{
		return String.format("%s(%s)", getClass().getSimpleName(), super.stackNamesString());
	}

	@Override
	public String toString()
	{
		return String.format("SystemServiceMonitor [state=%s, time=%s]", state, time);
	}
}
