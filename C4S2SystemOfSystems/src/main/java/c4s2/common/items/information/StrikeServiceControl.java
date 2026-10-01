package c4s2.common.items.information;

import c4s2.common.attributetypes.ServiceStatesEnum;
import sysmlinjava.attributetypes.InstantMilliseconds;

@SuppressWarnings("javadoc")
public class StrikeServiceControl extends ServiceControl
{
	public StrikeServiceControl(ServiceStatesEnum state, InstantMilliseconds time)
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
		return String.format("StrikeServiceControl [state=%s, time=%s]", state, time);
	}
}
