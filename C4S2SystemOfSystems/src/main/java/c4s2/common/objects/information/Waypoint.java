package c4s2.common.objects.information;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.valuetypes.InstantMilliseconds;
import sysmlinjava.valuetypes.PointGeospatial;

public class Waypoint extends SysMLClass
{
	@Attribute
	public InstantMilliseconds time;
	@Attribute
	public PointGeospatial location;

	public Waypoint(InstantMilliseconds time, PointGeospatial location)
	{
		super();
		this.time = time;
		this.location = location;
	}

	public Waypoint(Waypoint copied)
	{
		super(copied);
		this.time = new InstantMilliseconds(copied.time);
		this.location = new PointGeospatial(copied.location);
	}

	@Override
	public String toString()
	{
		return String.format("Waypoint [time=%s, location=%s]", time, location);
	}
}