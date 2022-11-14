package c4s2.common.signals;

import c4s2.common.objects.information.StrikeOrdnance;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

public class StrikeOrdnanceSignal extends SysMLSignal
{
	@Attribute
	public StrikeOrdnance ordnance;

	public StrikeOrdnanceSignal(StrikeOrdnance ordnance)
	{
		super();
		this.ordnance = ordnance;
	}

	@Override
	public String stackNamesString()
	{
		return ordnance.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("StrikeOrdnanceSignal [name=%s, id=%s, ordnance=%s]", name, id, ordnance);
	}
}
