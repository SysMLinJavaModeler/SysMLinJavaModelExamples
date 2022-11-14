package hflink.components.deployedcomputer;

import hflink.common.ports.ElectricalPowerProtocol;
import hflink.common.ports.EthernetProtocol;
import hflink.common.ports.IP;
import hflink.common.ports.ThermalHeatTransferProtocol;
import hflink.common.ports.UDP;
import sysmlinjava.annotations.AssociationConnector;
import sysmlinjava.annotations.AssociationConnectorFunction;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.requirements.RequirementAttribute;
import sysmlinjava.annotations.requirements.RequirementInterfaceProtocol;
import sysmlinjava.annotations.requirements.RequirementInterfaceProtocolConnector;
import sysmlinjava.annotations.requirements.RequirementPhysical;
import sysmlinjava.annotations.requirements.RequirementReliability;
import sysmlinjava.annotations.requirements.RequirementResource;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.connectors.SysMLAssociationBlockConnector;
import sysmlinjava.connectors.SysMLAssociationBlockConnectorFunction;
import sysmlinjava.requirements.SysMLVerificationMethodKind;
import sysmlinjava.valuetypes.Cost$US;
import sysmlinjava.valuetypes.HeatWatts;
import sysmlinjava.valuetypes.MassKilograms;
import sysmlinjava.valuetypes.Percent;
import sysmlinjava.valuetypes.PowerWatts;
import sysmlinjava.valuetypes.SString;
import sysmlinjava.valuetypes.VolumeMetersCubic;

/**
 * The DeployedComputer is a system component that hosts the web-server-based
 * application for the command/control of a remote system. It receives HTTP
 * requests and transmits HTTP responses over internet protocols via the
 * ModemRadio with the web-browser hosted by a CommandControlComputer.
 * <p>
 * The DeployedComputer block includes full ports for each of the protocols used
 * to communicate with the command/control computer via the modem-radio. These
 * protocols include UDP over IP over ethernet for command/control computer
 * communications, and a web-server for controlled system interactions. It also
 * includes full ports for power and heat and their corresponding flows -
 * power-in and heat-out. Specified block values include availability, size,
 * weight, speed, and cost.
 * <p>
 * The block also contains all of the connectors between the ports in the block.
 * These include the connectors between the full ports that represent the
 * protocol stacks of the external interfaces. Note the connectors between
 * DeployedComputer protocols and external systems are specified in the
 * CommandControlSystem block and/or the HFLinkDomain block.
 * 
 * @author ModelerOne
 *
 */
