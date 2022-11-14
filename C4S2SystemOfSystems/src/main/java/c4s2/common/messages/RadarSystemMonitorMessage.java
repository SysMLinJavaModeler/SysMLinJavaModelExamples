package c4s2.common.messages;

import c4s2.common.objects.information.RadarSystemMonitor;
import sysmlinjava.annotations.Attribute;
import sysmlinjavalibrary.common.messages.Message;

public class RadarSystemMonitorMessage extends Message
{
	@Attribute
	public RadarSystemMonitor monitor;

	public RadarSystemMonitorMessage(RadarSystemMonitor monitor)
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
		return String.format("RadarSystemMonitorMessage [monitor=%s]", monitor);
	}

}
