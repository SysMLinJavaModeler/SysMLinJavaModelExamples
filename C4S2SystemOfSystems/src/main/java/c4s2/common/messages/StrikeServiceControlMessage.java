package c4s2.common.messages;

import c4s2.common.items.information.StrikeServiceControl;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjavalibrary.common.messages.Message;

@SuppressWarnings("javadoc")
public class StrikeServiceControlMessage extends Message
{
	@Attribute
	public StrikeServiceControl control;

	public StrikeServiceControlMessage(StrikeServiceControl control)
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
		return String.format("StrikeServiceControlMessage [control=%s]", control);
	}
}
