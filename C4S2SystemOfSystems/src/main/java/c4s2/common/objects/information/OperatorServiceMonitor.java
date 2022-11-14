package c4s2.common.objects.information;

import c4s2.common.valueTypes.ServiceStatesEnum;
import sysmlinjava.valuetypes.InstantMilliseconds;

public class OperatorServiceMonitor extends ServiceMonitor
{
	public OperatorServiceMonitor(ServiceStatesEnum state, InstantMilliseconds time)
	{
		super(state, time);
	}

	public OperatorServiceMonitor(OperatorServiceMonitor copied)
	{
		super(copied);
	}

	@Override
	public String stackNamesString()
	{
		return String.format("%s(%s)", getClass().getSimpleName(), super.stackNamesString());
	}

	@Override
	public String toString()
	{
		return String.format("OperatorServiceMonitor [state=%s, time=%s]", state, time);
	}
}
