package c4s2.common.valueTypes;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.valuetypes.SysMLEnumeration;

public class RadarSystemStatesEnum extends SysMLEnumeration<RadarSystemStatesEnum>
{
	@Attribute public static final RadarSystemStatesEnum Initial = new RadarSystemStatesEnum("Initial", 0);
	@Attribute public static final RadarSystemStatesEnum Initializing = new RadarSystemStatesEnum("Initializing", 1);
	@Attribute public static final RadarSystemStatesEnum Idle = new RadarSystemStatesEnum("Idle", 2);
	@Attribute public static final RadarSystemStatesEnum F2T2Scanning = new RadarSystemStatesEnum("F2T2Scanning", 3);
	@Attribute public static final RadarSystemStatesEnum EAScanning = new RadarSystemStatesEnum("EAScanning", 4);
	@Attribute public static final RadarSystemStatesEnum Detached = new RadarSystemStatesEnum("Detached", 5);
	public static final RadarSystemStatesEnum[] values = {Initial, Initializing, Idle, F2T2Scanning, EAScanning, Detached};

	private RadarSystemStatesEnum(String name, int ordinal)
	{
		super(name, ordinal);
	}

	public static RadarSystemStatesEnum valueOf(String name)
	{
		return SysMLEnumeration.valueOf(name, values);
	}

	public static RadarSystemStatesEnum valueOf(int ordinal)
	{
		return SysMLEnumeration.valueOf(ordinal, values);
	}

	public static RadarSystemStatesEnum[] values()
	{
		return values;
	}	
}