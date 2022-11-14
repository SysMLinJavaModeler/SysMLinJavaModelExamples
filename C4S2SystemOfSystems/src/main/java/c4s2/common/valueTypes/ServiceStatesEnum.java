package c4s2.common.valueTypes;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.valuetypes.SysMLEnumeration;

public class ServiceStatesEnum extends SysMLEnumeration<ServiceStatesEnum>
{
	public static final int Initial = 0;
	public static final int Initializing = 1;
	public static final int Operational = 2;
	public static final int Final = 3;
	@Attribute public static final ServiceStatesEnum initial = new ServiceStatesEnum("initial", Initial);
	@Attribute public static final ServiceStatesEnum initializing = new ServiceStatesEnum("initializing", Initializing);
	@Attribute public static final ServiceStatesEnum operational = new ServiceStatesEnum("operational", Operational);
	@Attribute public static final ServiceStatesEnum finall = new ServiceStatesEnum("final", Final);
	public static final ServiceStatesEnum[] values = {initial, initializing, operational, finall};

	private ServiceStatesEnum(String name, int ordinal)
	{
		super(name, ordinal);
	}

	public static ServiceStatesEnum valueOf(String name)
	{
		return SysMLEnumeration.valueOf(name, values);
	}

	public static ServiceStatesEnum valueOf(int ordinal)
	{
		return SysMLEnumeration.valueOf(ordinal, values);
	}

	public static ServiceStatesEnum[] values()
	{
		return values;
	}
}
