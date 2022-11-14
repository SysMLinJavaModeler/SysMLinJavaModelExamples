package c4s2.common.events;

import c4s2.common.objects.information.OperatorSystemMonitorView;
import c4s2.common.signals.OperatorSystemMonitorViewSignal;
import sysmlinjava.events.SysMLSignalEvent;

public class OperatorSystemMonitorViewEvent extends SysMLSignalEvent
{
	public OperatorSystemMonitorViewEvent(OperatorSystemMonitorView monitorView)
	{
		super("OperatorSystemMonitorView");
		((OperatorSystemMonitorViewSignal)signal).monitorView = new OperatorSystemMonitorView(monitorView);
	}

	public OperatorSystemMonitorView getMonitorView()
	{
		return ((OperatorSystemMonitorViewSignal)signal).monitorView;
	}

	@Override
	public void createSignal()
	{
		signal = new OperatorSystemMonitorViewSignal(new OperatorSystemMonitorView());
	}
}
