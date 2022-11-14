package c4s2.components.services.radar;

import java.util.ArrayList;
import java.util.List;
import c4s2.common.objects.information.RadarControl;
import c4s2.common.objects.information.RadarSystemMonitor;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;

public class RadarMonitorAndControlDatabase extends SysMLBlock
{
	@Value
	public List<RadarSystemMonitor> monitors;
	@Value
	public List<RadarControl> controls;

	public RadarMonitorAndControlDatabase()
	{
		super();
	}

	@Operation
	public void add(RadarSystemMonitor monitor)
	{
		monitors.add(monitor);
	}

	@Override
	protected void createValues()
	{
		monitors = new ArrayList<>();
		controls = new ArrayList<>();
	}
}
