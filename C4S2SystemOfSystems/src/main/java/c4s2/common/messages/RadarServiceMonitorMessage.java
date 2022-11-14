package c4s2.common.messages;

import c4s2.common.objects.information.RadarServiceMonitor;
import sysmlinjava.annotations.Attribute;
import sysmlinjavalibrary.common.messages.Message;

public class RadarServiceMonitorMessage extends Message
{
	@Attribute
	public RadarServiceMonitor monitor;

	public RadarServiceMonitorMessage(RadarServiceMonitor monitor)
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
		return String.format("RadarServiceMonitorMessage [control=%s]", monitor);
	}
}
