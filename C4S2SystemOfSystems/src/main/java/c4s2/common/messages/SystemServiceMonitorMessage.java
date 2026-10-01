package c4s2.common.messages;

import c4s2.common.items.information.SystemServiceMonitor;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjavalibrary.common.messages.Message;

@SuppressWarnings("javadoc")
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
