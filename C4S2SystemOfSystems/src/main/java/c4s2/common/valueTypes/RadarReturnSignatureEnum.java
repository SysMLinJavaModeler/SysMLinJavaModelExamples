package c4s2.common.valueTypes;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.valuetypes.SysMLEnumeration;

public class RadarReturnSignatureEnum extends SysMLEnumeration<RadarReturnSignatureEnum>
{
	@Attribute public static final RadarReturnSignatureEnum unknown = new RadarReturnSignatureEnum("unknown", 0);
	@Attribute public static final RadarReturnSignatureEnum vehicleSmallArmored = new RadarReturnSignatureEnum("vehicleSmallArmored", 1);
	@Attribute public static final RadarReturnSignatureEnum vehicleSmallWheeled = new RadarReturnSignatureEnum("vehicleSmallWheeled", 2);
	@Attribute public static final RadarReturnSignatureEnum vehicleLargeArmored = new RadarReturnSignatureEnum("vehicleLargeArmored", 3);
	@Attribute public static final RadarReturnSignatureEnum vehicleLargeWheeled = new RadarReturnSignatureEnum("vehicleLargeWheeled", 4);
	@Attribute public static final RadarReturnSignatureEnum vehicleDestroyed = new RadarReturnSignatureEnum("vehicleDestroyed", 5);
	@Attribute public static final RadarReturnSignatureEnum structureSmall = new RadarReturnSignatureEnum("structureSmall", 6);
	@Attribute public static final RadarReturnSignatureEnum structureLarge = new RadarReturnSignatureEnum("structureLarge", 7);
	@Attribute public static final RadarReturnSignatureEnum structureDestroyed = new RadarReturnSignatureEnum("structureDestroyed", 7);
	public static final RadarReturnSignatureEnum[] values = {unknown, vehicleSmallArmored, vehicleSmallWheeled, vehicleLargeArmored, vehicleLargeWheeled, vehicleDestroyed, structureSmall, structureLarge, structureDestroyed};

	private RadarReturnSignatureEnum(String name, int ordinal)
	{
		super(name, ordinal);
	}

	public static RadarReturnSignatureEnum valueOf(String name)
	{
		return SysMLEnumeration.valueOf(name, values);
	}

	public static RadarReturnSignatureEnum valueOf(int ordinal)
	{
		return SysMLEnumeration.valueOf(ordinal, values);
	}

	public static RadarReturnSignatureEnum[] values()
	{
		return values;
	}	
}