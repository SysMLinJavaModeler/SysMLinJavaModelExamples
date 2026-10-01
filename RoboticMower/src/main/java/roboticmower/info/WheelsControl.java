package roboticmower.info;

import sysmlinjava.attributetypes.DirectionDegrees;
import sysmlinjava.attributetypes.RevolutionsPerMinute;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Speed and direction controls for wheels that drive the robotic mower
 * 
 * @author ModelerOne
 *
 */
public class WheelsControl extends SysMLAnything
{
	/**
	 * Attribute for the wheel speed in RPM
	 */
	@Attribute
	public RevolutionsPerMinute rpm;
	/**
	 * Attribute for the direction in which the wheels are to steer
	 */
	@Attribute
	public DirectionDegrees direction;

	/**
	 * Constructor
	 * 
	 * @param rpm       RPM speed of the wheels
	 * @param direction steering direction of the wheels
	 */
	public WheelsControl(RevolutionsPerMinute rpm, DirectionDegrees direction)
	{
		super();
		this.rpm = rpm;
		this.direction = direction;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("WheelsControl [rpm=");
		builder.append(rpm);
		builder.append(", direction=");
		builder.append(direction);
		builder.append(", name=");
		builder.append(name);
		builder.append(", id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}

}
