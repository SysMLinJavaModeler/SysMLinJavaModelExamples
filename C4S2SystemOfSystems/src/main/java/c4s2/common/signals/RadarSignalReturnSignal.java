package c4s2.common.signals;

import c4s2.common.items.information.RadarSignalReturn;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

@SuppressWarnings("javadoc")
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
