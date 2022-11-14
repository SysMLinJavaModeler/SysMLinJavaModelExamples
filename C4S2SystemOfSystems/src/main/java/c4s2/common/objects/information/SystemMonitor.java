package c4s2.common.objects.information;

import c4s2.common.valueTypes.C4S2OperatorServicesComputerStatesEnum;
import c4s2.common.valueTypes.C4S2ServicesComputerStatesEnum;
import c4s2.common.valueTypes.ServiceStatesEnum;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.valuetypes.InstantMilliseconds;
import sysmlinjavalibrary.components.communications.common.objects.EthernetSwitchIPRouterStatesEnum;
import sysmlinjavalibrary.components.communications.common.objects.SIPRNetRouterStatesEnum;

public class SystemMonitor extends SysMLClass implements StackedProtocolObject
{
	@Attribute
	public C4S2ServicesComputerStatesEnum c4s2ServicesComputerState;
	@Attribute
	public C4S2OperatorServicesComputerStatesEnum c4s2OperatorServicesComputerState;
	@Attribute
	public EthernetSwitchIPRouterStatesEnum ethernetSwitchIPRouterState;
	@Attribute
	public SIPRNetRouterStatesEnum siprnetRouterState;
	@Attribute
	public ServiceStatesEnum radarServicesState;
	@Attribute
	public ServiceStatesEnum strikeServicesState;
	@Attribute
	public ServiceStatesEnum targetServicesState;
	@Attribute
	public ServiceStatesEnum systemServicesState;
	@Attribute
	public ServiceStatesEnum operatorServicesState;
	@Attribute
	public InstantMilliseconds time;

	public SystemMonitor(C4S2ServicesComputerStatesEnum c4isrServicesComputerState, C4S2OperatorServicesComputerStatesEnum c4isrOperatorServicesComputerState, EthernetSwitchIPRouterStatesEnum ethernetSwitchIPRouterState, SIPRNetRouterStatesEnum siprnetRouterState, ServiceStatesEnum radarServicesState, ServiceStatesEnum strikeServicesState, ServiceStatesEnum targetServicesState, ServiceStatesEnum systemServicesState, ServiceStatesEnum operatorServicesState, InstantMilliseconds time)
	{
		super();
		this.c4s2ServicesComputerState = c4isrServicesComputerState;
		this.c4s2OperatorServicesComputerState = c4isrOperatorServicesComputerState;
		this.ethernetSwitchIPRouterState = ethernetSwitchIPRouterState;
		this.siprnetRouterState = siprnetRouterState;
		this.radarServicesState = radarServicesState;
		this.strikeServicesState = strikeServicesState;
		this.targetServicesState = targetServicesState;
		this.systemServicesState = systemServicesState;
		this.operatorServicesState = operatorServicesState;
		this.time = time;
	}

	public SystemMonitor()
	{
		this.c4s2ServicesComputerState = C4S2ServicesComputerStatesEnum.Initial;
		this.c4s2OperatorServicesComputerState = C4S2OperatorServicesComputerStatesEnum.Initial;
		this.ethernetSwitchIPRouterState = EthernetSwitchIPRouterStatesEnum.Initial;
		this.siprnetRouterState = SIPRNetRouterStatesEnum.Initial;
		this.radarServicesState = ServiceStatesEnum.initial;
		this.strikeServicesState = ServiceStatesEnum.initial;
		this.targetServicesState = ServiceStatesEnum.initial;
		this.systemServicesState = ServiceStatesEnum.initial;
		this.operatorServicesState = ServiceStatesEnum.initial;
		this.time = InstantMilliseconds.now();
	}

	public SystemMonitor(SystemMonitor monitor)
	{
		radarServicesState = monitor.radarServicesState;
		strikeServicesState = monitor.strikeServicesState;
		targetServicesState = monitor.targetServicesState;
		systemServicesState = monitor.systemServicesState;
		operatorServicesState = monitor.operatorServicesState;
		c4s2ServicesComputerState = monitor.c4s2ServicesComputerState;
		c4s2OperatorServicesComputerState = monitor.c4s2OperatorServicesComputerState;
		ethernetSwitchIPRouterState = monitor.ethernetSwitchIPRouterState;
		siprnetRouterState = monitor.siprnetRouterState;
		time = monitor.time;
	}

	public boolean isIAW(C4S2SystemControl control)
	{
		boolean operatorServiceCompIAW = c4s2OperatorServicesComputerState.equals(control.c4s2OperatorServicesComputerState);
		boolean svcCompIAW = c4s2ServicesComputerState.equals(control.c4s2ServicesComputerState);
		boolean swtchRtrIAW = ethernetSwitchIPRouterState.equals(control.ethernetSwitchIPRouterState);
		boolean radarServiceIAW = radarServicesState.equals(control.radarServicesState);
		boolean strikeServiceIAW = strikeServicesState.equals(control.strikeServicesState);
		boolean targetServiceIAW = targetServicesState.equals(control.targetServicesState);
		boolean systemServiceIAW = systemServicesState.equals(control.systemServicesState);
		boolean operatorServiceIAW = operatorServicesState.equals(control.operatorServicesState);
		return operatorServiceCompIAW && svcCompIAW && swtchRtrIAW && radarServiceIAW && strikeServiceIAW && targetServiceIAW && systemServiceIAW && operatorServiceIAW;
	}

	@Override
	public String stackNamesString()
	{
		return String.format("%s(states=%s,%s,%s,%s,%s,%s,%s,%s)", getClass().getSimpleName(),
			operatorServicesState,
			c4s2OperatorServicesComputerState,
			c4s2ServicesComputerState,
			ethernetSwitchIPRouterState,
			radarServicesState,
			strikeServicesState,
			targetServicesState,
			systemServicesState
			);
	}

	@Override
	public String toString()
	{
		return String.format(
			"SystemMonitor [c4isrServicesComputerState=%s, c4isrOperatorServicesComputerState=%s, ethernetSwitchipRouterState=%s, siprnetRouterState=%s, radarServicesState=%s, strikeServicesState=%s, targetServicesState=%s, systemServicesState=%s, operatorServicesState=%s, time=%s]",
			c4s2ServicesComputerState, c4s2OperatorServicesComputerState, ethernetSwitchIPRouterState, siprnetRouterState, radarServicesState, strikeServicesState, targetServicesState, systemServicesState, operatorServicesState, time);
	}
}
