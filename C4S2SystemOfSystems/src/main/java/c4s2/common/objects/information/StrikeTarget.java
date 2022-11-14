package c4s2.common.objects.information;

import java.util.Optional;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.valuetypes.PointGeospatial;
import sysmlinjava.valuetypes.VelocityMetersPerSecondRadians;

public class StrikeTarget extends SysMLClass implements StackedProtocolObject
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
