package c4s2.common.signals;

import c4s2.common.items.information.OperatorSystemControlView;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

@SuppressWarnings("javadoc")
public class OperatorSystemControlViewSignal extends SysMLSignal
{
	@Attribute
	public OperatorSystemControlView controlView;

	public OperatorSystemControlViewSignal(OperatorSystemControlView controlView)
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
		return String.format("OperatorSystemControlViewSignal [controlView=%s]", controlView);
	}
}
