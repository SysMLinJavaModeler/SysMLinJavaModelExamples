package c4s2.common.signals;

import c4s2.common.objects.information.RadarSignalTransmission;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

public class RadarSignalTransmissionSignal extends SysMLSignal
{
	@Attribute
	public RadarSignalTransmission transmission;

	public RadarSignalTransmissionSignal(RadarSignalTransmission transmission)
	{
		super();
		this.transmission = transmission;
	}

	@Override
	public String stackNamesString()
	{
		return transmission.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("RadarSignalTransmissionSignal [name=%s, id=%s, transmission=%s]", name, id, transmission);
	}
}
