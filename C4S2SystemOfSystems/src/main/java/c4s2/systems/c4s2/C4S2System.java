package c4s2.systems.c4s2;

import java.util.concurrent.TimeUnit;
import c4s2.common.valueTypes.C4S2SystemComponentsEnum;
import c4s2.components.computer.operator.C4S2OperatorServicesComputer;
import c4s2.components.computer.services.C4S2ServicesComputer;
import c4s2.parametrics.SWAPCConstraintBlock;
import c4s2.parametrics.SWAPCConstraintBlock.ParamIDs;
import sysmlinjava.analysis.htmldisplay.HTMLDisplay;
import sysmlinjava.annotations.AssociationConnector;
import sysmlinjava.annotations.AssociationConnectorFunction;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.parametrics.BindingConnector;
import sysmlinjava.annotations.parametrics.BindingConnectorFunction;
import sysmlinjava.annotations.parametrics.ConstraintBlock;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.connectors.SysMLAssociationBlockConnector;
import sysmlinjava.connectors.SysMLAssociationBlockConnectorFunction;
import sysmlinjava.connectors.SysMLBindingConnector;
import sysmlinjava.connectors.SysMLBindingConnectorFunction;
import sysmlinjava.ports.SysMLConstraintParameterPort;
import sysmlinjava.valuetypes.Cost$US;
import sysmlinjava.valuetypes.HeatWatts;
import sysmlinjava.valuetypes.MassKilograms;
import sysmlinjava.valuetypes.PowerWatts;
import sysmlinjava.valuetypes.VolumeMetersCubic;
import sysmlinjavalibrary.components.communications.internet.EthernetSwitchIPRouter;
import sysmlinjavalibrary.components.communications.siprnet.SIPRNetRouter;

/**
 * The {@code C4S2System} is a SysMLinJava model of a command/control
 * computer/communications (C4) system that controls surveillance and strike
 * (S2) operations. The system consists of two computers, i.e. an operator
 * services computer and a services computer, and connected communications
 * equipment, i.e. an ethernet switch/IP router and a SIPRNet router. The
 * {@code C4S2System} also makes and contains the connections between these
 * system componets.
 * <p>
 * The systems only behavior is to start and stop the system components. As
 * such, it has no state machine.
 * <p>
 * The {@code C4S2System} also contains a {@code SysMLConstraintBlock}. This
 * constraint block computes the classic SWAP-C for the system, i.e. the size,
 * weight, power, and cooling requirements of the system. These constraints
 * would be presumably used to develop and maintain a correct design of the
 * platform on which the {@code C4S2System} would be located.
 * 
 * @author ModelerOne
 *
 */
public class C4S2System extends SysMLBlock
{
	/**
	 * Part that represents the operator services computer
	 */
	@Part
	public C4S2OperatorServicesComputer operatorServicesComputer;
	/**
	 * Part that represents the C4S2 services computer
	 */
	@Part
	public C4S2ServicesComputer c4s2ServicesComputer;
	/**
	 * Part that represents the ethernet switch/IP router
	 */
	@Part
	public EthernetSwitchIPRouter switchRouter;
	/**
	 * Part that represents the operator SIPRNet router
	 */
	@Part
	public SIPRNetRouter siprnetRouter;

	/**
	 * Value for the maximum size of the system
	 */
	@Value
	public VolumeMetersCubic maxSize;
	/**
	 * Value for the maximum weight of the system
	 */
	@Value
	public MassKilograms maxWeight;
	/**
	 * Value for the maximum electrical power input to the system
	 */
	@Value
	public PowerWatts maxPowerIn;
	/**
	 * Value for the maximum heat output by the system
	 */
	@Value
	public HeatWatts maxHeatOut;
	/**
	 * Value for the maximum cost of the system
	 */
	@Value
	public Cost$US maxCost;

