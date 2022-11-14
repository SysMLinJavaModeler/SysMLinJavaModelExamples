package hflink.components.c2computer;

import hflink.common.ports.DesktopUIServerProtocol;
import hflink.common.ports.ElectricalPowerProtocol;
import hflink.common.ports.EthernetProtocol;
import hflink.common.ports.IP;
import hflink.common.ports.PCUIServerProtocol;
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
 * The CommandControlComputer is a system component that hosts the
 * web-browser-based application for the command/control of the remotely
 * deployed systems. It transmits HTTP requests and receive HTTP responses over
 * internet protocols via the ModemRadio with the web-servers hosted by
 * DeployedComputers.
 * <p>
 * The CommandControlComputer block includes full ports for each of the
 * protocols used to communicate with the deployed computers via the
 * modem-radio. These protocols include UDP over IP over ethernet for remote
 * computer communications, and a web-browser on a PC-like desktop for operator
 * interactions.It also includes full ports for power and heat and their
 * corresponding flows - power-in and heat-out. Specified block values include
 * availability, size, weight, speed, and cost.
 * <p>
 * The block also contains all of the connectors between the ports in the block.
 * These include the connectors between the full ports that represent the
 * protocol stacks of the external interfaces. Note the connectors between
 * {@code CommandControlComputer} protocols and external systems are specified
 * in the {@code CommandControlSystem} block and/or the {@code HFLinkDomain}
 * block.
 * 
 * @author ModelerOne
 *
 */
public class CommandControlComputer extends SysMLBlock
{
	/**
	 * Port for the web browser hosted by the computer and used by the operator to
	 * view controls and monitors and communicate via HTTP over UDP
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public WebBrowser C2WebBrowser;
	/**
	 * Port for the UDP used by the web browser to communicate with web servers on
	 * other computers
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public UDP UDP;
	/**
	 * Port for IP used by UDP to communicate with UDP ports on other computers
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public IP IP;
	/**
	 * Port for ethernet used by IP to communicate via router with IP on other
	 * computers
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public EthernetProtocol Ethernet;

	/**
	 * Port for the computer's desktop user-interface to interact with the operator
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public DesktopUIServerProtocol desktop;
	/**
	 * Port for the computer's physical interface (monitor, keyboard, etc.) to
	 * interact with the operator
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public PCUIServerProtocol pc;

	/**
	 * Port for electrical power in
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public ElectricalPowerProtocol electricPower;
	/**
	 * Port for heat transfer out
	 */
	@RequirementInterfaceProtocol
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
	 * Function to make connection of browser to UDP
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction webBrowserToUDPPortConnectorFunction;
	/**
	 * Function to make connection of UDP to IP
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction udpToIPPortConnectorFunction;
	/**
	 * Function to make connection of IP to ethernet
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction ipToEthernetPortConnectorFunction;

	/**
	 * Connector to invoke function to make connection of browser to UDP
	 */
	@RequirementInterfaceProtocolConnector
	@AssociationConnector
	public SysMLAssociationBlockConnector WebBrowserToUDPPortConnector;
	/**
	 * Connector to invoke function to make connection of UDP to IP
	 */
	@RequirementInterfaceProtocolConnector
	@AssociationConnector
	public SysMLAssociationBlockConnector UDPToIPPortConnector;
	/**
	 * Connector to invoke function to make connection of IP to ethernet
	 */
	@RequirementInterfaceProtocolConnector
	@AssociationConnector
	public SysMLAssociationBlockConnector IPToEthernetPortConnector;

	/**
	 * Function to make connection of browser to desktop
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction webBrowserToDesktopUIConnectorFunction;
	/**
	 * Function to make connection of desktop to physical PC
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction desktopUIToPCUIConnectorFunction;
	/**
	 * Connector to invoke function to make connection of browser to desktop
	 */
	@RequirementInterfaceProtocolConnector
	@AssociationConnector
	public SysMLAssociationBlockConnector WebBrowserToDesktopUIConnector;
	/**
	 * Connector to invoke function to make connection of desktop to physical PC
	 */
	@RequirementInterfaceProtocolConnector
	@AssociationConnector
	public SysMLAssociationBlockConnector DesktopUIToPCUIConnector;
	
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
	protected void createValues()
	{
		systemAvailability = new Percent(99.99);
		maximumSize = new VolumeMetersCubic(0.1);
		maximumWeight = new MassKilograms(9.5);
		maximumCost = new Cost$US(10_000);
		hostName = new SString("hostname"); //Set after construction by domain
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
	protected void createConnectorFunctions()
	{
		webBrowserToDesktopUIConnectorFunction = () ->
		{
			C2WebBrowser.application.addConnectedPortServer(desktop);
			desktop.addConnectedPortClient(C2WebBrowser.application);
		};
		desktopUIToPCUIConnectorFunction = () ->
		{
			desktop.addConnectedPortServer(pc);
			pc.addConnectedPortClient(desktop);
		};
		webBrowserToUDPPortConnectorFunction = () ->
		{
			C2WebBrowser.WebBrowserHTTP.addConnectedPortServer(UDP);
			UDP.addConnectedPortClient(C2WebBrowser.WebBrowserHTTP);
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
		WebBrowserToUDPPortConnector = new SysMLAssociationBlockConnector(C2WebBrowser, UDP, webBrowserToUDPPortConnectorFunction);
		UDPToIPPortConnector = new SysMLAssociationBlockConnector(UDP, IP, udpToIPPortConnectorFunction);
		IPToEthernetPortConnector = new SysMLAssociationBlockConnector(IP, Ethernet, ipToEthernetPortConnectorFunction);
		WebBrowserToDesktopUIConnector = new SysMLAssociationBlockConnector(C2WebBrowser, desktop, webBrowserToDesktopUIConnectorFunction);
		DesktopUIToPCUIConnector = new SysMLAssociationBlockConnector(desktop, pc, desktopUIToPCUIConnectorFunction);
	}
}
