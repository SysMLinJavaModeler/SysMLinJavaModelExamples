package c4s2.common.messages;

import c4s2.common.items.information.SystemMonitor;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjavalibrary.common.messages.Message;

@SuppressWarnings("javadoc")
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
