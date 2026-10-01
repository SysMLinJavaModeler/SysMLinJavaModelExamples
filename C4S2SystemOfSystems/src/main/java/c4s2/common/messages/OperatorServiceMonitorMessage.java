package c4s2.common.messages;

import c4s2.common.items.information.OperatorServiceMonitor;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjavalibrary.common.messages.Message;

@SuppressWarnings("javadoc")
public class OperatorServiceMonitorMessage extends Message
{
	@Attribute
	public OperatorServiceMonitor monitor;

	public OperatorServiceMonitorMessage(OperatorServiceMonitor monitor)
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
		return String.format("OperatorServiceMonitorMessage [control=%s]", monitor);
	}
}
