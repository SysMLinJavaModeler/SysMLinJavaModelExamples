package c4s2.common.items.information;

import c4s2.common.attributetypes.ServiceStatesEnum;
import sysmlinjava.attributetypes.InstantMilliseconds;

@SuppressWarnings("javadoc")
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
