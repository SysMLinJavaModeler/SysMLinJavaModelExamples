package dbssystem.common;

import sysmlinjava.attributetypes.BBoolean;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

public class TremorPresenceSignal extends SysMLSignal
{
	@Attribute
	public BBoolean isPresent;

	public TremorPresenceSignal(BBoolean isPresent)
	{
		super();
		this.isPresent = isPresent;
	}

	@Override
	public String stackNamesString()
	{
		return "Tremor present";
	}
}
