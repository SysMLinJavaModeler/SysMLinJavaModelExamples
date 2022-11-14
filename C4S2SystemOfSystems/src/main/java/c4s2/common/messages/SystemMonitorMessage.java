package c4s2.common.messages;

import c4s2.common.objects.information.SystemMonitor;
import sysmlinjava.annotations.Attribute;
import sysmlinjavalibrary.common.messages.Message;

public class SystemMonitorMessage extends Message
{
	@Attribute
	public SystemMonitor monitor;

	public SystemMonitorMessage(SystemMonitor monitor)
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
		return String.format("SystemMonitorMessage [control=%s]", monitor);
	}
}
