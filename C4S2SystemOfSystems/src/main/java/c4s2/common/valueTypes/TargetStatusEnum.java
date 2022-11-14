package c4s2.common.valueTypes;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.valuetypes.SysMLEnumeration;

public class TargetStatusEnum extends SysMLEnumeration<TargetStatusEnum>
{
	@Attribute public static final TargetStatusEnum operating = new TargetStatusEnum("operating", 0);
	@Attribute public static final TargetStatusEnum exploding = new TargetStatusEnum("exploding", 1);
	@Attribute public static final TargetStatusEnum destroyed = new TargetStatusEnum("destroyed", 2);
	public static final TargetStatusEnum[] values = {operating, exploding, destroyed};
	
	private TargetStatusEnum(String name, int ordinal)
	{
		super(name, ordinal);
	}

	public TargetStatusEnum(TargetStatusEnum copyOf)
	{
		super(copyOf.name, copyOf.ordinal);
	}

	public static TargetStatusEnum valueOf(String name)
	{
		return SysMLEnumeration.valueOf(name, values);
	}

	public static TargetStatusEnum valueOf(int ordinal)
	{
		return SysMLEnumeration.valueOf(ordinal, values);
	}

	public static TargetStatusEnum[] values()
	{
		return values;
	}
}
