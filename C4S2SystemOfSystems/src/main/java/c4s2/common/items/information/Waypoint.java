package c4s2.common.items.information;

import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.attributetypes.PointGeospatial;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.attributes.Attribute;

@SuppressWarnings("javadoc")
public class Waypoint extends SysMLAnything
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