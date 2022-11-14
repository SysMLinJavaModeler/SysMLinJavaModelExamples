package c4s2.common.messages;

import c4s2.common.objects.information.StrikeMonitor;
import sysmlinjava.annotations.Attribute;
import sysmlinjavalibrary.common.messages.Message;

public class StrikeMonitorMessage extends Message
{
	@Attribute
	public StrikeMonitor monitor;

	public StrikeMonitorMessage(StrikeMonitor monitor)
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
		return String.format("StrikeMonitorMessage [monitor=%s]", monitor);
	}
}
