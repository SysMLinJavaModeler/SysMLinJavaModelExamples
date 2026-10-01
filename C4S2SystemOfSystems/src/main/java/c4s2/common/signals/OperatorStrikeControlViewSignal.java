package c4s2.common.signals;

import c4s2.common.items.information.OperatorStrikeControlView;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

@SuppressWarnings("javadoc")
public class OperatorStrikeControlViewSignal extends SysMLSignal
{
	@Attribute
	public OperatorStrikeControlView controlView;

	public OperatorStrikeControlViewSignal(OperatorStrikeControlView controlView)
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
		return String.format("OperatorStrikeControlViewSignal [controlView=%s]", controlView);
	}
}