	/**
	 * Functions that make connectors between the C4S2 services computer's SNMP
	 * manager and its SNMP agent.
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction c4s2ServicesComputerSNMPConnectorFunction;
	/**
	 * Functions that make connectors between the C4S2 services computer's SNMP
	 * manager and the C4S2 operator services computer's SNMP agent
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction operatorServicesComputerSNMPConnectorFunction;
	/**
	 * Functions that make connectors between the C4S2 services computer's SNMP
	 * manager the ethernet switch/IP router's SNMP agent
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction switchRouterSNMPConnectorFunction;
	/**
	 * Functions that make connectors between the C4S2 services computer's SNMP
	 * manager the SIPRNet router's SNMP agent
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction siprnetRouterSNMPConnectorFunction;
	/**
	 * Functions that make connectors between the C4S2 services computer's ethernet
	 * and IP protocols and the ethernet switch/IP router's protocols
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction c4s2ServicesComputerToSwitchRouterConnectorFunction;
	/**
	 * Functions that make connectors between the C4S2 operator services computer's
	 * ethernet and IP protocols and the ethernet switch/IP router's protocols
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction operatorServicesComputerToSwitchRouterConnectorFunction;
	/**
	 * Functions that make connectors between the SIPRNet router's ethernet and IP
	 * protocols and the ethernet switch/IP router's protocols
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction siprnetRouterToSwitchRouterConnectorFunction;
	/**
	 * Functions that make connectors between the C4S2 operator services computer's
	 * services and the C4S2 services computer's services
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction operatorServiceComputerToC4S2ServicesComputerConnectorFunction;
	/**
	 * Functions that make connectors between the C4S2 services computer's IP
	 * protocol and the SIPRNet router's IP protocol
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction c4s2ServicesComputerToSIPRNetRouterConnectorFunction;

	/**
	 * Connector between the C4S2 services computer's SNMP manager and its SNMP
	 * agent
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector c4s2ServicesComputerSNMPConnector;
	/**
	 * Connector between the C4S2 services computer's SNMP manager and the C4S2
	 * operator services computer's SNMP agent
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector operatorServicesComputerSNMPConnector;
	/**
	 * Connector between the C4S2 services computer's SNMP manager the ethernet
	 * switch/IP router's SNMP agent
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector switchRouterSNMPConnector;
	/**
	 * Connector between the C4S2 services computer's SNMP manager the SIPRNet
	 * router's SNMP agent
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector siprnetRouterSNMPConnector;
	/**
	 * Connector between the C4S2 services computer's ethernet and IP protocols and
	 * the ethernet switch/IP router's protocols
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector c4s2ServicesComputerToSwitchRouterConnector;
	/**
	 * Connector between the C4S2 operator services computer's ethernet and IP
	 * protocols and the ethernet switch/IP router's protocols
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector operatorServicesComputerToSwitchRouterConnector;
	/**
	 * Connector between the SIPRNet router's ethernet and IP protocols and the
	 * ethernet switch/IP router's protocols
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector siprnetRouterToSwitchRouterConnector;
	/**
	 * Connector between the C4S2 operator services computer's services and the C4S2
	 * services computer's services
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector operatorServiceComputerToC4S2ServicesComputerConnector;
	/**
	 * Connector between the C4S2 services computer's IP protocol and the SIPRNet
	 * router's IP protocol
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector c4s2ServicesComputerToSIPRNetRouterConnector;

	/**
	 * Constraint block for computing the SWAP-C values for the system
	 */
	@ConstraintBlock
	public SWAPCConstraintBlock swapcConstraintBlock;

	/**
	 * Bound reference connectors for the parameter values used for the SWAP-C
	 * constraints
	 */
	@BindingConnectorFunction
	public SysMLBindingConnectorFunction swapcC4S2SystemConstraintParameterConnectorFunction;

	/**
	 * Bound references for the parameter values used for the SWAP-C constraints
	 */
	@BindingConnector
	public SysMLBindingConnector swapcC4S2SystemConstraintParameterConnector;
	
	@BindingConnectorFunction
	private SysMLBindingConnectorFunction c4s2SServiesComputerSizeConnectorFunction;
	@BindingConnectorFunction
	private SysMLBindingConnectorFunction operatorServiesComputerSizeConnectorFunction;
	@BindingConnectorFunction
	private SysMLBindingConnectorFunction switchRouterSizeConnectorFunction;
	@BindingConnectorFunction
	private SysMLBindingConnectorFunction siprnetRouterSizeConnectorFunction;

	@BindingConnectorFunction
	private SysMLBindingConnectorFunction c4s2SServiesComputerWeightConnectorFunction;
	@BindingConnectorFunction
	private SysMLBindingConnectorFunction operatorServiesComputerWeightConnectorFunction;
	@BindingConnectorFunction
	private SysMLBindingConnectorFunction switchRouterWeightConnectorFunction;
	@BindingConnectorFunction
	private SysMLBindingConnectorFunction siprnetRouterWeightConnectorFunction;
	
	@BindingConnectorFunction
	private SysMLBindingConnectorFunction c4s2SServiesComputerPowerConnectorFunction;
	@BindingConnectorFunction
	private SysMLBindingConnectorFunction operatorServiesComputerPowerConnectorFunction;
	@BindingConnectorFunction
	private SysMLBindingConnectorFunction switchRouterPowerConnectorFunction;
	@BindingConnectorFunction
	private SysMLBindingConnectorFunction siprnetRouterPowerConnectorFunction;
	
