package c4s2.common.signals;

import c4s2.common.items.information.StrikeOrdnance;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

@SuppressWarnings("javadoc")
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
