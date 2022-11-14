package c4s2.common.events;

import c4s2.common.objects.information.OperatorStrikeMonitorView;
import c4s2.common.signals.OperatorStrikeMonitorViewSignal;
import sysmlinjava.events.SysMLSignalEvent;

public class OperatorStrikeMonitorViewEvent extends SysMLSignalEvent
{
	public OperatorStrikeMonitorViewEvent(OperatorStrikeMonitorView monitorView)
	{
		super("OperatorStrikeMonitorView");
		((OperatorStrikeMonitorViewSignal)signal).monitorView = new OperatorStrikeMonitorView(monitorView);
	}

	public OperatorStrikeMonitorView getMonitorView()
	{
		return ((OperatorStrikeMonitorViewSignal)signal).monitorView;
	}

	@Override
	public void createSignal()
	{
		signal = new OperatorStrikeMonitorViewSignal(new OperatorStrikeMonitorView());
	}
}
