package c4s2.common.messages;

import c4s2.common.objects.information.StrikeSystemMonitor;
import sysmlinjava.annotations.Attribute;
import sysmlinjavalibrary.common.messages.Message;

public class StrikeSystemMonitorMessage extends Message
{
	@Attribute
	public StrikeSystemMonitor monitor;

	public StrikeSystemMonitorMessage(StrikeSystemMonitor strikeSystemMonitor)
	{
		super();
		this.monitor = strikeSystemMonitor;
	}

	@Override
	public String stackNamesString()
	{
		return monitor.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("StrikeSystemMonitorMessage [monitor=%s]", monitor);
	}
}
