package c4s2.common.messages;

import c4s2.common.objects.information.RadarSystemControl;
import sysmlinjava.annotations.Attribute;
import sysmlinjavalibrary.common.messages.Message;

public class RadarSystemControlMessage extends Message
{
	@Attribute
	public RadarSystemControl control;

	public RadarSystemControlMessage(RadarSystemControl control)
	{
		super();
		this.control = control;
	}

	@Override
	public String stackNamesString()
	{
		return control.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("RadarSystemControlMessage [control=%s]", control);
	}
}
