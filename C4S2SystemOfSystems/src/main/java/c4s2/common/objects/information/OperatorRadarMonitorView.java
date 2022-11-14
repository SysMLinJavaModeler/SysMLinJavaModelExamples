package c4s2.common.objects.information;

import sysmlinjava.annotations.Attribute;

public class OperatorRadarMonitorView extends OperatorMonitorView
{
	@Attribute
	public RadarMonitor monitor;

	public OperatorRadarMonitorView(RadarMonitor monitor)
	{
		super();
		this.monitor = monitor;
	}

	public OperatorRadarMonitorView()
	{
		this.monitor = new RadarMonitor();
	}

	public OperatorRadarMonitorView(OperatorRadarMonitorView monitorView)
	{
		this.monitor = new RadarMonitor(monitorView.monitor);
	}

	@Override
	public String stackNamesString()
	{
		return monitor.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("OperatorRadarMonitorView [monitor=%s]", monitor);
	}
}
