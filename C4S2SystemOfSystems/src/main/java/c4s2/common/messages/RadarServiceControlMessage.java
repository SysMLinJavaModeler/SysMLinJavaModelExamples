package c4s2.common.messages;

import c4s2.common.items.information.RadarServiceControl;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjavalibrary.common.messages.Message;

@SuppressWarnings("javadoc")
public class RadarServiceControlMessage extends Message
{
	@Attribute
	public RadarServiceControl control;

	public RadarServiceControlMessage(RadarServiceControl control)
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
		return String.format("RadarServiceControlMessage [control=%s]", control);
	}
}
