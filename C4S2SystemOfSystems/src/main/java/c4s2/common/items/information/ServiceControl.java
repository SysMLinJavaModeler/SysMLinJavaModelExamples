package c4s2.common.items.information;

import c4s2.common.attributetypes.ServiceStatesEnum;
import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.common.StackedProtocolObject;

@SuppressWarnings("javadoc")
public class ServiceControl extends SysMLAnything implements StackedProtocolObject
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
