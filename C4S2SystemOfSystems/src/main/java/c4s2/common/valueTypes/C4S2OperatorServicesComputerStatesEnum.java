package c4s2.common.valueTypes;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.valuetypes.SysMLEnumeration;

public class C4S2OperatorServicesComputerStatesEnum extends SysMLEnumeration<C4S2OperatorServicesComputerStatesEnum>
{
	@Attribute public static final C4S2OperatorServicesComputerStatesEnum Initial = new C4S2OperatorServicesComputerStatesEnum("Initial", 0);
	@Attribute public static final C4S2OperatorServicesComputerStatesEnum PowerOff = new C4S2OperatorServicesComputerStatesEnum("PowerOff", 1);
	@Attribute public static final C4S2OperatorServicesComputerStatesEnum Initializing = new C4S2OperatorServicesComputerStatesEnum("Initializing", 2);
	@Attribute public static final C4S2OperatorServicesComputerStatesEnum Operational = new C4S2OperatorServicesComputerStatesEnum("Operational", 3);
	@Attribute public static final C4S2OperatorServicesComputerStatesEnum Final = new C4S2OperatorServicesComputerStatesEnum("Final", 4);
	public static final C4S2OperatorServicesComputerStatesEnum[] values = {Initial, PowerOff, Initializing, Operational, Final};
	
	private C4S2OperatorServicesComputerStatesEnum(String name, int ordinal)
	{
		super(name, ordinal);
	}

	public static C4S2OperatorServicesComputerStatesEnum valueOf(String name)
	{
		return SysMLEnumeration.valueOf(name, values);
	}

	public static C4S2OperatorServicesComputerStatesEnum valueOf(int ordinal)
	{
		return SysMLEnumeration.valueOf(ordinal, values);
	}

	public static C4S2OperatorServicesComputerStatesEnum[] values()
	{
		return values;
	}
}
