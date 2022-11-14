package c4s2.common.events;

import c4s2.common.objects.information.OperatorTargetControlView;
import c4s2.common.signals.OperatorTargetControlViewSignal;
import sysmlinjava.events.SysMLSignalEvent;

public class OperatorTargetControlViewEvent extends SysMLSignalEvent
{
	public OperatorTargetControlViewEvent(OperatorTargetControlView controlView)
	{
		super("OperatorTargetControlView");
		((OperatorTargetControlViewSignal)signal).controlView = new OperatorTargetControlView(controlView);
	}

	public OperatorTargetControlView getControlView()
	{
		return ((OperatorTargetControlViewSignal)signal).controlView;
	}

	@Override
	public void createSignal()
	{
		signal = new OperatorTargetControlViewSignal(new OperatorTargetControlView());
	}
}
