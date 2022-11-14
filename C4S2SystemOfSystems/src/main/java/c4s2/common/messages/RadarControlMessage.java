package c4s2.common.messages;

import c4s2.common.objects.information.RadarControl;
import sysmlinjava.annotations.Attribute;
import sysmlinjavalibrary.common.messages.Message;

public class RadarControlMessage extends Message
{
	@Attribute
	public RadarControl control;

	public RadarControlMessage(RadarControl control)
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
		return String.format("RadarControlMessage [control=%s]", control);
	}
}
