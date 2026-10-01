package dbssystem.common;

import java.io.Serializable;
import sysmlinjava.attributetypes.IInteger;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.attributes.Attribute;

public class PressureSignal extends SysMLAnything implements Serializable
{
	private static final long serialVersionUID = -8304480747719490359L;

	@Attribute
	public IInteger rate;
	
	public PressureSignal(IInteger rate)
	{
		super();
		this.rate = rate;
	}

	public PulseValue toPulse()
	{
		return new PulseValue(rate);
	}

	@Override
	public String toString()
	{
		return String.format("PressureSignal [rate=%s]", rate);
	}
}
