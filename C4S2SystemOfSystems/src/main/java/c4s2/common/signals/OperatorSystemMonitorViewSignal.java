package c4s2.common.signals;

import c4s2.common.objects.information.OperatorSystemMonitorView;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

public class OperatorSystemMonitorViewSignal extends SysMLSignal
{
	@Attribute
	public OperatorSystemMonitorView monitorView;

	public OperatorSystemMonitorViewSignal(OperatorSystemMonitorView monitorView)
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
		return String.format("OperatorSystemMonitorViewSignal [monitorView=%s]", monitorView);
	}
}
