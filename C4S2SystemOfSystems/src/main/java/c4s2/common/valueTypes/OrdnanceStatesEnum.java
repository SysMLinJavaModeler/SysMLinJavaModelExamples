package c4s2.common.valueTypes;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.valuetypes.SysMLEnumeration;

/**
 * The OrdnanceStatesEnum is a SysMLinJava Enumerated ValueType of the states of
 * the {@code StrikeOrdnance}.
 * 
 * @author ModelerOne
 *
 */
public class OrdnanceStatesEnum extends SysMLEnumeration<OrdnanceStatesEnum>
{
	@Attribute public static final OrdnanceStatesEnum unarmed = new OrdnanceStatesEnum("unarmed", 0);
	@Attribute public static final OrdnanceStatesEnum armed = new OrdnanceStatesEnum("armed", 1);
	@Attribute public static final OrdnanceStatesEnum spent = new OrdnanceStatesEnum("spent", 2);
	public static final OrdnanceStatesEnum[] values = {unarmed, armed, spent};

	private OrdnanceStatesEnum(String name, int ordinal)
	{
		super(name, ordinal);
	}

	public static OrdnanceStatesEnum valueOf(String name)
	{
		return SysMLEnumeration.valueOf(name, values);
	}

	public static OrdnanceStatesEnum valueOf(int ordinal)
	{
		return SysMLEnumeration.valueOf(ordinal, values);
	}

	public static OrdnanceStatesEnum[] values()
	{
		return values;
	}
}
