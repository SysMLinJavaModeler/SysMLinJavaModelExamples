package c4s2.common.signals;

import c4s2.common.objects.information.OperatorRadarControlView;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

public class OperatorRadarControlViewSignal extends SysMLSignal
{
	@Attribute
	public OperatorRadarControlView controlView;

	public OperatorRadarControlViewSignal(OperatorRadarControlView controlView)
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
		return String.format("OperatorRadarControlViewSignal [controlView=%s]", controlView);
	}
}
