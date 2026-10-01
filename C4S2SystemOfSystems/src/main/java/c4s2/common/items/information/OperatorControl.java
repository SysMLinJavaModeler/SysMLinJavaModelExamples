package c4s2.common.items.information;

import c4s2.common.attributetypes.C4S2OperatorStatesEnum;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.attributes.Attribute;

@SuppressWarnings("javadoc")
public class OperatorControl extends SysMLAnything
{	
	@Attribute
	public C4S2OperatorStatesEnum state;

	public OperatorControl(C4S2OperatorStatesEnum state)
	{
		super();
		this.state = state;
	}
}
