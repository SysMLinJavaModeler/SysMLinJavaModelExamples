package c4s2.common.signals;

import c4s2.common.items.information.OperatorSystemMonitorView;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

@SuppressWarnings("javadoc")
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
