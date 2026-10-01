package dbssystem.common;

import java.io.Serializable;

import sysmlinjava.attributetypes.DistanceMillimeters;
import sysmlinjava.attributetypes.FrequencyHertz;
import sysmlinjava.attributetypes.SysMLAttributeType;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.units.SysMLinJavaUnits;

public class TremorLevel extends SysMLAttributeType implements Serializable
{
	private static final long serialVersionUID = -44622084523635911L;

	@Attribute
	public FrequencyHertz frequency;
	@Attribute
	public DistanceMillimeters amplitude;
	
	public TremorLevel(FrequencyHertz frequency, DistanceMillimeters amplitude)
	{
		super();
		this.frequency = frequency;
		this.amplitude = amplitude;
	}

	public TremorLevel(TremorLevel copied)
	{
		this.frequency = (FrequencyHertz) copied.frequency.copy();
		this.amplitude = (DistanceMillimeters) copied.amplitude.copy();
	}

	public void setValue(TremorLevel level)
	{
		frequency.value = level.frequency.value;
		amplitude.value = level.amplitude.value;
		notifyAttributeObservers();
	}

	@Override
	public SysMLAttributeType copy()
	{
		return new TremorLevel(this);
	}

	@Override
	public void createUnits()
	{
		units = SysMLinJavaUnits.Object;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("TremorLevel [frequency=");
		builder.append(frequency);
		builder.append(", amplitude=");
		builder.append(amplitude);
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