	@BindingConnectorFunction
	private SysMLBindingConnectorFunction c4s2SServiesComputerCoolingConnectorFunction;
	@BindingConnectorFunction
	private SysMLBindingConnectorFunction operatorServiesComputerCoolingConnectorFunction;
	@BindingConnectorFunction
	private SysMLBindingConnectorFunction switchRouterCoolingConnectorFunction;
	@BindingConnectorFunction
	private SysMLBindingConnectorFunction siprnetRouterCoolingConnectorFunction;

	public C4S2System()
	{
		super();
	}

	@Override
	public void start()
	{
		swapcConstraintBlock.start();
		switchRouter.start();
		siprnetRouter.start();
		c4s2ServicesComputer.start();
		operatorServicesComputer.start();
	}

	public void awaitTermination()
	{
		try
		{
			operatorServicesComputer.concurrentExecutionThreads.awaitTermination(30, TimeUnit.MINUTES);
			c4s2ServicesComputer.concurrentExecutionThreads.awaitTermination(30, TimeUnit.MINUTES);
			siprnetRouter.concurrentExecutionThreads.awaitTermination(30, TimeUnit.MINUTES);
			switchRouter.concurrentExecutionThreads.awaitTermination(30, TimeUnit.MINUTES);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void stop()
	{
		operatorServicesComputer.stop();
		c4s2ServicesComputer.stop();
		siprnetRouter.stop();
		switchRouter.stop();
		swapcConstraintBlock.stop();
	}

	@Override
	protected void createValues()
	{
		maxSize = new VolumeMetersCubic(0.1);
		maxWeight = new MassKilograms(100.0);
		maxPowerIn = new PowerWatts(1500);
		maxHeatOut = new HeatWatts(1400);
		maxCost = new Cost$US(10000.00);
	}

	@Override
	protected void createParts()
	{
		c4s2ServicesComputer = new C4S2ServicesComputer();
		operatorServicesComputer = new C4S2OperatorServicesComputer();
		switchRouter = new EthernetSwitchIPRouter(C4S2SystemComponentsEnum.EthernetSwitchIPRouter.name, 0);
		siprnetRouter = new SIPRNetRouter(C4S2SystemComponentsEnum.SIPRNetRouter.name, 0);
	}

	@Override
	protected void createConnectorFunctions()
	{
		c4s2ServicesComputerSNMPConnectorFunction = () ->
		{
			c4s2ServicesComputer.systemServices.snmpC4S2ServicesComputer.addConnectedPortPeer(c4s2ServicesComputer.snmpAgent);
			c4s2ServicesComputer.snmpAgent.addConnectedPortPeer(c4s2ServicesComputer.systemServices.snmpC4S2ServicesComputer);
		};
		operatorServicesComputerSNMPConnectorFunction = () ->
		{
			c4s2ServicesComputer.systemServices.snmpOperatorServicesComputer.addConnectedPortPeer(operatorServicesComputer.snmpAgent);
			operatorServicesComputer.snmpAgent.addConnectedPortPeer(c4s2ServicesComputer.systemServices.snmpOperatorServicesComputer);
		};
		switchRouterSNMPConnectorFunction = () ->
		{
			c4s2ServicesComputer.systemServices.snmpSwitchRouter.addConnectedPortPeer(switchRouter.snmpAgent);
			switchRouter.snmpAgent.addConnectedPortPeer(c4s2ServicesComputer.systemServices.snmpSwitchRouter);
		};
		siprnetRouterSNMPConnectorFunction = () ->
		{
			c4s2ServicesComputer.systemServices.snmpSIPRNetRouter.addConnectedPortPeer(siprnetRouter.snmpAgent);
			siprnetRouter.snmpAgent.addConnectedPortPeer(c4s2ServicesComputer.systemServices.snmpSIPRNetRouter);
		};
		operatorServiceComputerToC4S2ServicesComputerConnectorFunction = () ->
		{
			operatorServicesComputer.ip.addConnectedPortPeer(c4s2ServicesComputer.ip);
			c4s2ServicesComputer.ip.addConnectedPortPeer(operatorServicesComputer.ip);
			operatorServicesComputer.udp.addConnectedPortPeer(c4s2ServicesComputer.udp);
			c4s2ServicesComputer.udp.addConnectedPortPeer(operatorServicesComputer.udp);
		};
		c4s2ServicesComputerToSwitchRouterConnectorFunction = () ->
		{
			c4s2ServicesComputer.ethernet.addConnectedPortPeer(switchRouter.ethernet0);
			switchRouter.ethernet0.addConnectedPortPeer(c4s2ServicesComputer.ethernet);
		};
		operatorServicesComputerToSwitchRouterConnectorFunction = () ->
		{
			operatorServicesComputer.ethernet.addConnectedPortPeer(switchRouter.ethernet1);
			switchRouter.ethernet1.addConnectedPortPeer(operatorServicesComputer.ethernet);
		};
		siprnetRouterToSwitchRouterConnectorFunction = () ->
		{
			siprnetRouter.haipe.ethernetDecrypted.addConnectedPortPeer(switchRouter.ethernet2);
			switchRouter.ethernet2.addConnectedPortPeer(siprnetRouter.haipe.ethernetDecrypted);
		};
		c4s2ServicesComputerToSIPRNetRouterConnectorFunction = () ->
		{
			c4s2ServicesComputer.ip.addConnectedPortPeer(siprnetRouter.haipe.ipDecrypted);
			siprnetRouter.haipe.ipDecrypted.addConnectedPortPeer(c4s2ServicesComputer.ip);
		};
	}

	@Override
	protected void createConnectors()
	{
		c4s2ServicesComputerSNMPConnector = new SysMLAssociationBlockConnector(c4s2ServicesComputer.systemServices, c4s2ServicesComputer, c4s2ServicesComputerSNMPConnectorFunction);
		operatorServicesComputerSNMPConnector = new SysMLAssociationBlockConnector(c4s2ServicesComputer.systemServices, operatorServicesComputer, operatorServicesComputerSNMPConnectorFunction);
		switchRouterSNMPConnector = new SysMLAssociationBlockConnector(c4s2ServicesComputer.systemServices, switchRouter, switchRouterSNMPConnectorFunction);
		siprnetRouterSNMPConnector = new SysMLAssociationBlockConnector(c4s2ServicesComputer.systemServices, siprnetRouter, siprnetRouterSNMPConnectorFunction);
		c4s2ServicesComputerToSwitchRouterConnector = new SysMLAssociationBlockConnector(c4s2ServicesComputer, switchRouter, c4s2ServicesComputerToSwitchRouterConnectorFunction);
		operatorServicesComputerToSwitchRouterConnector = new SysMLAssociationBlockConnector(operatorServicesComputer, switchRouter, operatorServicesComputerToSwitchRouterConnectorFunction);
		siprnetRouterToSwitchRouterConnector = new SysMLAssociationBlockConnector(c4s2ServicesComputer, siprnetRouter, siprnetRouterToSwitchRouterConnectorFunction);
		operatorServiceComputerToC4S2ServicesComputerConnector = new SysMLAssociationBlockConnector(operatorServicesComputer, c4s2ServicesComputer, operatorServiceComputerToC4S2ServicesComputerConnectorFunction);
	}

	@Override
	protected void createConstraintBlocks()
	{
		swapcConstraintBlock = new SWAPCConstraintBlock(this, HTMLDisplay.udpPort);
	}

	@Override
	protected void createConstraintParameterConnectorFunctions()
	{
		swapcC4S2SystemConstraintParameterConnectorFunction = () ->
		{
			SysMLConstraintParameterPort c4s2ServicesComputerVolumePort = swapcConstraintBlock.paramPorts.get(ParamIDs.c4s2ServicesComputerVolume.toString());
			c4s2ServicesComputerVolumePort.parameterContextBlock = c4s2ServicesComputer;
			c4s2ServicesComputer.maxSize.bindTo(c4s2ServicesComputerVolumePort);

			SysMLConstraintParameterPort c4s2ServicesComputerWeightPort = swapcConstraintBlock.paramPorts.get(ParamIDs.c4s2ServicesComputerWeight.toString());
			c4s2ServicesComputerWeightPort.parameterContextBlock = c4s2ServicesComputer;
			c4s2ServicesComputer.maxWeight.bindTo(c4s2ServicesComputerWeightPort);

			SysMLConstraintParameterPort c4s2ServicesComputerPowerPort = swapcConstraintBlock.paramPorts.get(ParamIDs.c4s2ServicesComputerPower.toString());
			c4s2ServicesComputerPowerPort.parameterContextBlock = c4s2ServicesComputer;
			c4s2ServicesComputer.maxPowerIn.bindTo(c4s2ServicesComputerPowerPort);

			SysMLConstraintParameterPort c4s2ServicesComputerHeatPort = swapcConstraintBlock.paramPorts.get(ParamIDs.c4s2ServicesComputerHeat.toString());
			c4s2ServicesComputerHeatPort.parameterContextBlock = c4s2ServicesComputer;
			c4s2ServicesComputer.maxHeatOut.bindTo(c4s2ServicesComputerHeatPort);
			//-------------------
			SysMLConstraintParameterPort operatorServicesComputerVolumePort = swapcConstraintBlock.paramPorts.get(ParamIDs.c4s2OperatorServicesComputerVolume.toString());
			operatorServicesComputerVolumePort.parameterContextBlock = operatorServicesComputer;
			operatorServicesComputer.maxSize.bindTo(operatorServicesComputerVolumePort);

			SysMLConstraintParameterPort operatorServicesComputerWeightPort = swapcConstraintBlock.paramPorts.get(ParamIDs.c4s2OperatorServicesComputerWeight.toString());
			operatorServicesComputerWeightPort.parameterContextBlock = operatorServicesComputer;
			operatorServicesComputer.maxWeight.bindTo(operatorServicesComputerWeightPort);

			SysMLConstraintParameterPort operatorServicesComputerPowerPort = swapcConstraintBlock.paramPorts.get(ParamIDs.c4s2OperatorServicesComputerPower.toString());
			operatorServicesComputerPowerPort.parameterContextBlock = operatorServicesComputer;
			operatorServicesComputer.maxPowerIn.bindTo(operatorServicesComputerPowerPort);

			SysMLConstraintParameterPort operatorServicesComputerHeatPort = swapcConstraintBlock.paramPorts.get(ParamIDs.c4s2OperatorServicesComputerHeat.toString());
			operatorServicesComputerHeatPort.parameterContextBlock = operatorServicesComputer;
			operatorServicesComputer.maxHeatOut.bindTo(operatorServicesComputerHeatPort);
			//-------------------
			SysMLConstraintParameterPort switchRouterVolumePort = swapcConstraintBlock.paramPorts.get(ParamIDs.switchRouterVolume.toString());
			switchRouterVolumePort.parameterContextBlock = switchRouter;
			switchRouter.maxSize.bindTo(switchRouterVolumePort);

			SysMLConstraintParameterPort switchRouterWeightPort = swapcConstraintBlock.paramPorts.get(ParamIDs.switchRouterWeight.toString());
			switchRouterWeightPort.parameterContextBlock = switchRouter;
			switchRouter.maxWeight.bindTo(switchRouterWeightPort);

			SysMLConstraintParameterPort switchRouterPowerPort = swapcConstraintBlock.paramPorts.get(ParamIDs.switchRouterPower.toString());
			switchRouterPowerPort.parameterContextBlock = switchRouter;
			switchRouter.maxPowerIn.bindTo(switchRouterPowerPort);

			SysMLConstraintParameterPort switchRouterHeatPort = swapcConstraintBlock.paramPorts.get(ParamIDs.switchRouterHeat.toString());
			switchRouterHeatPort.parameterContextBlock = switchRouter;
			switchRouter.maxHeatOut.bindTo(switchRouterHeatPort);
			//-------------------
			SysMLConstraintParameterPort siprnetRouterVolumePort = swapcConstraintBlock.paramPorts.get(ParamIDs.siprnetRouterVolume.toString());
			siprnetRouterVolumePort.parameterContextBlock = siprnetRouter;
			siprnetRouter.maxSize.bindTo(siprnetRouterVolumePort);

			SysMLConstraintParameterPort siprnetRouterWeightPort = swapcConstraintBlock.paramPorts.get(ParamIDs.siprnetRouterWeight.toString());
			siprnetRouterWeightPort.parameterContextBlock = siprnetRouter;
			siprnetRouter.maxWeight.bindTo(siprnetRouterWeightPort);

			SysMLConstraintParameterPort siprnetRouterPowerPort = swapcConstraintBlock.paramPorts.get(ParamIDs.siprnetRouterPower.toString());
			siprnetRouterPowerPort.parameterContextBlock = siprnetRouter;
			siprnetRouter.maxPowerIn.bindTo(siprnetRouterPowerPort);

			SysMLConstraintParameterPort siprnetRouterHeatPort = swapcConstraintBlock.paramPorts.get(ParamIDs.siprnetRouterHeat.toString());
			siprnetRouterHeatPort.parameterContextBlock = siprnetRouter;
			siprnetRouter.maxHeatOut.bindTo(siprnetRouterHeatPort);
		};
	}

	@Override
	protected void createConstraintParameterConnectors()
	{
		swapcC4S2SystemConstraintParameterConnector = new SysMLBindingConnector(this, swapcConstraintBlock, swapcC4S2SystemConstraintParameterConnectorFunction);
	}
}
