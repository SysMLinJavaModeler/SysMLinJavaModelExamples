package c4s2.common.valueTypes;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.valuetypes.SysMLEnumeration;

public class C4S2ServicesComputerStatesEnum extends SysMLEnumeration<C4S2ServicesComputerStatesEnum>
{
	@Attribute public static final C4S2ServicesComputerStatesEnum Initial = new C4S2ServicesComputerStatesEnum("Initial", 0);
	@Attribute public static final C4S2ServicesComputerStatesEnum PowerOff = new C4S2ServicesComputerStatesEnum("PowerOff", 1);
	@Attribute public static final C4S2ServicesComputerStatesEnum Initializing = new C4S2ServicesComputerStatesEnum("Initializing", 2);
	@Attribute public static final C4S2ServicesComputerStatesEnum Operational = new C4S2ServicesComputerStatesEnum("Operational", 3);
	@Attribute public static final C4S2ServicesComputerStatesEnum Final = new C4S2ServicesComputerStatesEnum("Final", 4);
	@Attribute public static final C4S2ServicesComputerStatesEnum[] values = {Initial, PowerOff, Initializing, Operational, Final};

	private C4S2ServicesComputerStatesEnum(String name, int ordinal)
	{
		super(name, ordinal);
	}

	public static C4S2ServicesComputerStatesEnum valueOf(String name)
	{
		return SysMLEnumeration.valueOf(name, values);
	}

	public static C4S2ServicesComputerStatesEnum valueOf(int ordinal)
	{
		return SysMLEnumeration.valueOf(ordinal, values);
	}

	public static C4S2ServicesComputerStatesEnum[] values()
	{
		return values;
	}
}
