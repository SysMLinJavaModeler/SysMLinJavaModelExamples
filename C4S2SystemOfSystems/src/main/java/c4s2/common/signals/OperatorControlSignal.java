package c4s2.common.signals;

import c4s2.common.objects.information.OperatorControl;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

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
