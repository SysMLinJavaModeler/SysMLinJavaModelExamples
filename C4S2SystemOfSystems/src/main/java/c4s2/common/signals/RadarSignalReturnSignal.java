package c4s2.common.signals;

import c4s2.common.objects.information.RadarSignalReturn;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

public class RadarSignalReturnSignal extends SysMLSignal
{
	@Attribute
	public RadarSignalReturn radarReturn;

	public RadarSignalReturnSignal(RadarSignalReturn radarReturn)
	{
		super();
		this.radarReturn = radarReturn;
	}

	@Override
	public String stackNamesString()
	{
		return radarReturn.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("RadarSignalReturnSignal [radarReturn=%s]", radarReturn);
	}

}
