package c4s2.common.events;

import c4s2.common.objects.information.OperatorTargetMonitorView;
import c4s2.common.signals.OperatorTargetMonitorViewSignal;
import sysmlinjava.events.SysMLSignalEvent;

public class OperatorTargetMonitorViewEvent extends SysMLSignalEvent
{
	public OperatorTargetMonitorViewEvent(OperatorTargetMonitorView monitorView)
	{
		super("OperatorTargetMonitorView");
		((OperatorTargetMonitorViewSignal)signal).monitorView = new OperatorTargetMonitorView(monitorView.monitor);
	}

	public OperatorTargetMonitorView getMonitorView()
	{
		return ((OperatorTargetMonitorViewSignal)signal).monitorView;
	}

	@Override
	public void createSignal()
	{
		signal = new OperatorTargetMonitorViewSignal(new OperatorTargetMonitorView());
	}
}
