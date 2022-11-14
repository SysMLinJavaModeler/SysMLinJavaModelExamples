package c4s2.common.objects.information;

import sysmlinjava.annotations.Attribute;

public class OperatorSystemControlView extends OperatorControlView
{
	@Attribute
	public C4S2SystemControl control;

	public OperatorSystemControlView(C4S2SystemControl control)
	{
		super();
		this.control = control;
	}

	public OperatorSystemControlView()
	{
		this.control = new C4S2SystemControl();
	}

	public OperatorSystemControlView(OperatorSystemControlView controlView)
	{
		this.control = new C4S2SystemControl(controlView.control);
	}

	public OperatorSystemControlView copy()
	{
		return new OperatorSystemControlView(control.copy());
	}

	@Override
	public String stackNamesString()
	{
		return control.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("OperatorSystemControlView [control=%s]", control);
	}
}
