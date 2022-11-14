package c4s2.common.objects.information;

import c4s2.common.valueTypes.C4S2OperatorStatesEnum;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;

public class OperatorControl extends SysMLClass
{	
	@Attribute
	public C4S2OperatorStatesEnum state;

	public OperatorControl(C4S2OperatorStatesEnum state)
	{
		super();
		this.state = state;
	}
}
