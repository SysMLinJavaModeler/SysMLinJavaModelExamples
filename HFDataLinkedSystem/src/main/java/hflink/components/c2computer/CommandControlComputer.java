package hflink.components.c2computer;

import hflink.common.ports.DesktopUIServerProtocol;
import hflink.common.ports.ElectricalPowerProtocol;
import hflink.common.ports.EthernetProtocol;
import hflink.common.ports.IP;
import hflink.common.ports.PCUIServerProtocol;
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
 * The CommandControlComputer is a system component that hosts the
 * web-browser-based application for the command/control of the remotely
 * deployed systems. It transmits HTTP requests and receive HTTP responses over
 * internet protocols via the ModemRadio with the web-servers hosted by
 * DeployedComputers.
 * <p>
 * The CommandControlComputer part includes flow ports for each of the
 * protocols used to communicate with the deployed computers via the
 * modem-radio. These protocols include UDP over IP over ethernet for remote
 * computer communications, and a web-browser on a PC-like desktop for operator
 * interactions.  It also includes flow ports for power and heat and their
 * corresponding flows - power-in and heat-out. Specified part attributes include
 * availability, size, weight, speed, and cost.
 * <p>
 * The part also contains all of the connectors between the ports in the block.
 * These include the connectors between the flow ports that represent the
 * protocol stacks of the external interfaces. Note the connectors between
 * {@code CommandControlComputer} protocols and external systems are specified
 * in the {@code CommandControlSystem} part and/or the {@code HFLinkDomain}
 * part.
 * 
 * @author ModelerOne
 */
public class CommandControlComputer extends SysMLPart
{
	/**
	 * Port for the web browser hosted by the computer and used by the operator to
	 * view controls and monitors and communicate via HTTP over UDP
	 */
	@RequirementInterfaceProtocol
	@Port
	public WebBrowser C2WebBrowser;
	/**
	 * Port for the UDP used by the web browser to communicate with web servers on
	 * other computers
	 */
	@RequirementInterfaceProtocol
	@Port
	public UDP UDP;
	/**
	 * Port for IP used by UDP to communicate with UDP ports on other computers
	 */
	@RequirementInterfaceProtocol
	@Port
	public IP IP;
	/**
	 * Port for ethernet used by IP to communicate via router with IP on other
	 * computers
	 */
	@RequirementInterfaceProtocol
	@Port
	public EthernetProtocol Ethernet;

	/**
	 * Port for the computer's desktop user-interface to interact with the operator
	 */
	@RequirementInterfaceProtocol
	@Port
	public DesktopUIServerProtocol desktop;
	/**
	 * Port for the computer's physical interface (monitor, keyboard, etc.) to
	 * interact with the operator
	 */
	@RequirementInterfaceProtocol
	@Port
	public PCUIServerProtocol pc;

	/**
	 * Port for electrical power in
	 */
	@RequirementInterfaceProtocol
	@Port
	public ElectricalPowerProtocol electricPower;
	/**
	 * Port for heat transfer out
	 */
	@RequirementInterfaceProtocol
	@Port
	public ThermalHeatTransferProtocol thermalHeat;

	/**
	 * Attribute of heat out
	 */
	@RequirementResource(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Attribute
	public HeatWatts maxHeatOut;
	/**
	 * Attribute of power in
	 */
	@RequirementResource(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Attribute
	public PowerWatts maxPowerIn;

	/**
	 * Attribute of (minimum) system availability
	 */
	@RequirementReliability(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Attribute
	public Percent systemAvailability;
	/**
	 * Attribute of max size of the computer
	 */
	@RequirementPhysical(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Attribute
	public VolumeMetersCubic maximumSize;
	/**
	 * Attribute of max weight of the computer
	 */
	@RequirementPhysical(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Attribute
	public MassKilograms maximumWeight;
	/**
	 * Attribute of max cost of the computer
	 */
	@RequirementAttribute(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Attribute
	public Cost$US maximumCost;

	/**
	 * Attribute of host name of computer
	 */
	@RequirementAttribute(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Attribute
	public SString hostName;

	/**
	 * Connector (2-way) of browser (HTTP) to UDP
	 */
	@RequirementInterfaceProtocolConnector
	@FlowConnector
	public SysMLFlowConnector WebBrowserToUDPConnector;
	/**
	 * Connector (2-way) of UDP to IP
	 */
	@RequirementInterfaceProtocolConnector
	@FlowConnector
	public SysMLFlowConnector UDPToIPConnector;
	/**
	 * Connector (2-way) of IP to ethernet
	 */
	@RequirementInterfaceProtocolConnector
	@FlowConnector
	public SysMLFlowConnector IPToEthernetConnector;

	/**
	 * Connector (2-way) of browser to desktop
	 */
	@RequirementInterfaceProtocolConnector
	@FlowConnector
	public SysMLFlowConnector WebBrowserToDesktopUIConnector;
	/**
	 * Connector (2-way) of desktop to physical PC
	 */
	@RequirementInterfaceProtocolConnector
	@FlowConnector
	public SysMLFlowConnector DesktopUIToPCUIConnector;

	/**
	 * Constructor
	 */
	public CommandControlComputer()
	{
		super();
	}

	@Override
	public void start()
	{
		C2WebBrowser.start();
		Ethernet.start();
	}

	@Override
	public void stop()
	{
		C2WebBrowser.stop();
		Ethernet.stop();
	}

	@Override
	protected void createAttributes()
	{
		systemAvailability = new Percent(99.99);
		maximumSize = new VolumeMetersCubic(0.1);
		maximumWeight = new MassKilograms(9.5);
		maximumCost = new Cost$US(10_000);
		hostName = new SString("hostname"); // final value set by constraint performed during creation of domain
		maxPowerIn = new PowerWatts(500);
		maxHeatOut = new HeatWatts(500);
	}

	@Override
	protected void createPorts()
	{
		C2WebBrowser = new WebBrowser(this, 0L);
		UDP = new UDP(this, 0L);
		IP = new IP(this, 0L);
		Ethernet = new EthernetProtocol(this, 0L);
		desktop = new DesktopUIServerProtocol(this, 0L);
		pc = new PCUIServerProtocol(this, 0L);

		electricPower = new ElectricalPowerProtocol(this, 0L);
		thermalHeat = new ThermalHeatTransferProtocol(this, 0L);
	}

	@Override
	protected void createFlowConnectors()
	{
		WebBrowserToDesktopUIConnector = new SysMLFlowConnector(TypesEnum.clienttoserver, true, C2WebBrowser.application, desktop, "", 0L);
		DesktopUIToPCUIConnector = new SysMLFlowConnector(TypesEnum.clienttoserver, true, desktop, pc, "", 0L);
		WebBrowserToUDPConnector = new SysMLFlowConnector(TypesEnum.clienttoserver, true, C2WebBrowser, UDP, "", 0L);
		UDPToIPConnector = new SysMLFlowConnector(TypesEnum.clienttoserver, true, UDP, IP, "", 0L);
		IPToEthernetConnector = new SysMLFlowConnector(TypesEnum.clienttoserver, true, IP, Ethernet, "", 0L);
	}
}
