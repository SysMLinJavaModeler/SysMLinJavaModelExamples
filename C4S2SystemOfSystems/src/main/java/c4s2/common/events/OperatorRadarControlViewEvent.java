package c4s2.common.events;

import c4s2.common.objects.information.OperatorRadarControlView;
import c4s2.common.signals.OperatorRadarControlViewSignal;
import sysmlinjava.events.SysMLSignalEvent;

public class OperatorRadarControlViewEvent extends SysMLSignalEvent
{
	public OperatorRadarControlViewEvent(OperatorRadarControlView controlView)
	{
		super("OperatorRadarControlView");
		((OperatorRadarControlViewSignal)signal).controlView = new OperatorRadarControlView(controlView);
	}

	public OperatorRadarControlView getControlView()
	{
		return ((OperatorRadarControlViewSignal)signal).controlView;
	}

	@Override
	public void createSignal()
	{
		signal = new OperatorRadarControlViewSignal(new OperatorRadarControlView());
		
	}

}
