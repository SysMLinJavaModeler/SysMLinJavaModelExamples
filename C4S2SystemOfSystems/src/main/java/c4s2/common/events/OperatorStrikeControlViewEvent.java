package c4s2.common.events;

import c4s2.common.objects.information.OperatorStrikeControlView;
import c4s2.common.signals.OperatorStrikeControlViewSignal;
import sysmlinjava.events.SysMLSignalEvent;

public class OperatorStrikeControlViewEvent extends SysMLSignalEvent
{
	public OperatorStrikeControlViewEvent(OperatorStrikeControlView controlView)
	{
		super("OperatorStrikeControlView");
		((OperatorStrikeControlViewSignal)signal).controlView = new OperatorStrikeControlView(controlView);
	}

	public OperatorStrikeControlView getControlView()
	{
		return ((OperatorStrikeControlViewSignal)signal).controlView;
	}

	@Override
	public void createSignal()
	{
		signal = new OperatorStrikeControlViewSignal(new OperatorStrikeControlView());
	}
}
