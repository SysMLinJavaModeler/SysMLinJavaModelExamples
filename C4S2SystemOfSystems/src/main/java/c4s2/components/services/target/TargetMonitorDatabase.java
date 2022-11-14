package c4s2.components.services.target;

import java.util.ArrayList;
import java.util.List;
import c4s2.common.objects.information.TargetMonitor;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;

public class TargetMonitorDatabase extends SysMLBlock
{
	@Value
	public List<TargetMonitor> monitors;

	public TargetMonitorDatabase()
	{
		super();
	}

	@Operation
	public void add(TargetMonitor monitor)
	{
		monitors.add(monitor);
	}

	@Override
	public void createValues()
	{
		monitors = new ArrayList<>();
	}
}
