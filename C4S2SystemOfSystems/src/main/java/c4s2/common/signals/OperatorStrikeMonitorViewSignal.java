package c4s2.common.signals;

import c4s2.common.objects.information.OperatorStrikeMonitorView;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

public class OperatorStrikeMonitorViewSignal extends SysMLSignal
{
	@Attribute
	public OperatorStrikeMonitorView monitorView;

	public OperatorStrikeMonitorViewSignal(OperatorStrikeMonitorView monitorView)
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
		return String.format("OperatorStrikeMonitorViewSignal [monitorView=%s]", monitorView);
	}
}
