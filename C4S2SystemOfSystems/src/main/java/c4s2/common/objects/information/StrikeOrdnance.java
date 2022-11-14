package c4s2.common.objects.information;

import c4s2.common.valueTypes.OrdnanceStatesEnum;
import c4s2.common.valueTypes.OrdnanceTypeEnum;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.valuetypes.InstantMilliseconds;
import sysmlinjava.valuetypes.PointGeospatial;

public class StrikeOrdnance extends SysMLClass implements StackedProtocolObject
{
	@Attribute
	public OrdnanceTypeEnum type;
	@Attribute
	public OrdnanceStatesEnum state;
	@Attribute
	public PointGeospatial location;
	@Attribute
	public InstantMilliseconds time;

	public StrikeOrdnance(OrdnanceTypeEnum type, OrdnanceStatesEnum state, PointGeospatial location, InstantMilliseconds time)
	{
		super();
		this.type = type;
		this.state = state;
		this.location = location;
		this.time = time;
	}

	public StrikeOrdnance(StrikeOrdnance copyOf)
	{
		super();
		this.type = copyOf.type;
		this.state = copyOf.state;
		this.location = new PointGeospatial(copyOf.location);
		this.time = new InstantMilliseconds(copyOf.time);
	}

	public StrikeOrdnance()
	{
		super();
		this.type = OrdnanceTypeEnum.small;
		this.state = OrdnanceStatesEnum.unarmed;
		this.location = new PointGeospatial();
		this.time = new InstantMilliseconds();
	}

	@Override
	public String stackNamesString()
	{
		return String.format("%s(type=%s, state=%s)", getClass().getSimpleName(), type.name, state.name);
	}

	@Override
	public String toString()
	{
		return String.format("StrikeOrdnance [type=%s, state=%s, location=%s, time=%s]", type, state, location, time);
	}
}
