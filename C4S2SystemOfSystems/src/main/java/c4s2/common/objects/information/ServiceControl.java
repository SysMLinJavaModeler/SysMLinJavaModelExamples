package c4s2.common.objects.information;

import c4s2.common.valueTypes.ServiceStatesEnum;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.valuetypes.InstantMilliseconds;

public class ServiceControl extends SysMLClass implements StackedProtocolObject
{
	@Attribute
	public ServiceStatesEnum state;
	@Attribute
	public InstantMilliseconds time;

	public ServiceControl(ServiceStatesEnum state, InstantMilliseconds time)
	{
		super();
		this.state = state;
		this.time = time;
	}

	@Override
	public String stackNamesString()
	{
		return String.format("state=%s", state);
	}

	@Override
	public String toString()
	{
		return String.format("ServiceControl [state=%s, time=%s]", state, time);
	}
}
