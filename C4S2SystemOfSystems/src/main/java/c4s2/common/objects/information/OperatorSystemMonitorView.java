package c4s2.common.objects.information;

import sysmlinjava.annotations.Attribute;

public class OperatorSystemMonitorView extends OperatorMonitorView
{
	@Attribute
	public SystemMonitor monitor;

	public OperatorSystemMonitorView(SystemMonitor monitor)
	{
		super();
		this.monitor = monitor;
	}

	public OperatorSystemMonitorView()
	{
		this.monitor = new SystemMonitor();
	}

	public OperatorSystemMonitorView(OperatorSystemMonitorView monitorView)
	{
		super();
		this.monitor = new SystemMonitor(monitorView.monitor);
	}

	@Override
	public String stackNamesString()
	{
		return monitor.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("OperatorSystemMonitorView [monitor=%s]", monitor);
	}
}
