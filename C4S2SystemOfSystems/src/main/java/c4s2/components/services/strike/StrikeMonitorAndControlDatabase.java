package c4s2.components.services.strike;

import java.util.ArrayList;
import java.util.List;

import c4s2.common.items.information.StrikeControl;
import c4s2.common.items.information.StrikeMonitor;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.parts.SysMLPart;

/**
 * Database of monitors receive from and controls sent to strike system
 */
@SuppressWarnings("javadoc")
public class StrikeMonitorAndControlDatabase extends SysMLPart
{
	@Attribute
	public List<StrikeMonitor> monitors;
	@Attribute
	public List<StrikeControl> controls;

	public StrikeMonitorAndControlDatabase()
	{
		super();
	}

	@Action
	public void add(StrikeMonitor monitor)
	{
		monitors.add(monitor);
	}

	@Action
	public void add(StrikeControl control)
	{
		controls.add(control);
	}

	@Override
	protected void createAttributes()
	{
		monitors = new ArrayList<>();
		controls = new ArrayList<>();
	}
}
