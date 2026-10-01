package c4s2.common.messages;

import c4s2.common.items.information.RadarMonitor;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjavalibrary.common.messages.Message;

@SuppressWarnings("javadoc")
public class RadarMonitorMessage extends Message
{
	@Attribute
	public RadarMonitor monitor;

	public RadarMonitorMessage(RadarMonitor monitor)
	{
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
		return String.format("RadarMonitorMessage [monitor=%s]", monitor);
	}
}
