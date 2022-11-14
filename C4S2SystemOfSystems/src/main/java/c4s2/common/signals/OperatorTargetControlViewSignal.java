package c4s2.common.signals;

import c4s2.common.objects.information.OperatorTargetControlView;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

public class OperatorTargetControlViewSignal extends SysMLSignal
{
	@Attribute
	public OperatorTargetControlView controlView;

	public OperatorTargetControlViewSignal(OperatorTargetControlView controlView)
	{
		super();
		this.controlView = controlView;
	}

	@Override
	public String stackNamesString()
	{
		return controlView.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("OperatorTargetControlViewSignal [controlView=%s]", controlView);
	}
}
