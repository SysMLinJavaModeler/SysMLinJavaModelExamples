package c4s2.components.services.system;

import static c4s2.common.valueTypes.ServiceStatesEnum.*;
import java.util.List;
import java.util.Optional;
import c4s2.common.messages.OperatorServiceControlMessage;
import c4s2.common.messages.RadarServiceControlMessage;
import c4s2.common.messages.StrikeServiceControlMessage;
import c4s2.common.messages.SystemMonitorMessage;
import c4s2.common.messages.SystemServiceControlMessage;
import c4s2.common.messages.TargetServiceControlMessage;
import c4s2.common.objects.information.OperatorServiceControl;
import c4s2.common.objects.information.OperatorServiceMonitor;
import c4s2.common.objects.information.RadarServiceControl;
import c4s2.common.objects.information.RadarServiceMonitor;
import c4s2.common.objects.information.StrikeServiceControl;
import c4s2.common.objects.information.StrikeServiceMonitor;
import c4s2.common.objects.information.C4S2SystemControl;
import c4s2.common.objects.information.SystemMonitor;
import c4s2.common.objects.information.SystemServiceControl;
import c4s2.common.objects.information.SystemServiceMonitor;
import c4s2.common.objects.information.TargetServiceControl;
import c4s2.common.objects.information.TargetServiceMonitor;
import c4s2.common.ports.information.C4S2MessagingProtocol;
import c4s2.common.valueTypes.C4S2OperatorServicesComputerStatesEnum;
import c4s2.common.valueTypes.C4S2ServicesComputerStatesEnum;
import c4s2.common.valueTypes.ServiceStatesEnum;
import c4s2.components.computer.operator.C4S2OperatorServicesComputer;
import c4s2.components.computer.services.C4S2ServicesComputer;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.statemachine.FinalEvent;
import sysmlinjava.valuetypes.InstantMilliseconds;
import sysmlinjavalibrary.common.objects.information.MIB;
import sysmlinjavalibrary.common.objects.information.SNMPRequest;
import sysmlinjavalibrary.common.objects.information.SNMPResponse;
import sysmlinjavalibrary.common.ports.information.SNMPManagerProtocol;
import sysmlinjavalibrary.components.communications.common.objects.EthernetSwitchIPRouterStatesEnum;
import sysmlinjavalibrary.components.communications.common.objects.SIPRNetRouterStatesEnum;
import sysmlinjavalibrary.components.communications.internet.EthernetSwitchIPRouter;
import sysmlinjavalibrary.components.communications.siprnet.SIPRNetRouter;

public class SystemServices extends SysMLBlock
{
	@FullPort
	public C4S2MessagingProtocol messaging;
	@FullPort
	public SNMPManagerProtocol snmpC4S2ServicesComputer;
	@FullPort
	public SNMPManagerProtocol snmpOperatorServicesComputer;
	@FullPort
	public SNMPManagerProtocol snmpSwitchRouter;
	@FullPort
	public SNMPManagerProtocol snmpSIPRNetRouter;

	@Part
	public SNMPMIBDatabase mibDatabase;
	@Part
	public ServiceMonitorDatabase serviceMonitorDatabase;

	@Value
	public C4S2SystemControl currentSystemControl;
	@Value
	public SystemMonitor currentSystemMonitor;
	@Value
	private SystemServiceControl currentServiceControl;
	@Value
	private SystemServiceMonitor currentServiceMonitor;

	public SystemServices()
	{
		super("SystemServices", 0L);
	}

