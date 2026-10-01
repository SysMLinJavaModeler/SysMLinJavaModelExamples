package c4s2.common.items.information;

import java.util.Optional;

import sysmlinjava.attributetypes.PointGeospatial;
import sysmlinjava.attributetypes.VelocityMetersPerSecondRadians;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.common.StackedProtocolObject;

@SuppressWarnings("javadoc")
public class StrikeTarget extends SysMLAnything implements StackedProtocolObject
{
	@Attribute
	public PointGeospatial position;
	@Attribute
	public VelocityMetersPerSecondRadians velocity;

	public StrikeTarget(PointGeospatial position, VelocityMetersPerSecondRadians velocity)
	{
		super();
		this.position = position;
		this.velocity = velocity;
	}
	
	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.empty());
	}

	@Override
	public String toString()
	{
		return String.format("StrikeTarget [position=%s, velocity=%s]", position, velocity);
	}
}
