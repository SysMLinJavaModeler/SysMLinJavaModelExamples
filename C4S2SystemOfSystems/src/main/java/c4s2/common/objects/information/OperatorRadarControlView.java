package c4s2.common.objects.information;

import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;

public class OperatorRadarControlView extends OperatorControlView implements StackedProtocolObject
{
	@Attribute
	public RadarControl control;

	public OperatorRadarControlView(RadarControl control)
	{
		super();
		this.control = control;
	}

	public OperatorRadarControlView(OperatorRadarControlView copied)
	{
		super(copied);
		this.control = new RadarControl(copied.control);
	}

	public OperatorRadarControlView()
	{
		this.control = new RadarControl();
	}

	@Override
	public String stackNamesString()
	{
		return control.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("OperatorRadarControlView [control=%s]", control);
	}
}
