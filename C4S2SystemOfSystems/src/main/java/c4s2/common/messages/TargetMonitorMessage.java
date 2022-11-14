package c4s2.common.messages;

import c4s2.common.objects.information.TargetMonitor;
import sysmlinjava.annotations.Attribute;
import sysmlinjavalibrary.common.messages.Message;

public class TargetMonitorMessage extends Message
{
	@Attribute
	public TargetMonitor monitor;

	public TargetMonitorMessage(TargetMonitor monitor)
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
		return String.format("TargetMonitorMessage [monitor=%s]", monitor);
	}
}
