package c4s2.common.messages;

import c4s2.common.objects.information.TargetServiceControl;
import sysmlinjava.annotations.Attribute;
import sysmlinjavalibrary.common.messages.Message;

public class TargetServiceControlMessage extends Message
{
	@Attribute
	public TargetServiceControl control;

	public TargetServiceControlMessage(TargetServiceControl control)
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
		return String.format("TargetServiceControlMessage [control=%s]", control);
	}
}