public class DeployedComputer extends SysMLBlock
{
	/**
	 * Port for the web server hosted by the computer and used by the computer's
	 * services to receive controls and send monitors via HTTP over UDP to the C2
	 * computer
	 */
	@RequirementInterfaceProtocol(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@FullPort
	public WebServer DeployedWebServer;
	/**
	 * Port for the UDP used by the web server to communicate with the web browser
	 * on the C2 computer
	 */
	@RequirementInterfaceProtocol(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@FullPort
	public UDP UDP;
	/**
	 * Port for IP used by UDP to communicate with UDP ports on the C2 computer
	 */
	@RequirementInterfaceProtocol(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@FullPort
	public IP IP;
	/**
	 * Port for ethernet used by IP to communicate via router with IP on the C2
	 * computer
	 */
	@RequirementInterfaceProtocol(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@FullPort
	public EthernetProtocol Ethernet;

	/**
	 * Port for electrical power in
	 */
	@RequirementInterfaceProtocol(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@FullPort
	public ElectricalPowerProtocol electricPower;
	/**
	 * Port for heat transfer out
	 */
	@RequirementInterfaceProtocol(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@FullPort
	public ThermalHeatTransferProtocol thermalHeat;

	/**
	 * Flow of heat out
	 */
	@RequirementResource(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Flow
	public HeatWatts maxHeatOut;
	/**
	 * Flow of power in
	 */
	@RequirementResource(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Flow
	public PowerWatts maxPowerIn;

	/**
	 * Value for (minimum) system availability
	 */
	@RequirementReliability(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Value
	public Percent systemAvailability;
	/**
	 * Value for max size of the computer
	 */
	@RequirementPhysical(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Value
	public VolumeMetersCubic maximumSize;
	/**
	 * Value for max weight of the computer
	 */
	@RequirementPhysical(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Value
	public MassKilograms maximumWeight;
	/**
	 * Value for max cost of the computer
	 */
	@RequirementAttribute(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Value
	public Cost$US maximumCost;
	/**
	 * Value for host name of computer
	 */
	@RequirementAttribute(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Value
	public SString hostName;

	/**
	 * Connector to invoke function to make connection of server to UDP
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction webServerToUDPPortConnectorFunction;
	/**
	 * Function to make connection of web server to UDP
	 */
	@RequirementInterfaceProtocolConnector
	@AssociationConnector
	public SysMLAssociationBlockConnector webServerToUDPPortConnector;
	/**
	 * Function to make connection of UDP to IP
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction udpToIPPortConnectorFunction;
	/**
	 * Connector to invoke function to make connection of UDP to IP
	 */
	@RequirementInterfaceProtocolConnector
	@AssociationConnector
	public SysMLAssociationBlockConnector udpToIPPortConnector;
	/**
	 * Function to make connection of IP to ethernet
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction ipToEthernetPortConnectorFunction;
	/**
	 * Connector to invoke function to make connection of IP to ethernet
	 */
	@RequirementInterfaceProtocolConnector
	@AssociationConnector
	public SysMLAssociationBlockConnector ipToEthernetPortConnector;

	/**
	 * Constructor
	 */
	public DeployedComputer()
	{
		super();
	}

	@Override
	public void start()
	{
		Ethernet.start();
		DeployedWebServer.start();
	}

	@Override
	public void stop()
	{
		DeployedWebServer.stop();
		Ethernet.stop();
	}

	@Override
	protected void createValues()
	{
		systemAvailability = new Percent(99.99);
		maximumSize = new VolumeMetersCubic(0.1);
		maximumWeight = new MassKilograms(9.5);
		maximumCost = new Cost$US(10_000);
		hostName = new SString("hostName");  //Set after construction by domain
	}

	@Override
	protected void createFlows()
	{
		maxPowerIn = new PowerWatts(500);
		maxHeatOut = new HeatWatts(500);
	}

	@Override
	protected void createFullPorts()
	{
		DeployedWebServer = new WebServer(this, 0L);
		UDP = new UDP(this, 0L);
		IP = new IP(this, 0L);
		Ethernet = new EthernetProtocol(this, 0L);

		electricPower = new ElectricalPowerProtocol(this, 0L);
		thermalHeat = new ThermalHeatTransferProtocol(this, 0L);
	}

	@Override
	protected void createConnectorFunctions()
	{
		webServerToUDPPortConnectorFunction = () ->
		{
			DeployedWebServer.WebServerHTTP.addConnectedPortServer(UDP);
			UDP.addConnectedPortClient(DeployedWebServer.WebServerHTTP);
		};
		udpToIPPortConnectorFunction = () ->
		{
			UDP.addConnectedPortServer(IP);
			IP.addConnectedPortClient(UDP);
		};
		ipToEthernetPortConnectorFunction = () ->
		{
			IP.addConnectedPortServer(Ethernet);
			Ethernet.addConnectedPortClient(IP);
		};
	}

	@Override
	protected void createConnectors()
	{
		webServerToUDPPortConnector = new SysMLAssociationBlockConnector(DeployedWebServer, UDP, webServerToUDPPortConnectorFunction);
		udpToIPPortConnector = new SysMLAssociationBlockConnector(UDP, IP, udpToIPPortConnectorFunction);
		ipToEthernetPortConnector = new SysMLAssociationBlockConnector(IP, Ethernet, ipToEthernetPortConnectorFunction);
	}
}
