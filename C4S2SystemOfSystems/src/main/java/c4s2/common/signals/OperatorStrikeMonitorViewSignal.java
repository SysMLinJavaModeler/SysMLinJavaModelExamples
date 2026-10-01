package c4s2.common.signals;

import c4s2.common.items.information.OperatorStrikeMonitorView;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

@SuppressWarnings("javadoc")
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
