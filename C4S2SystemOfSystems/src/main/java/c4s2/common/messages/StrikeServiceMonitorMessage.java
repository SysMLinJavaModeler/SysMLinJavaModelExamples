package c4s2.common.messages;

import c4s2.common.objects.information.StrikeServiceMonitor;
import sysmlinjava.annotations.Attribute;
import sysmlinjavalibrary.common.messages.Message;

public class StrikeServiceMonitorMessage extends Message
{
	@Attribute
	public StrikeServiceMonitor monitor;

	public StrikeServiceMonitorMessage(StrikeServiceMonitor serviceMonitor)
	{
		super();
		this.monitor = serviceMonitor;
	}

	@Override
	public String stackNamesString()
	{
		return monitor.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("StrikeServiceMonitorMessage [control=%s]", monitor);
	}
}