	@Reception
	public void onSNMResponse(SNMPResponse response)
	{
		logger.info(response.toString());
		MIB mib = response.mib;
		mibDatabase.mibs.add(mib);
		List<String> systemStateStrings = mib.getDataStrings();
		String systemName = systemStateStrings.get(0);
		String stateName = systemStateStrings.get(1);
		
		if(systemName.equals(C4S2ServicesComputer.class.getSimpleName()))
			currentSystemMonitor.c4s2ServicesComputerState = C4S2ServicesComputerStatesEnum.valueOf(stateName);
		else if(systemName.equals(C4S2OperatorServicesComputer.class.getSimpleName()))
			currentSystemMonitor.c4s2OperatorServicesComputerState = C4S2OperatorServicesComputerStatesEnum.valueOf(stateName);
		else if(systemName.equals(EthernetSwitchIPRouter.class.getSimpleName()))
			currentSystemMonitor.ethernetSwitchIPRouterState = EthernetSwitchIPRouterStatesEnum.valueOf(stateName);
		else if(systemName.equals(SIPRNetRouter.class.getSimpleName()))
			currentSystemMonitor.siprnetRouterState = SIPRNetRouterStatesEnum.valueOf(stateName);
		
		messaging.transmit(new SystemMonitorMessage(currentSystemMonitor));
	}

	@Reception
	public void onSystemControl(C4S2SystemControl control)
	{
		logger.info(control.toString());
		if(!control.isFinalized())
		{
			currentSystemControl = control;
			InstantMilliseconds now = InstantMilliseconds.now();
	
			messaging.transmit(new RadarServiceControlMessage(new RadarServiceControl(currentSystemControl.radarServicesState, now)));
			messaging.transmit(new StrikeServiceControlMessage(new StrikeServiceControl(currentSystemControl.strikeServicesState, now)));
			messaging.transmit(new TargetServiceControlMessage(new TargetServiceControl(currentSystemControl.targetServicesState, now)));
			messaging.transmit(new OperatorServiceControlMessage(new OperatorServiceControl(currentSystemControl.operatorServicesState, now)));
			messaging.transmit(new SystemServiceControlMessage(new SystemServiceControl(currentSystemControl.systemServicesState, now)));
			MIB c4s2ServicesComputerMIB = new MIB(now, C4S2ServicesComputer.class.getSimpleName(), currentSystemControl.c4s2ServicesComputerState.toString());
			MIB operatorServicesComputerMIB = new MIB(now, C4S2OperatorServicesComputer.class.getSimpleName(), currentSystemControl.c4s2OperatorServicesComputerState.toString());
			MIB switchRouterMIB = new MIB(now, EthernetSwitchIPRouter.class.getSimpleName(), currentSystemControl.ethernetSwitchIPRouterState.toString());
			MIB siprnetRouterMIB = new MIB(now, SIPRNetRouter.class.getSimpleName(), currentSystemControl.siprnetRouterState.toString());
			snmpSIPRNetRouter.transmit(new SNMPRequest(now, siprnetRouterMIB));
			snmpOperatorServicesComputer.transmit(new SNMPRequest(now, operatorServicesComputerMIB));
			snmpSwitchRouter.transmit(new SNMPRequest(now, switchRouterMIB));
			messaging.transmit(new SystemServiceControlMessage(new SystemServiceControl(currentSystemControl.systemServicesState, now)));
			snmpC4S2ServicesComputer.transmit(new SNMPRequest(now, c4s2ServicesComputerMIB));
		}
		else
			shutdownSystem();
	}

	@Operation
	private void shutdownSystem()
	{
	}

	public void onRadarServicesMonitor(RadarServiceMonitor serviceMonitor)
	{
		logger.info(serviceMonitor.toString());
		serviceMonitorDatabase.radarServiceMonitors.add(serviceMonitor);
		currentSystemMonitor.radarServicesState = serviceMonitor.state;
		SystemMonitorMessage message = new SystemMonitorMessage(currentSystemMonitor);
		messaging.transmit(message);		
	}

	public void onStrikeServicesMonitor(StrikeServiceMonitor serviceMonitor)
	{
		logger.info(serviceMonitor.toString());
		serviceMonitorDatabase.strikeServiceMonitors.add(serviceMonitor);
		currentSystemMonitor.strikeServicesState = serviceMonitor.state;
		SystemMonitorMessage message = new SystemMonitorMessage(currentSystemMonitor);
		messaging.transmit(message);		
	}

