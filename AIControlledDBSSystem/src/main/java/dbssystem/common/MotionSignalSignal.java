package dbssystem.common;

import java.io.Serializable;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

public class MotionSignalSignal extends SysMLSignal implements Serializable
{
	private static final long serialVersionUID = -950252377931186103L;

	@Attribute
	public MotionSignal value;

	public MotionSignalSignal(MotionSignal value)
	{
		super();
		this.value = value;
	}

	@Override
	public String stackNamesString()
	{
		return "Tremor Motion";
	}
}
