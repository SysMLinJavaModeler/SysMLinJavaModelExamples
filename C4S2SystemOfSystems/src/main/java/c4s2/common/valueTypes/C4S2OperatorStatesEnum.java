package c4s2.common.valueTypes;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.valuetypes.SysMLEnumeration;

/**
 * The C4S2OperatorStatesEnum is a SysMLinJava Enumerated ValueType of the
 * states of the C4S2 Operator.
 * 
 * @author ModelerOne
 *
 */
public class C4S2OperatorStatesEnum extends SysMLEnumeration<C4S2OperatorStatesEnum>
{
	@Attribute public static final C4S2OperatorStatesEnum Initializing = new C4S2OperatorStatesEnum("Initializing", 0);
	@Attribute public static final C4S2OperatorStatesEnum Configuring = new C4S2OperatorStatesEnum("Configuring", 1);
	@Attribute public static final C4S2OperatorStatesEnum FindingFixingTrackingTargeting = new C4S2OperatorStatesEnum("FindingFixingTrackingTargeting", 2);
	@Attribute public static final C4S2OperatorStatesEnum Engaging = new C4S2OperatorStatesEnum("Engaging", 3);
	@Attribute public static final C4S2OperatorStatesEnum Assessing = new C4S2OperatorStatesEnum("Assessing", 4);
	@Attribute public static final C4S2OperatorStatesEnum Finalizing = new C4S2OperatorStatesEnum("Finalizing", 5);
	@Attribute public static final C4S2OperatorStatesEnum Final = new C4S2OperatorStatesEnum("Final", 6);
	public static final C4S2OperatorStatesEnum[] values = {Initializing, Configuring, FindingFixingTrackingTargeting, Engaging, Assessing, Finalizing, Final};

	private C4S2OperatorStatesEnum(String name, int ordinal)
	{
		super(name, ordinal);
	}

	public static C4S2OperatorStatesEnum valueOf(String name)
	{
		return SysMLEnumeration.valueOf(name, values);
	}

	public static C4S2OperatorStatesEnum valueOf(int ordinal)
	{
		return SysMLEnumeration.valueOf(ordinal, values);
	}

	public static C4S2OperatorStatesEnum[] values()
	{
		return values;
	}
}
