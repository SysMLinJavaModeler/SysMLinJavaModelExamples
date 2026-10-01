package c4s2.common.messages;

import c4s2.common.items.information.TargetServiceMonitor;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjavalibrary.common.messages.Message;

@SuppressWarnings("javadoc")
public class TargetServiceMonitorMessage extends Message
{
	@Attribute
	public TargetServiceMonitor monitor;

	public TargetServiceMonitorMessage(TargetServiceMonitor monitor)
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
		return String.format("TargetServiceMonitorMessage [control=%s]", monitor);
	}
}
