package c4s2.common.items.information;

import sysmlinjava.javaannotations.attributes.Attribute;

@SuppressWarnings("javadoc")
public class OperatorTargetMonitorView extends OperatorMonitorView
{
	@Attribute
	public TargetMonitor monitor;

	public OperatorTargetMonitorView(TargetMonitor monitor)
	{
		super();
		this.monitor = monitor;
	}

	public OperatorTargetMonitorView()
	{
		monitor = new TargetMonitor();
	}

	@Override
	public String stackNamesString()
	{
		return monitor.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("OperatorTargetMonitorView [monitor=%s]", monitor);
	}
}
