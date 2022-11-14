package c4s2.common.messages;

import c4s2.common.objects.information.SystemServiceMonitor;
import sysmlinjava.annotations.Attribute;
import sysmlinjavalibrary.common.messages.Message;

public class SystemServiceMonitorMessage extends Message
{
	@Attribute
	public SystemServiceMonitor monitor;

	public SystemServiceMonitorMessage(SystemServiceMonitor monitor)
	{
		super();
		this.monitor = monitor;
	}

	@Override
	public String stackNamesString()
	{
		return monitor.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("SystemServiceMonitorMessage [control=%s]", monitor);
	}
}
