package c4s2.common.objects.information;

import c4s2.common.valueTypes.ServiceStatesEnum;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.valuetypes.InstantMilliseconds;

public class ServiceMonitor extends SysMLClass implements StackedProtocolObject
{
	@Attribute
	public ServiceStatesEnum state;
	@Attribute
	public InstantMilliseconds time;

	public ServiceMonitor(ServiceStatesEnum state, InstantMilliseconds time)
	{
		super();
		this.state = state;
		this.time = time;
	}

	public ServiceMonitor(ServiceMonitor copied)
	{
		super(copied);
		this.state = copied.state;
		this.time = new InstantMilliseconds(copied.time);
	}

	@Override
	public String stackNamesString()
	{
		return String.format("state=%s", state);
	}

	@Override
	public String toString()
	{
		return String.format("ServiceMonitor [state=%s, time=%s]", state, time);
	}
}
