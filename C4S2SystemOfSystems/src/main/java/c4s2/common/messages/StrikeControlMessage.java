package c4s2.common.messages;

import c4s2.common.objects.information.StrikeControl;
import sysmlinjava.annotations.Attribute;
import sysmlinjavalibrary.common.messages.Message;

public class StrikeControlMessage extends Message
{
	@Attribute
	public StrikeControl control;

	public StrikeControlMessage(StrikeControl control)
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
		return String.format("StrikeControlMessage [control=%s]", control);
	}
}
