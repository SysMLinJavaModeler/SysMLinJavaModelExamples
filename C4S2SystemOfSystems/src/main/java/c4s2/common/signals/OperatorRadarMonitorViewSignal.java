package c4s2.common.signals;

import c4s2.common.items.information.OperatorRadarMonitorView;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

@SuppressWarnings("javadoc")
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
