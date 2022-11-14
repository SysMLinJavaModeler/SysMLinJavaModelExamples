package c4s2.common.events;

import c4s2.common.objects.information.OperatorRadarMonitorView;
import c4s2.common.signals.OperatorRadarMonitorViewSignal;
import sysmlinjava.events.SysMLSignalEvent;

public class OperatorRadarMonitorViewEvent extends SysMLSignalEvent
{
	public OperatorRadarMonitorViewEvent(OperatorRadarMonitorView monitorView)
	{
		super("OperatorRadarMonitorView");
		((OperatorRadarMonitorViewSignal)signal).monitorView = new OperatorRadarMonitorView(monitorView);
	}

	public OperatorRadarMonitorView getMonitorView()
	{
		return ((OperatorRadarMonitorViewSignal)signal).monitorView;
	}

	@Override
	public void createSignal()
	{
		signal = new OperatorRadarMonitorViewSignal(new OperatorRadarMonitorView());
	}
}
