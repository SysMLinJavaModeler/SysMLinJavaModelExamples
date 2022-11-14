package c4s2.common.events;

import c4s2.common.objects.information.OperatorControl;
import c4s2.common.signals.OperatorControlSignal;
import c4s2.common.valueTypes.C4S2OperatorStatesEnum;
import sysmlinjava.events.SysMLSignalEvent;

public class C4S2OperatorControlEvent extends SysMLSignalEvent
{
	public C4S2OperatorControlEvent(OperatorControl control)
	{
		super("OperatorControl");
		((OperatorControlSignal)signal).control.state = control.state;
	}

	public OperatorControl getControl()
	{
		return ((OperatorControlSignal)signal).control;
	}

	@Override
	public void createSignal()
	{
		signal = new OperatorControlSignal(new OperatorControl(C4S2OperatorStatesEnum.Configuring));
		
	}
}
