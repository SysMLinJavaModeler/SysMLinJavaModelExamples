package c4s2.common.valueTypes;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.valuetypes.SysMLEnumeration;

public class StrikeSystemStatesEnum extends SysMLEnumeration<StrikeSystemStatesEnum>
{
	@Attribute public static final StrikeSystemStatesEnum initializing = new StrikeSystemStatesEnum("initializing", 0);
	@Attribute public static final StrikeSystemStatesEnum standingby = new StrikeSystemStatesEnum("standingby", 1);
	@Attribute public static final StrikeSystemStatesEnum striking = new StrikeSystemStatesEnum("striking", 2);
	@Attribute public static final StrikeSystemStatesEnum returning = new StrikeSystemStatesEnum("returning", 3);
	@Attribute public static final StrikeSystemStatesEnum returned = new StrikeSystemStatesEnum("returned", 4);
	@Attribute public static final StrikeSystemStatesEnum detached = new StrikeSystemStatesEnum("detached", 5);
	public static final StrikeSystemStatesEnum[] values = {initializing, standingby, striking, returning, returned, detached};
	
	private StrikeSystemStatesEnum(String name, int ordinal)
	{
		super(name, ordinal);
	}

	public StrikeSystemStatesEnum(StrikeSystemStatesEnum copyOf)
	{
		super(copyOf.name, copyOf.ordinal);
	}

	public static StrikeSystemStatesEnum valueOf(String name)
	{
		return SysMLEnumeration.valueOf(name, values);
	}

	public static StrikeSystemStatesEnum valueOf(int ordinal)
	{
		return SysMLEnumeration.valueOf(ordinal, values);
	}

	public static StrikeSystemStatesEnum[] values()
	{
		return values;
	}
}
