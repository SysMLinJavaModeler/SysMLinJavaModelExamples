package dbssystem.common;

import java.io.Serializable;

import sysmlinjava.attributetypes.IInteger;

public class PulseValue extends IInteger implements Serializable
{
	private static final long serialVersionUID = -5645764597295659619L;

	public PulseValue(IInteger value)
	{
		super(value.value);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("PulseValue [value=");
		builder.append(value);
		builder.append(", units=");
		builder.append(units);
		builder.append(", name=");
		builder.append(name);
		builder.append(", id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}
}
