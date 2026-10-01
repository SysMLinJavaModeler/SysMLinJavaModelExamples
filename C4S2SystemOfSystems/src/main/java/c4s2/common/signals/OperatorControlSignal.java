package c4s2.common.signals;

import c4s2.common.items.information.OperatorControl;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

@SuppressWarnings("javadoc")
public class OperatorControlSignal extends SysMLSignal
{
	@Attribute
	public OperatorControl control;

	public OperatorControlSignal(OperatorControl control)
	{
		super();
		this.control = control;
	}
}
