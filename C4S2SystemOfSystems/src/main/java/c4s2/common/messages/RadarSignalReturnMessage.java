package c4s2.common.messages;

import c4s2.common.items.information.RadarSignalReturn;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjavalibrary.common.messages.Message;

@SuppressWarnings("javadoc")
public class RadarSignalReturnMessage extends Message
{
	@Attribute
	public RadarSignalReturn radarReturn;

	public RadarSignalReturnMessage(RadarSignalReturn radarReturn)
	{
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
		return String.format("RadarSignalReturnMessage [radarReturn=%s]", radarReturn);
	}
}
