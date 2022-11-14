package c4s2.common.signals;

import c4s2.common.objects.information.OperatorTargetMonitorView;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

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
