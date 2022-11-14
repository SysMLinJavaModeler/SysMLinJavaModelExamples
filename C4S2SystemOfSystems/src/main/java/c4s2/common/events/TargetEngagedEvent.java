package c4s2.common.events;

import sysmlinjava.events.SysMLChangeEvent;

public class TargetEngagedEvent extends SysMLChangeEvent
{
	public TargetEngagedEvent()
	{
		super("TargetEngaged");
	}

	@Override
	protected void createChangeExpression()
	{
		changeExpression = "Target engaged";
	}
}
