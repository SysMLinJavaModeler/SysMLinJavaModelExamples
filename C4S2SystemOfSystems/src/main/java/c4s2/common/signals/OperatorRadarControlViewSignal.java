package c4s2.common.signals;

import c4s2.common.items.information.OperatorRadarControlView;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

@SuppressWarnings("javadoc")
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
