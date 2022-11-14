package c4s2.common.messages;

import c4s2.common.objects.information.SystemServiceControl;
import sysmlinjava.annotations.Attribute;
import sysmlinjavalibrary.common.messages.Message;

public class SystemServiceControlMessage extends Message
{
	@Attribute
	public SystemServiceControl control;

	public SystemServiceControlMessage(SystemServiceControl control)
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
		return String.format("SystemServiceControlMessage [control=%s]", control);
	}
}
