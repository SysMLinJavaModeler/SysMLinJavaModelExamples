package c4s2.components.services.target;

import java.util.ArrayList;
import java.util.List;

import c4s2.common.items.information.TargetMonitor;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.parts.SysMLPart;

public class TargetMonitorDatabase extends SysMLPart
{
	@Attribute
	public List<TargetMonitor> monitors;

	public TargetMonitorDatabase()
	{
		super();
	}

	@Action
	public void add(TargetMonitor monitor)
	{
		monitors.add(monitor);
	}

	@Override
	public void createAttributes()
	{
		monitors = new ArrayList<>();
	}
}
