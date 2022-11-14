package c4s2.common.valueTypes;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.valuetypes.SysMLEnumeration;

public class C4S2SystemComponentsEnum extends SysMLEnumeration<C4S2SystemComponentsEnum>
{
	@Attribute public static final C4S2SystemComponentsEnum C4S2ServicesComputer = new C4S2SystemComponentsEnum("C4S2ServicesComputer", 0);
	@Attribute public static final C4S2SystemComponentsEnum C4S2OperatorServicesComputer = new C4S2SystemComponentsEnum("C4S2OperatorServicesComputer", 1);
	@Attribute public static final C4S2SystemComponentsEnum EthernetSwitchIPRouter = new C4S2SystemComponentsEnum("EthernetSwitchIPRouter", 2);
	@Attribute public static final C4S2SystemComponentsEnum SIPRNetRouter = new C4S2SystemComponentsEnum("SIPRNetRouter", 3);
	@Attribute public static final C4S2SystemComponentsEnum[] values = {C4S2ServicesComputer, C4S2OperatorServicesComputer, EthernetSwitchIPRouter, SIPRNetRouter};

	private C4S2SystemComponentsEnum(String name, int ordinal)
	{
		super(name, ordinal);
	}

	public static C4S2SystemComponentsEnum valueOf(String name)
	{
		return SysMLEnumeration.valueOf(name, values);
	}

	public static C4S2SystemComponentsEnum valueOf(int ordinal)
	{
		return SysMLEnumeration.valueOf(ordinal, values);
	}

	public static C4S2SystemComponentsEnum[] values()
	{
		return values;
	}
}
