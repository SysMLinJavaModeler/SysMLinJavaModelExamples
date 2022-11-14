package c4s2.common.objects.information;

import sysmlinjava.annotations.Attribute;

public class OperatorStrikeMonitorView extends OperatorMonitorView
{
	@Attribute
	public StrikeMonitor monitor;

	public OperatorStrikeMonitorView(StrikeMonitor monitor)
	{
		super();
		this.monitor = monitor;
	}

	public OperatorStrikeMonitorView()
	{
		this.monitor = new StrikeMonitor();
	}

	public OperatorStrikeMonitorView(OperatorStrikeMonitorView monitorView)
	{
		this.monitor = new StrikeMonitor(monitorView.monitor);
	}

	@Override
	public String stackNamesString()
	{
		return monitor.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("OperatorStrikeMonitorView [monitor=%s]", monitor);
	}
}
