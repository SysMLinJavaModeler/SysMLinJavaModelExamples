package c4s2.common.items.information;

import c4s2.common.attributetypes.ServiceStatesEnum;
import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.common.StackedProtocolObject;

@SuppressWarnings("javadoc")
public class ServiceMonitor extends SysMLAnything implements StackedProtocolObject
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
