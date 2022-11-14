package c4s2.common.signals;

import c4s2.common.objects.information.OperatorRadarMonitorView;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

public class OperatorRadarMonitorViewSignal extends SysMLSignal
{
	@Attribute
	public OperatorRadarMonitorView monitorView;

	public OperatorRadarMonitorViewSignal(OperatorRadarMonitorView monitorView)
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
		return String.format("OperatorRadarMonitorViewSignal [monitorView=%s]", monitorView);
	}
}
