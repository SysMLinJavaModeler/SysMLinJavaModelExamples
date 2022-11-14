package c4s2.common.events;

import c4s2.common.objects.information.OperatorSystemControlView;
import c4s2.common.signals.OperatorSystemControlViewSignal;
import sysmlinjava.events.SysMLSignalEvent;

public class OperatorSystemControlViewEvent extends SysMLSignalEvent
{
	public OperatorSystemControlViewEvent(OperatorSystemControlView controlView)
	{
		super("OperatorSystemControlView");
		((OperatorSystemControlViewSignal)signal).controlView = new OperatorSystemControlView(controlView);
	}

	public OperatorSystemControlView getControlView()
	{
		return ((OperatorSystemControlViewSignal)signal).controlView;
	}

	@Override
	public void createSignal()
	{
		signal = new OperatorSystemControlViewSignal(new OperatorSystemControlView());
	}
}
