package c4s2.components.services.strike;

import java.util.ArrayList;
import java.util.List;
import c4s2.common.objects.information.StrikeControl;
import c4s2.common.objects.information.StrikeMonitor;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;

public class StrikeMonitorAndControlDatabase extends SysMLBlock
{
	@Value
	public List<StrikeMonitor> monitors;
	@Value
	public List<StrikeControl> controls;

	public StrikeMonitorAndControlDatabase()
	{
		super();
	}

	@Operation
	public void add(StrikeMonitor monitor)
	{
		monitors.add(monitor);
	}
	
	@Operation
	public void add(StrikeControl control)
	{
		controls.add(control);
	}

	@Override
	protected void createValues()
	{
		monitors = new ArrayList<>();
		controls = new ArrayList<>();
	}
}
