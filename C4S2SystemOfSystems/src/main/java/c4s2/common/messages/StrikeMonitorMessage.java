package c4s2.common.messages;

import c4s2.common.items.information.StrikeMonitor;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjavalibrary.common.messages.Message;

@SuppressWarnings("javadoc")
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
