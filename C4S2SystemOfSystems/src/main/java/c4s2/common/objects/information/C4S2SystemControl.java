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

public class C4S2SystemControl extends SysMLClass implements StackedProtocolObject
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

	public C4S2SystemControl(C4S2ServicesComputerStatesEnum c4s2ServicesComputerState, C4S2OperatorServicesComputerStatesEnum c4s2OperatorServicesComputerState, EthernetSwitchIPRouterStatesEnum ethernetSwitchIPRouterState, SIPRNetRouterStatesEnum siprnetRouterState, ServiceStatesEnum radarServicesState, ServiceStatesEnum strikeServicesState, ServiceStatesEnum targetServicesState, ServiceStatesEnum systemServicesState, ServiceStatesEnum operatorServicesState, InstantMilliseconds time)
	{
		super();
		this.c4s2ServicesComputerState = c4s2ServicesComputerState;
		this.c4s2OperatorServicesComputerState = c4s2OperatorServicesComputerState;
		this.ethernetSwitchIPRouterState = ethernetSwitchIPRouterState;
		this.siprnetRouterState = siprnetRouterState;
		this.radarServicesState = radarServicesState;
		this.strikeServicesState = strikeServicesState;
		this.targetServicesState = targetServicesState;
		this.systemServicesState = systemServicesState;
		this.operatorServicesState = operatorServicesState;
		this.time = time;
	}

	public C4S2SystemControl()
	{
		radarServicesState = ServiceStatesEnum.initial;
		strikeServicesState = ServiceStatesEnum.initial;
		targetServicesState = ServiceStatesEnum.initial;
		systemServicesState = ServiceStatesEnum.initial;
		operatorServicesState = ServiceStatesEnum.initial;
		c4s2ServicesComputerState = C4S2ServicesComputerStatesEnum.Initial;
		c4s2OperatorServicesComputerState = C4S2OperatorServicesComputerStatesEnum.Initial;
		ethernetSwitchIPRouterState = EthernetSwitchIPRouterStatesEnum.Initial;
		siprnetRouterState = SIPRNetRouterStatesEnum.Initial;
		time = InstantMilliseconds.now();
	}

	public C4S2SystemControl(C4S2SystemControl control)
	{
		radarServicesState = control.radarServicesState;
		strikeServicesState = control.strikeServicesState;
		targetServicesState = control.targetServicesState;
		systemServicesState = control.systemServicesState;
		operatorServicesState = control.operatorServicesState;
		c4s2ServicesComputerState = control.c4s2ServicesComputerState;
		c4s2OperatorServicesComputerState = control.c4s2OperatorServicesComputerState;
		ethernetSwitchIPRouterState = control.ethernetSwitchIPRouterState;
		siprnetRouterState = control.siprnetRouterState;
		time = control.time;
	}

	public void toConfigured()
	{
		radarServicesState = ServiceStatesEnum.operational;
		strikeServicesState = ServiceStatesEnum.operational;
		targetServicesState = ServiceStatesEnum.operational;
		systemServicesState = ServiceStatesEnum.operational;
		operatorServicesState = ServiceStatesEnum.operational;
		c4s2ServicesComputerState = C4S2ServicesComputerStatesEnum.Operational;
		c4s2OperatorServicesComputerState = C4S2OperatorServicesComputerStatesEnum.Operational;
		ethernetSwitchIPRouterState = EthernetSwitchIPRouterStatesEnum.Operational;
		siprnetRouterState = SIPRNetRouterStatesEnum.Operational;
		time = InstantMilliseconds.now();
	}

	public void toFinalized()
	{
		radarServicesState = ServiceStatesEnum.finall;
		strikeServicesState = ServiceStatesEnum.finall;
		targetServicesState = ServiceStatesEnum.finall;
		systemServicesState = ServiceStatesEnum.finall;
		operatorServicesState = ServiceStatesEnum.finall;
		c4s2ServicesComputerState = C4S2ServicesComputerStatesEnum.PowerOff;
		c4s2OperatorServicesComputerState = C4S2OperatorServicesComputerStatesEnum.PowerOff;
		ethernetSwitchIPRouterState = EthernetSwitchIPRouterStatesEnum.PowerOff;
		siprnetRouterState = SIPRNetRouterStatesEnum.PowerOff;
		time = InstantMilliseconds.now();
	}

	public boolean isFinalized()
	{
		return
		radarServicesState == ServiceStatesEnum.finall &&
		strikeServicesState == ServiceStatesEnum.finall &&
		targetServicesState == ServiceStatesEnum.finall &&
		systemServicesState == ServiceStatesEnum.finall &&
		operatorServicesState == ServiceStatesEnum.finall &&
		c4s2ServicesComputerState == C4S2ServicesComputerStatesEnum.PowerOff &&
		c4s2OperatorServicesComputerState == C4S2OperatorServicesComputerStatesEnum.PowerOff &&
		ethernetSwitchIPRouterState == EthernetSwitchIPRouterStatesEnum.PowerOff &&
		siprnetRouterState == SIPRNetRouterStatesEnum.PowerOff;
	}
	public C4S2SystemControl copy()
	{
		return new C4S2SystemControl(c4s2ServicesComputerState, c4s2OperatorServicesComputerState, ethernetSwitchIPRouterState, siprnetRouterState, radarServicesState, strikeServicesState, targetServicesState, systemServicesState, operatorServicesState, time);
	}

	@Override
	public String stackNamesString()
	{
		return String.format("%s(state=%s)", getClass().getSimpleName(), c4s2ServicesComputerState);
	}

	@Override
	public String toString()
	{
		return String.format(
			"C4S2SystemControl [c4s2ServicesComputerState=%s, c4s2OperatorServicesComputerState=%s, ethernetSwitchIPRouterState=%s, siprnetRouterState=%s, radarServicesState=%s, strikeServicesState=%s, targetServicesState=%s, systemServicesState=%s, operatorServicesState=%s, time=%s]",
			c4s2ServicesComputerState, c4s2OperatorServicesComputerState, ethernetSwitchIPRouterState, siprnetRouterState, radarServicesState, strikeServicesState, targetServicesState, systemServicesState, operatorServicesState, time);
	}
}
