package c4s2.common.valueTypes;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.valuetypes.SysMLEnumeration;

public class TargetDevelopmentAlgorithmsEnum extends SysMLEnumeration<TargetDevelopmentAlgorithmsEnum>
{
	public static final int simple = 0;
	public static final int difficult = 1;
	public static final int complex = 2;
	@Attribute public static final TargetDevelopmentAlgorithmsEnum Simple = new TargetDevelopmentAlgorithmsEnum("Simple", simple);
	@Attribute public static final TargetDevelopmentAlgorithmsEnum Difficult = new TargetDevelopmentAlgorithmsEnum("Difficult", difficult);
	@Attribute public static final TargetDevelopmentAlgorithmsEnum Complex = new TargetDevelopmentAlgorithmsEnum("Complex", complex);
	@Attribute public static final TargetDevelopmentAlgorithmsEnum[] values = {Simple, Difficult, Complex};

	private TargetDevelopmentAlgorithmsEnum(String name, int ordinal)
	{
		super(name, ordinal);
	}

	public static TargetDevelopmentAlgorithmsEnum valueOf(String name)
	{
		return SysMLEnumeration.valueOf(name, values);
	}

	public static TargetDevelopmentAlgorithmsEnum valueOf(int ordinal)
	{
		return SysMLEnumeration.valueOf(ordinal, values);
	}

	public static TargetDevelopmentAlgorithmsEnum[] values()
	{
		return values;
	}
}
