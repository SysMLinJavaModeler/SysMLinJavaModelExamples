package c4s2.common.messages;

import c4s2.common.objects.information.StrikeSystemControl;
import sysmlinjava.annotations.Attribute;
import sysmlinjavalibrary.common.messages.Message;

public class StrikeSystemControlMessage extends Message
{
	@Attribute
	public StrikeSystemControl control;

	public StrikeSystemControlMessage(StrikeSystemControl control)
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
		return String.format("StrikeSystemControlMessage [control=%s]", control);
	}
}
