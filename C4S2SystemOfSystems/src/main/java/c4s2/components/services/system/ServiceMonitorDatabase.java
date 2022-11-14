package c4s2.components.services.system;

import java.util.ArrayList;
import java.util.List;
import c4s2.common.objects.information.OperatorServiceMonitor;
import c4s2.common.objects.information.RadarServiceMonitor;
import c4s2.common.objects.information.ServiceMonitor;
import c4s2.common.objects.information.StrikeServiceMonitor;
import c4s2.common.objects.information.SystemServiceMonitor;
import c4s2.common.objects.information.TargetServiceMonitor;
import sysmlinjava.common.SysMLClass;

public class ServiceMonitorDatabase extends SysMLClass
{
	public List<RadarServiceMonitor> radarServiceMonitors;
	public List<StrikeServiceMonitor> strikeServiceMonitors;
	public List<TargetServiceMonitor> targetServiceMonitors;
	public List<SystemServiceMonitor> systemServiceMonitors;
	public List<OperatorServiceMonitor> operatorServiceMonitors;

	public ServiceMonitorDatabase()
	{
		super();
		radarServiceMonitors = new ArrayList<>();
		strikeServiceMonitors = new ArrayList<>();
		targetServiceMonitors = new ArrayList<>();
		systemServiceMonitors = new ArrayList<>();
		operatorServiceMonitors = new ArrayList<>();
	}

	public void add(ServiceMonitor serviceMonitor)
	{
		if(serviceMonitor instanceof RadarServiceMonitor monitor)
			radarServiceMonitors.add(monitor);
		else if(serviceMonitor instanceof StrikeServiceMonitor monitor)
			strikeServiceMonitors.add(monitor);
		else if(serviceMonitor instanceof TargetServiceMonitor monitor)
			targetServiceMonitors.add(monitor);
		else if(serviceMonitor instanceof SystemServiceMonitor monitor)
			systemServiceMonitors.add(monitor);
		else if(serviceMonitor instanceof OperatorServiceMonitor monitor)
			operatorServiceMonitors.add(monitor);
	}
}
