package c4s2.common.signals;

import c4s2.common.items.information.OperatorTargetMonitorView;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

@SuppressWarnings("javadoc")
public class OperatorTargetMonitorViewSignal extends SysMLSignal
{
	@Attribute
	public OperatorTargetMonitorView monitorView;

	public OperatorTargetMonitorViewSignal(OperatorTargetMonitorView monitorView)
	{
		super();
		this.monitorView = monitorView;
	}

	@Override
	public String stackNamesString()
	{
		return monitorView.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("OperatorTargetMonitorViewSignal [monitorView=%s]", monitorView);
	}
}
