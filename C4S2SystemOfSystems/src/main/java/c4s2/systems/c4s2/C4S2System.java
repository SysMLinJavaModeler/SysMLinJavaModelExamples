package c4s2.systems.c4s2;

import java.util.concurrent.TimeUnit;

import c4s2.common.attributetypes.C4S2SystemComponentsEnum;
import c4s2.components.computer.operator.C4S2OperatorServicesComputer;
import c4s2.components.computer.services.C4S2ServicesComputer;
import c4s2.parametrics.SWAPCAnalysisCase;
import c4s2.parametrics.SWAPCAnalysisCase.ParamIDs;
import sysmlinjava.attributetypes.Cost$US;
import sysmlinjava.attributetypes.HeatWatts;
import sysmlinjava.attributetypes.MassKilograms;
import sysmlinjava.attributetypes.PowerWatts;
import sysmlinjava.attributetypes.VolumeMetersCubic;
import sysmlinjava.connectors.SysMLBindingConnector;
import sysmlinjava.connectors.SysMLFlowConnector;
import sysmlinjava.connectors.SysMLFlowConnector.TypesEnum;
import sysmlinjava.javaannotations.analysis.parametrics.ParametricAnalysis;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.connectors.BindingConnector;
import sysmlinjava.javaannotations.connectors.FlowConnector;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.views.htmldisplay.HTMLDisplay;
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
public class C4S2System extends SysMLPart
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
	@Attribute
	public VolumeMetersCubic maxSize;
	/**
	 * Value for the maximum weight of the system
	 */
	@Attribute
	public MassKilograms maxWeight;
	/**
	 * Value for the maximum electrical power input to the system
	 */
	@Attribute
	public PowerWatts maxPowerIn;
	/**
	 * Value for the maximum heat output by the system
	 */
	@Attribute
	public HeatWatts maxHeatOut;
	/**
	 * Value for the maximum cost of the system
	 */
	@Attribute
	public Cost$US maxCost;

	/**
	 * Connector between the C4S2 services computer's SNMP manager and its SNMP
	 * agent
	 */
	@FlowConnector
	public SysMLFlowConnector c4s2ServicesComputerToSNMP;
	/**
	 * Connector between the C4S2 services computer's SNMP manager and the C4S2
	 * operator services computer's SNMP agent
	 */
	@FlowConnector
	public SysMLFlowConnector operatorServicesComputerToSNMP;
	/**
	 * Connector between the C4S2 services computer's SNMP manager the ethernet
	 * switch/IP router's SNMP agent
	 */
	@FlowConnector
	public SysMLFlowConnector switchRouterToSNMP;
	/**
	 * Connector between the C4S2 services computer's SNMP manager the SIPRNet
	 * router's SNMP agent
	 */
	@FlowConnector
	public SysMLFlowConnector siprnetRouterToSNMP;
	/**
	 * Connector between the C4S2 services computer's ethernet and ethernet switch/IP router's protocols
	 */
	@FlowConnector
	public SysMLFlowConnector c4s2ServicesComputerToSwitchRouterEthernet;
	/**
	 * Connector between the C4S2 operator services computer's ethernet the ethernet switch/IP router's protocols
	 */
	@FlowConnector
	public SysMLFlowConnector operatorServicesComputerToSwitchRouterEthernet;
	/**
	 * Connector between the SIPRNet router's ethernet and the
	 * ethernet switch/IP router's protocols
	 */
	@FlowConnector
	public SysMLFlowConnector siprnetRouterToSwitchRouterEthernet;
	/**
	 * Connector between the C4S2 operator services computer's services and the C4S2
	 * services computer's services IP protocols
	 */
	@FlowConnector
	public SysMLFlowConnector operatorServiceComputerToC4S2ServicesComputerIP;
	/**
	 * Connector between the C4S2 operator services computer's services and the C4S2
	 * services computer's services UDP protocols
	 */
	@FlowConnector
	public SysMLFlowConnector operatorServiceComputerToC4S2ServicesComputerUDP;
	/**
	 * Connector between the C4S2 services computer's IP protocol and the SIPRNet
	 * router's IP protocol
	 */
	@FlowConnector
	public SysMLFlowConnector c4s2ServicesComputerToSIPRNetRouterIP;

	/**
	 * Constraint block for computing the SWAP-C values for the system
	 */
	@ParametricAnalysis
	public SWAPCAnalysisCase swapcAnalysis;

	/**
	 * Connector for binding the analysis parameter for the system component's size
	 */
	@BindingConnector
	private SysMLBindingConnector c4s2SServicesComputerVolumeBindingConnector;
	/**
	 * Connector for binding the analysis parameter for the system component's size
	 */
	@BindingConnector
	private SysMLBindingConnector operatorServicesComputerVolumeBindingConnector;
	/**
	 * Connector for binding the analysis parameter for the system component's size
	 */
	@BindingConnector
	private SysMLBindingConnector switchRouterVolumeBindingConnector;
	/**
	 * Connector for binding the analysis parameter for the system component's size
	 */
	@BindingConnector
	private SysMLBindingConnector siprnetRouterVolumeBindingConnector;

	/**
	 * Connector for binding the analysis parameter for the system component's weight
	 */
	@BindingConnector
	private SysMLBindingConnector c4s2SServicesComputerWeightBindingConnector;
	/**
	 * Connector for binding the analysis parameter for the system component's weight
	 */
	@BindingConnector
	private SysMLBindingConnector operatorServicesComputerWeightBindingConnector;
	/**
	 * Connector for binding the analysis parameter for the system component's weight
	 */
	@BindingConnector
	private SysMLBindingConnector switchRouterWeightBindingConnector;
	/**
	 * Connector for binding the analysis parameter for the system component's weight
	 */
	@BindingConnector
	private SysMLBindingConnector siprnetRouterWeightBindingConnector;

	/**
	 * Connector for binding the analysis parameter for the system component's power
	 */
	@BindingConnector
	private SysMLBindingConnector c4s2SServicesComputerPowerBindingConnector;
	/**
	 * Connector for binding the analysis parameter for the system component's power
	 */
	@BindingConnector
	private SysMLBindingConnector operatorServicesComputerPowerBindingConnector;
	/**
	 * Connector for binding the analysis parameter for the system component's power
	 */
	@BindingConnector
	private SysMLBindingConnector switchRouterPowerBindingConnector;
	/**
	 * Connector for binding the analysis parameter for the system component's power
	 */
	@BindingConnector
	private SysMLBindingConnector siprnetRouterPowerBindingConnector;

	/**
	 * Connector for binding the analysis parameter for the system component's
	 * cooling
	 */
	@BindingConnector
	private SysMLBindingConnector c4s2SServicesComputerHeatBindingConnector;
	/**
	 * Connector for binding the analysis parameter for the system component's
	 * cooling
	 */
	@BindingConnector
	private SysMLBindingConnector operatorServicesComputerHeatBindingConnector;
	/**
	 * Connector for binding the analysis parameter for the system component's
	 * cooling
	 */
	@BindingConnector
	private SysMLBindingConnector switchRouterHeatBindingConnector;
	/**
	 * Connector for binding the analysis parameter for the system component's
	 * cooling
	 */
	@BindingConnector
	private SysMLBindingConnector siprnetRouterHeatBindingConnector;

	/**
	 * Constructor
	 */
	public C4S2System()
	{
		super();
	}

	@Override
	public void start()
	{
		swapcAnalysis.start();
		switchRouter.start();
		siprnetRouter.start();
		c4s2ServicesComputer.start();
		operatorServicesComputer.start();
	}

	/**
	 * Waits for each subsystem to terminate execution.
	 */
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
		swapcAnalysis.stop();
	}

	@Override
	protected void createAttributes()
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
		switchRouter = new EthernetSwitchIPRouter(C4S2SystemComponentsEnum.EthernetSwitchIPRouter.name(), 0);
		siprnetRouter = new SIPRNetRouter(C4S2SystemComponentsEnum.SIPRNetRouter.name(), 0);
	}

	@Override
	protected void createFlowConnectors()
	{
		c4s2ServicesComputerToSNMP = new SysMLFlowConnector(TypesEnum.peertopeer, true, c4s2ServicesComputer.systemServices.snmpC4S2ServicesComputer, c4s2ServicesComputer.snmpAgent, "", 0L);
		operatorServicesComputerToSNMP = new SysMLFlowConnector(TypesEnum.peertopeer, true, c4s2ServicesComputer.systemServices.snmpOperatorServicesComputer, operatorServicesComputer.snmpAgent, "", 0L);
		switchRouterToSNMP = new SysMLFlowConnector(TypesEnum.peertopeer, true, c4s2ServicesComputer.systemServices.snmpSwitchRouter, switchRouter.snmpAgent, "", 0L);
		siprnetRouterToSNMP = new SysMLFlowConnector(TypesEnum.peertopeer, true, c4s2ServicesComputer.systemServices.snmpSIPRNetRouter, siprnetRouter.snmpAgent, "", 0L);
		
		operatorServiceComputerToC4S2ServicesComputerUDP = new SysMLFlowConnector(TypesEnum.peertopeer, true, operatorServicesComputer.udp, c4s2ServicesComputer.udp, "", 0L);
		operatorServiceComputerToC4S2ServicesComputerIP = new SysMLFlowConnector(TypesEnum.peertopeer, true, operatorServicesComputer.ip, c4s2ServicesComputer.ip, "", 0L);
		operatorServicesComputerToSwitchRouterEthernet = new SysMLFlowConnector(TypesEnum.peertopeer, true, operatorServicesComputer.ethernet, switchRouter.ethernet0, "", 0L);
		c4s2ServicesComputerToSwitchRouterEthernet = new SysMLFlowConnector(TypesEnum.peertopeer, true, c4s2ServicesComputer.ethernet, switchRouter.ethernet0, "", 0L);

		siprnetRouterToSwitchRouterEthernet = new SysMLFlowConnector(TypesEnum.peertopeer, true, siprnetRouter.haipe.ethernetDecrypted, switchRouter.ethernet2, "", 0L);
		c4s2ServicesComputerToSIPRNetRouterIP = new SysMLFlowConnector(TypesEnum.peertopeer, true, c4s2ServicesComputer.ip, siprnetRouter.haipe.ipDecrypted, "", 0L);
	}

	@Override
	protected void createAnalysisCases()
	{
		swapcAnalysis = new SWAPCAnalysisCase(this, HTMLDisplay.udpPort);
	}

	@Override
	protected void createBindingConnectors()
	{
		c4s2SServicesComputerVolumeBindingConnector = new SysMLBindingConnector(c4s2ServicesComputer.maxSize, swapcAnalysis, ParamIDs.c4s2ServicesComputerVolume.toString());
		operatorServicesComputerVolumeBindingConnector = new SysMLBindingConnector(operatorServicesComputer.maxSize, swapcAnalysis, ParamIDs.c4s2OperatorServicesComputerVolume.toString());
		switchRouterVolumeBindingConnector = new SysMLBindingConnector(switchRouter.maxSize, swapcAnalysis, ParamIDs.switchRouterVolume.toString());
		siprnetRouterVolumeBindingConnector = new SysMLBindingConnector(siprnetRouter.maxSize, swapcAnalysis, ParamIDs.siprnetRouterVolume.toString());

		c4s2SServicesComputerWeightBindingConnector = new SysMLBindingConnector(c4s2ServicesComputer.maxWeight, swapcAnalysis, ParamIDs.c4s2ServicesComputerWeight.toString());
		operatorServicesComputerWeightBindingConnector = new SysMLBindingConnector(operatorServicesComputer.maxWeight, swapcAnalysis, ParamIDs.c4s2OperatorServicesComputerWeight.toString());
		switchRouterWeightBindingConnector = new SysMLBindingConnector(switchRouter.maxWeight, swapcAnalysis, ParamIDs.switchRouterWeight.toString());
		siprnetRouterWeightBindingConnector = new SysMLBindingConnector(siprnetRouter.maxWeight, swapcAnalysis, ParamIDs.siprnetRouterWeight.toString());

		c4s2SServicesComputerPowerBindingConnector = new SysMLBindingConnector(c4s2ServicesComputer.maxPowerIn, swapcAnalysis, ParamIDs.c4s2ServicesComputerPower.toString());
		operatorServicesComputerPowerBindingConnector = new SysMLBindingConnector(operatorServicesComputer.maxPowerIn, swapcAnalysis, ParamIDs.c4s2OperatorServicesComputerPower.toString());
		switchRouterPowerBindingConnector = new SysMLBindingConnector(switchRouter.maxPowerIn, swapcAnalysis, ParamIDs.switchRouterPower.toString());
		siprnetRouterPowerBindingConnector = new SysMLBindingConnector(siprnetRouter.maxPowerIn, swapcAnalysis, ParamIDs.siprnetRouterPower.toString());

		c4s2SServicesComputerHeatBindingConnector = new SysMLBindingConnector(c4s2ServicesComputer.maxHeatOut, swapcAnalysis, ParamIDs.c4s2ServicesComputerHeat.toString());
		operatorServicesComputerHeatBindingConnector = new SysMLBindingConnector(operatorServicesComputer.maxHeatOut, swapcAnalysis, ParamIDs.c4s2OperatorServicesComputerHeat.toString());
		switchRouterHeatBindingConnector = new SysMLBindingConnector(switchRouter.maxHeatOut, swapcAnalysis, ParamIDs.switchRouterHeat.toString());
		siprnetRouterHeatBindingConnector = new SysMLBindingConnector(siprnetRouter.maxHeatOut, swapcAnalysis, ParamIDs.siprnetRouterHeat.toString());

		swapcAnalysis.paramConnectors.put(ParamIDs.c4s2ServicesComputerVolume.toString(), c4s2SServicesComputerVolumeBindingConnector);
		swapcAnalysis.paramConnectors.put(ParamIDs.c4s2OperatorServicesComputerVolume.toString(), operatorServicesComputerVolumeBindingConnector);
		swapcAnalysis.paramConnectors.put(ParamIDs.switchRouterVolume.toString(), switchRouterVolumeBindingConnector);
		swapcAnalysis.paramConnectors.put(ParamIDs.siprnetRouterVolume.toString(), siprnetRouterVolumeBindingConnector);
		swapcAnalysis.paramConnectors.put(ParamIDs.c4s2ServicesComputerWeight.toString(), c4s2SServicesComputerWeightBindingConnector);
		swapcAnalysis.paramConnectors.put(ParamIDs.c4s2OperatorServicesComputerWeight.toString(), operatorServicesComputerWeightBindingConnector);
		swapcAnalysis.paramConnectors.put(ParamIDs.switchRouterWeight.toString(), switchRouterWeightBindingConnector);
		swapcAnalysis.paramConnectors.put(ParamIDs.siprnetRouterWeight.toString(), siprnetRouterWeightBindingConnector);
		swapcAnalysis.paramConnectors.put(ParamIDs.c4s2ServicesComputerPower.toString(), c4s2SServicesComputerPowerBindingConnector);
		swapcAnalysis.paramConnectors.put(ParamIDs.c4s2OperatorServicesComputerPower.toString(), operatorServicesComputerPowerBindingConnector);
		swapcAnalysis.paramConnectors.put(ParamIDs.switchRouterPower.toString(), switchRouterPowerBindingConnector);
		swapcAnalysis.paramConnectors.put(ParamIDs.siprnetRouterPower.toString(), siprnetRouterPowerBindingConnector);
		swapcAnalysis.paramConnectors.put(ParamIDs.c4s2ServicesComputerHeat.toString(), c4s2SServicesComputerHeatBindingConnector);
		swapcAnalysis.paramConnectors.put(ParamIDs.c4s2OperatorServicesComputerHeat.toString(), operatorServicesComputerHeatBindingConnector);
		swapcAnalysis.paramConnectors.put(ParamIDs.switchRouterHeat.toString(), switchRouterHeatBindingConnector);
		swapcAnalysis.paramConnectors.put(ParamIDs.siprnetRouterHeat.toString(), siprnetRouterHeatBindingConnector);
	}
}
