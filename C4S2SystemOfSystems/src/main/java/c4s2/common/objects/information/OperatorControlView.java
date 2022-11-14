package c4s2.common.objects.information;

import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.common.SysMLClass;

public class OperatorControlView extends SysMLClass implements StackedProtocolObject
{
	public OperatorControlView()
	{
		super();
	}

	public OperatorControlView(OperatorRadarControlView copied)
	{
		super(copied);
	}
}
