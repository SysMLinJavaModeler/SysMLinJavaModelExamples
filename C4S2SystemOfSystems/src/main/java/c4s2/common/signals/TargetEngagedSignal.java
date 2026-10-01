package c4s2.common.signals;

import sysmlinjava.attributetypes.BBoolean;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

@SuppressWarnings("javadoc")
public class TargetEngagedSignal extends SysMLSignal
{
	@Attribute
	public BBoolean engaged;

	public TargetEngagedSignal(BBoolean engaged)
	{
		super();
		this.engaged = engaged;
	}

	@Override
	public String stackNamesString()
	{
		return engaged.toString();
	}

	@Override
	public String toString()
	{
		return String.format("TargetEngagedSignal [name=%s, id=%s, engaged=%s]", name, id, engaged.toString());
	}
}
