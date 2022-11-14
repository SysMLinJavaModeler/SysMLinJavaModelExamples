package c4s2.common.objects.information;

import c4s2.common.valueTypes.ServiceStatesEnum;
import sysmlinjava.valuetypes.InstantMilliseconds;

public class TargetServiceMonitor extends ServiceMonitor
{
	public TargetServiceMonitor(ServiceStatesEnum state, InstantMilliseconds time)
	{
		super(state, time);
	}

	@Override
	public String stackNamesString()
	{
		return String.format("%s(%s)", getClass().getSimpleName(), super.stackNamesString());
	}
	public TargetServiceMonitor(TargetServiceMonitor copied)
	{
		super(copied);
	}

	@Override
	public String toString()
	{
		return String.format("TargetServiceMonitor [state=%s, time=%s]", state, time);
	}
}
