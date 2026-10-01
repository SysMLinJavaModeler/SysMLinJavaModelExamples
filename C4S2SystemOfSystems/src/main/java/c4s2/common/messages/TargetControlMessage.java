package c4s2.common.messages;

import c4s2.common.items.information.TargetControl;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjavalibrary.common.messages.Message;

@SuppressWarnings("javadoc")
public class TargetControlMessage extends Message
{
	@Attribute
	public TargetControl control;

	public TargetControlMessage(TargetControl control)
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
		return String.format("TargetControlMessage [control=%s]", control);
	}
}
