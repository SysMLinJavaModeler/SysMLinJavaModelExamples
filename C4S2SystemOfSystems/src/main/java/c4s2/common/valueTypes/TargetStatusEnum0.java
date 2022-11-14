package c4s2.common.valueTypes;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.valuetypes.SysMLEnumeration;

public class TargetStatusEnum0 extends SysMLEnumeration<TargetStatusEnum0>
{
	/**
	* target is operating normally
	*/
	@Attribute public static final TargetStatusEnum0 operating = new TargetStatusEnum0("operating", 0);
	/**
	* target is exploding
	*/
	@Attribute public static final TargetStatusEnum0 exploding = new TargetStatusEnum0("exploding", 1);
	/**
	* taget is destroyed
	*/
	@Attribute public static final TargetStatusEnum0 destroyed = new TargetStatusEnum0("destroyed", 2);
	public static final TargetStatusEnum0[] values = {operating,exploding,destroyed};

	private TargetStatusEnum0(String name, int ordinal)
	{
		super(name, ordinal);
	}

	public TargetStatusEnum0(TargetStatusEnum0 copyOf)
	{
		super(copyOf.name, copyOf.ordinal);
	}

	public static TargetStatusEnum0 valueOf(String name)
	{
		return SysMLEnumeration.valueOf(name, values);
	}

	public static TargetStatusEnum0 valueOf(int ordinal)
	{
		return SysMLEnumeration.valueOf(ordinal, values);
	}

	public static TargetStatusEnum0[] values()
	{
		return values;
	}
}


