package c4s2.common.items.information;

import java.util.Optional;

import c4s2.common.attributetypes.RadarSystemStatesEnum;
import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.common.StackedProtocolObject;

@SuppressWarnings("javadoc")
public class RadarSystemControl extends SysMLAnything implements StackedProtocolObject
{	
	@Attribute
	public RadarSystemStatesEnum state;
	@Attribute
	public Optional<RadarSignalTransmission> transmission;
	@Attribute
	public InstantMilliseconds time;

	public RadarSystemControl(RadarSystemStatesEnum state, Optional<RadarSignalTransmission> transmission, InstantMilliseconds time)
	{
		super();
		this.state = state;
		this.transmission = transmission;
		this.time = time;
	}

	public RadarSystemControl(RadarSystemControl copied)
	{
		super(copied);
		this.state = copied.state;
		if(copied.transmission.isPresent())
			this.transmission = Optional.of(new RadarSignalTransmission(copied.transmission.get()));
		else
			this.transmission = Optional.empty();
		this.time = new InstantMilliseconds(copied.time);
	}

	@Override
	public String stackNamesString()
	{
		return String.format("%s(state=%s)", getClass().getSimpleName(), state);
	}

	@Override
	public String toString()
	{
		return String.format("RadarControl [state=%s, transmission=%s, time=%s]", state, transmission, time);
	}
}
