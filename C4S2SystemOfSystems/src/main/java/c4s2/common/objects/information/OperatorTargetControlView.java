package c4s2.common.objects.information;

import sysmlinjava.annotations.Attribute;

public class OperatorTargetControlView extends OperatorControlView
{
	@Attribute
	public TargetControl control;

	public OperatorTargetControlView(TargetControl control)
	{
		super();
		this.control = control;
	}

	public OperatorTargetControlView(OperatorTargetControlView copied)
	{
		super();
		this.control = new TargetControl(copied.control);
	}

	public OperatorTargetControlView()
	{
		this.control = new TargetControl();
	}

	@Override
	public String stackNamesString()
	{
		return control.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("OperatorTargetControlView [control=%s]", control);
	}
}
