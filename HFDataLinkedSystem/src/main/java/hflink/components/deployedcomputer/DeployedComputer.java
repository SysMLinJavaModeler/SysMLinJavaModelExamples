package hflink.components.deployedcomputer;

import hflink.common.ports.ElectricalPowerProtocol;
import hflink.common.ports.EthernetProtocol;
import hflink.common.ports.IP;
import hflink.common.ports.ThermalHeatTransferProtocol;
import hflink.common.ports.UDP;
import sysmlinjava.attributetypes.Cost$US;
import sysmlinjava.attributetypes.HeatWatts;
import sysmlinjava.attributetypes.MassKilograms;
import sysmlinjava.attributetypes.Percent;
import sysmlinjava.attributetypes.PowerWatts;
import sysmlinjava.attributetypes.SString;
import sysmlinjava.attributetypes.VolumeMetersCubic;
import sysmlinjava.connectors.SysMLFlowConnector;
import sysmlinjava.connectors.SysMLFlowConnector.TypesEnum;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.connectors.FlowConnector;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.javaannotations.requirements.RequirementAttribute;
import sysmlinjava.javaannotations.requirements.RequirementInterfaceProtocol;
import sysmlinjava.javaannotations.requirements.RequirementInterfaceProtocolConnector;
import sysmlinjava.javaannotations.requirements.RequirementPhysical;
import sysmlinjava.javaannotations.requirements.RequirementReliability;
import sysmlinjava.javaannotations.requirements.RequirementResource;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.requirements.SysMLVerificationMethodKind;

/**
 * The DeployedComputer is a system component that hosts the web-server-based
 * application for the command/control of a remote system. It receives HTTP
 * requests and transmits HTTP responses over internet protocols via the
 * ModemRadio with the web-browser hosted by a CommandControlComputer.
 * <p>
 * The DeployedComputer includes flow ports for each of the protocols used
 * to communicate with the command/control computer via the modem-radio. These
 * protocols include UDP over IP over ethernet for command/control computer
 * communications, and a web-server for controlled system interactions. It also
 * includes ports for power and heat and their corresponding flows -
 * power-in and heat-out. Specified attributes include availability, size,
 * weight, speed, and cost.
 * <p>
 * The part also contains all of the connectors between the ports in the part.
 * These include the connectors between the ports that represent the
 * protocol stacks of the external interfaces. Note the connectors between
 * DeployedComputer protocols and external systems are specified in the
 * CommandControlSystem and/or the HFLinkDomain parts.
 * 
 * @author ModelerOne
 *
 */
public class DeployedComputer extends SysMLPart
{
	/**
	 * Port for the web server hosted by the computer and used by the computer's
	 * services to receive controls and send monitors via HTTP over UDP to the C2
	 * computer
	 */
	@RequirementInterfaceProtocol(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Port
	public WebServer DeployedWebServer;
	/**
	 * Port for the UDP used by the web server to communicate with the web browser
	 * on the C2 computer
	 */
	@RequirementInterfaceProtocol(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Port
	public UDP UDP;
	/**
	 * Port for IP used by UDP to communicate with UDP ports on the C2 computer
	 */
	@RequirementInterfaceProtocol(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Port
	public IP IP;
	/**
	 * Port for ethernet used by IP to communicate via router with IP on the C2
	 * computer
	 */
	@RequirementInterfaceProtocol(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Port
	public EthernetProtocol Ethernet;

	/**
	 * Port for electrical power in
	 */
	@RequirementInterfaceProtocol(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Port
	public ElectricalPowerProtocol electricPower;
	/**
	 * Port for heat transfer out
	 */
	@RequirementInterfaceProtocol(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Port
	public ThermalHeatTransferProtocol thermalHeat;

	/**
	 * Flow of heat out
	 */
	@RequirementResource(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Attribute
	public HeatWatts maxHeatOut;
	/**
	 * Flow of power in
	 */
	@RequirementResource(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Attribute
	public PowerWatts maxPowerIn;

	/**
	 * Value for (minimum) system availability
	 */
	@RequirementReliability(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Attribute
	public Percent systemAvailability;
	/**
	 * Value for max size of the computer
	 */
	@RequirementPhysical(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Attribute
	public VolumeMetersCubic maximumSize;
	/**
	 * Value for max weight of the computer
	 */
	@RequirementPhysical(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Attribute
	public MassKilograms maximumWeight;
	/**
	 * Value for max cost of the computer
	 */
	@RequirementAttribute(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Attribute
	public Cost$US maximumCost;
	/**
	 * Value for host name of computer
	 */
	@RequirementAttribute(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Attribute
	public SString hostName;

	/**
	 * Connector of web server HTTP to UDP
	 */
	@RequirementInterfaceProtocolConnector
	@FlowConnector
	public SysMLFlowConnector webServerToUDPConnector;
	/**
	 * Connector of UDP to IP
	 */
	@RequirementInterfaceProtocolConnector
	@FlowConnector
	public SysMLFlowConnector udpToIPConnector;
	/**
	 * Connector of IP to ethernet
	 */
	@RequirementInterfaceProtocolConnector
	@FlowConnector
	public SysMLFlowConnector ipToEthernetConnector;
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
	protected void createAttributes()
	{
		systemAvailability = new Percent(99.99);
		maximumSize = new VolumeMetersCubic(0.1);
		maximumWeight = new MassKilograms(9.5);
		maximumCost = new Cost$US(10_000);
		hostName = new SString("hostName");  //Set after construction by domain
		maxPowerIn = new PowerWatts(500);
		maxHeatOut = new HeatWatts(500);
	}

	@Override
	protected void createPorts()
	{
		DeployedWebServer = new WebServer(this, 0L);
		UDP = new UDP(this, 0L);
		IP = new IP(this, 0L);
		Ethernet = new EthernetProtocol(this, 0L);

		electricPower = new ElectricalPowerProtocol(this, 0L);
		thermalHeat = new ThermalHeatTransferProtocol(this, 0L);
	}

	@Override
	protected void createFlowConnectors()
	{
		webServerToUDPConnector = new SysMLFlowConnector(TypesEnum.clienttoserver, true, DeployedWebServer, UDP, "", 0L);
		udpToIPConnector = new SysMLFlowConnector(TypesEnum.clienttoserver, true, UDP, IP, "", 0L);
		ipToEthernetConnector = new SysMLFlowConnector(TypesEnum.clienttoserver, true, IP, Ethernet, "", 0L);
	}
}
