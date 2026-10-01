package dbssystem.common;

import java.io.Serializable;

import sysmlinjava.attributetypes.JerkMetersPerSecondCubed;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.attributes.Attribute;

public class MotionSignal extends SysMLAnything  implements Serializable
{
	private static final long serialVersionUID = 1851159395194130147L;

	@Attribute
	public JerkMetersPerSecondCubed jerk;

	public MotionSignal(JerkMetersPerSecondCubed value)
	{
		super();
		this.jerk = value;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("MotionSignal [jerk=");
		builder.append(jerk);
		builder.append(", name=");
		builder.append(name);
		builder.append(", id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}

}
