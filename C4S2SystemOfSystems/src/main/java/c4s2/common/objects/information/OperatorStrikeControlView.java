package c4s2.common.objects.information;

import sysmlinjava.annotations.Attribute;

public class OperatorStrikeControlView extends OperatorControlView
{
	@Attribute
	public StrikeControl control;

	public OperatorStrikeControlView(StrikeControl control)
	{
		super();
		this.control = control;
	}
	
	public OperatorStrikeControlView()
	{
		this.control = new StrikeControl();
	}

	public OperatorStrikeControlView(OperatorStrikeControlView controlView)
	{
		this.control = new StrikeControl(controlView.control);
	}

	public OperatorStrikeControlView copy()
	{
		return new OperatorStrikeControlView(new StrikeControl(control));
	}

	@Override
	public String stackNamesString()
	{
		return control.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("OperatorStrikeControlView [control=%s]", control);
	}
}
