package c4s2.common.messages;

import c4s2.common.objects.information.OperatorServiceControl;
import sysmlinjava.annotations.Attribute;
import sysmlinjavalibrary.common.messages.Message;

public class OperatorServiceControlMessage extends Message
{
	@Attribute
	public OperatorServiceControl control;

	public OperatorServiceControlMessage(OperatorServiceControl control)
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
		return String.format("OperatorServiceControlMessage [control=%s]", control);
	}
}