	public void onTargetServicesMonitor(TargetServiceMonitor serviceMonitor)
	{
		logger.info(serviceMonitor.toString());
		serviceMonitorDatabase.targetServiceMonitors.add(serviceMonitor);
		currentSystemMonitor.targetServicesState = serviceMonitor.state;
		SystemMonitorMessage message = new SystemMonitorMessage(currentSystemMonitor);
		messaging.transmit(message);		
	}

	public void onSystemServicesMonitor(SystemServiceMonitor serviceMonitor)
	{
		logger.info(serviceMonitor.toString());
		serviceMonitorDatabase.systemServiceMonitors.add(serviceMonitor);
		currentSystemMonitor.systemServicesState = serviceMonitor.state;
		SystemMonitorMessage message = new SystemMonitorMessage(currentSystemMonitor);
		messaging.transmit(message);		
	}

	public void onOperatorServicesMonitor(OperatorServiceMonitor serviceMonitor)
	{
		logger.info(serviceMonitor.toString());
		serviceMonitorDatabase.operatorServiceMonitors.add(serviceMonitor);
		currentSystemMonitor.operatorServicesState = serviceMonitor.state;
		SystemMonitorMessage message = new SystemMonitorMessage(currentSystemMonitor);
		messaging.transmit(message);		
	}

	public void onSystemServicesControl(SystemServiceControl control)
	{
		logger.info(control.toString());
		currentServiceControl = control;
		switch(currentServiceControl.state.ordinal)
		{
		case Initial:
			logger.severe("unable to transition to Initial state from current state");
			break;
		case Initializing:
		case Operational:
			currentServiceMonitor.state = currentServiceControl.state;
			currentServiceMonitor.time = InstantMilliseconds.now();
			serviceMonitorDatabase.systemServiceMonitors.add(currentServiceMonitor);
			currentSystemMonitor.systemServicesState = currentServiceMonitor.state;
			messaging.transmit(new SystemMonitorMessage(currentSystemMonitor));
			break;
		case Final:
			acceptEvent(new FinalEvent());
			break;
		}
	}
	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new SystemServicesStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		super.createValues();
		currentSystemMonitor = new SystemMonitor(C4S2ServicesComputerStatesEnum.Initial, C4S2OperatorServicesComputerStatesEnum.Initial, EthernetSwitchIPRouterStatesEnum.Initial, SIPRNetRouterStatesEnum.Initial,
			ServiceStatesEnum.initial, ServiceStatesEnum.initial, ServiceStatesEnum.initial, ServiceStatesEnum.initial, ServiceStatesEnum.initial, InstantMilliseconds.now());
		currentSystemControl = new C4S2SystemControl(C4S2ServicesComputerStatesEnum.Initial, C4S2OperatorServicesComputerStatesEnum.Initial, EthernetSwitchIPRouterStatesEnum.Initial, SIPRNetRouterStatesEnum.Initial,
			ServiceStatesEnum.initial, ServiceStatesEnum.initial, ServiceStatesEnum.initial, ServiceStatesEnum.initial, ServiceStatesEnum.initial, InstantMilliseconds.now());
		currentServiceControl = new SystemServiceControl(ServiceStatesEnum.initial, InstantMilliseconds.now());
		currentServiceMonitor = new SystemServiceMonitor(ServiceStatesEnum.initial, InstantMilliseconds.now());
	}

	@Override
	protected void createParts()
	{
		super.createParts();
		serviceMonitorDatabase = new ServiceMonitorDatabase();
		mibDatabase = new SNMPMIBDatabase();
	}

	@Override
	protected void createFullPorts()
	{
		messaging = new C4S2MessagingProtocol(this, 0L, "SystemServicesMessaging");
		snmpOperatorServicesComputer = new SNMPManagerProtocol(this, 0L);
		snmpC4S2ServicesComputer = new SNMPManagerProtocol(this, 0L);
		snmpSwitchRouter = new SNMPManagerProtocol(this, 0L);
		snmpSIPRNetRouter = new SNMPManagerProtocol(this, 0L);
	}
}
