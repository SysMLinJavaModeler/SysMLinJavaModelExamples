package c4s2.common.messages;

import c4s2.common.items.information.StrikeSystemMonitor;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjavalibrary.common.messages.Message;

@SuppressWarnings("javadoc")
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
