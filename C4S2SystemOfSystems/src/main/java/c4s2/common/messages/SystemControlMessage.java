package c4s2.common.messages;

import c4s2.common.items.information.C4S2SystemControl;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjavalibrary.common.messages.Message;

@SuppressWarnings("javadoc")
public class SystemControlMessage extends Message
{
	@Attribute
	public C4S2SystemControl control;

	public SystemControlMessage(C4S2SystemControl control)
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
		return String.format("SystemControlMessage [control=%s]", control);
	}
}
