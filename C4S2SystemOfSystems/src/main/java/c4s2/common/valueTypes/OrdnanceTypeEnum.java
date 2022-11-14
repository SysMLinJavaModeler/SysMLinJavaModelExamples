package c4s2.common.valueTypes;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.valuetypes.DistanceMeters;
import sysmlinjava.valuetypes.EnergyTonsTNT;
import sysmlinjava.valuetypes.SysMLEnumeration;

/**
 * The OrdnanceTypeEnum is a SysMLinJava Enumerated ValueType of the types of
 * {@code StrikeOrdnance}.
 * 
 * @author ModelerOne
 *
 */
public class OrdnanceTypeEnum extends SysMLEnumeration<OrdnanceTypeEnum>
{
	@Attribute public static final OrdnanceTypeEnum small = new OrdnanceTypeEnum("small", 0, new DistanceMeters(50), new EnergyTonsTNT(0.005));
	@Attribute public static final OrdnanceTypeEnum medium = new OrdnanceTypeEnum("medium", 0, new DistanceMeters(100), new EnergyTonsTNT(0.05));
	@Attribute public static final OrdnanceTypeEnum large = new OrdnanceTypeEnum("large", 0, new DistanceMeters(200), new EnergyTonsTNT(0.5));
	public static final OrdnanceTypeEnum[] values = {small, medium, large};

	public DistanceMeters range;
	public EnergyTonsTNT yield;

	private OrdnanceTypeEnum(String name, int ordinal, DistanceMeters range, EnergyTonsTNT yield)
	{
		super(name, ordinal);
		this.range = range;
		this.yield = yield;
	}

	public static OrdnanceTypeEnum valueOf(String name)
	{
		return SysMLEnumeration.valueOf(name, values);
	}

	public static OrdnanceTypeEnum valueOf(int ordinal)
	{
		return SysMLEnumeration.valueOf(ordinal, values);
	}

	public static OrdnanceTypeEnum[] values()
	{
		return values;
	}

	public DistanceMeters getRange()
	{
		return range;
	}

	public EnergyTonsTNT getYield()
	{
		return yield;
	}
}