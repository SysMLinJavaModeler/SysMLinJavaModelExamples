package c4s2.components.services.radar;

import java.util.ArrayList;
import java.util.List;

import c4s2.common.items.information.RadarControl;
import c4s2.common.items.information.RadarSystemMonitor;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.parts.SysMLPart;

/**
 * Database of monitors received from and controls sent to radar system
 */
@SuppressWarnings("javadoc")
public class RadarMonitorAndControlDatabase extends SysMLPart
{
	@Attribute
	public List<RadarSystemMonitor> monitors;
	@Attribute
	public List<RadarControl> controls;

	public RadarMonitorAndControlDatabase()
	{
		super();
	}

	@Action
	public void add(RadarSystemMonitor monitor)
	{
		monitors.add(monitor);
	}

	@Override
	protected void createAttributes()
	{
		monitors = new ArrayList<>();
		controls = new ArrayList<>();
	}
}
