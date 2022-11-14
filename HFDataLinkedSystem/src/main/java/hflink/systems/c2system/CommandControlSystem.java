package hflink.systems.c2system;

import hflink.components.c2computer.CommandControlComputer;
import hflink.components.modemradio.ModemRadio;
import hflink.components.switchrouter.EthernetSwitchIPRouter;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.AssociationConnector;
import sysmlinjava.annotations.AssociationConnectorFunction;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.comments.Rationale;
import sysmlinjava.annotations.requirements.RequirementComponent;
import sysmlinjava.annotations.requirements.RequirementInterface;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLRationale;
import sysmlinjava.connectors.SysMLAssociationBlockConnector;
import sysmlinjava.connectors.SysMLAssociationBlockConnectorFunction;
import sysmlinjava.requirements.SysMLVerificationMethodKind;

/**
 * The {@code CommandControlSystem} is the SysMLinJava model of the HFLink
 * system that performs command/control activities for remotely deployed
 * systems. It interacts with an operator to send controls to and receive
 * monitors from the remote systems.
 * 
 * The {@code CommandControlSystem} consists of three components, each of which
 * is modeled as a SysML part in the system block. These components include a
 * {@code CommandControlComputer} to host the web-browser application used by
 * the operator to monitor and control the remote systems, a {@code ModemRadio}
 * to transmit and recieve the monitor/control data via long-range
 * {@code TDMA}-over-{@code HF} communications, and an
 * {@code EthernetSwitchIPRouter} to provide communications between the computer
 * and modem-radio.
 * 
 * The block also contains each of the connectors between the system parts.
 * These connectors are simply the computer to switch/router connector and the
 * modem-radio to switch/router connector. The connectors between this and other
 * systems are specified in the {@code HFLinkDomain} block.
 * 
 * @author ModelerOne
 *
 */
public class CommandControlSystem extends SysMLBlock
{
	/**
	 * Part for the computer
	 */
	@RequirementComponent(requirementVerificationMethod = {SysMLVerificationMethodKind.Inspection, SysMLVerificationMethodKind.Demonstration})
	@Part
	public CommandControlComputer C2Computer;
	/**
	 * Part for the switch/router
	 */
	@RequirementComponent(requirementVerificationMethod = {SysMLVerificationMethodKind.Inspection, SysMLVerificationMethodKind.Demonstration})
	@Part
	public EthernetSwitchIPRouter SwitchRouter;
	/**
	 * Part for the modem-radio
	 */
	@RequirementComponent(requirementVerificationMethod = {SysMLVerificationMethodKind.Inspection, SysMLVerificationMethodKind.Demonstration})
	@Part
	public ModemRadio ModemRadio;

	/**
	 * Rationale for using a single combined switch/router versus two separate
	 * components
	 */
	@Rationale
	public SysMLRationale combinedSwitchRouter;

	/**
	 * Connector function that makes the ethernet connection between the computer
	 * and the switch/router.
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction computerToSwitchRouterEthernetConnectorFunction;
	/**
	 * Connector function that makes the ethernet connection between the modem-radio
	 * and the switch/router.
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction modemRadioToSwitchRouterEthernetConnectorFunction;
	/**
	 * Connector function that makes the "virtual" ethernet connection between the
	 * computer and the switch/router.
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction computerToSwitchRouterConnectorFunction;
	/**
	 * Connector function that makes the "virtual" connection between the
	 * modem-radio and the switch/router. <b>Note:</b>This is a "virtual" connector
	 * function for use by automated requirements generation tools for generating
	 * interface requirements.
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction modemRadioToSwitchRouterConnectorFunction;

	/**
	 * Connector that invokes the connector function that makes the ethernet
	 * connection between the computer and the switch/router.
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector computerToSwitchRouterEthernetConnector;
	/**
	 * Connector that invokes the connector function that makes the ethernet
	 * connection between the modem-radio and the switch/router.
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector modemRadioToSwitchRouterEthernetConnector;
	/**
	 * Connector that invokes the connector function that makes the "virtual"
	 * connection between the computer and the switch/router. <b>Note:</b>This is a
	 * "virtual" connector function for use by automated requirements generation
	 * tools for generating interface requirements.
	 */
	@RequirementInterface
	@AssociationConnector
	public SysMLAssociationBlockConnector computerToSwitchRouterConnector;
	/**
	 * Connector that invokes the connector function that makes the "virtual"
	 * connection between the modem-radio and the switch/router. <b>Note:</b>This is
	 * a "virtual" connector function for use by automated requirements generation
	 * tools for generating interface requirements.
	 */
	@RequirementInterface
	@AssociationConnector
	public SysMLAssociationBlockConnector modemRadioToSwitchRouterConnector;

	/**
	 * Constructor
	 * 
	 * @param name unique name
	 */
	public CommandControlSystem(String name)
	{
		super(name, 0L);
	}

	@Operation
	@Override
	public void start()
	{
		C2Computer.start();
		SwitchRouter.start();
		ModemRadio.start();
	}

	@Operation
	@Override
	public void stop()
	{
		C2Computer.stop();
		SwitchRouter.stop();
		ModemRadio.stop();
	}

	@Override
	protected void createParts()
	{
		C2Computer = new CommandControlComputer();
		SwitchRouter = new EthernetSwitchIPRouter();
		ModemRadio = new ModemRadio(name.get() + "ModemRadio");
	}

	@Override
	protected void createConnectorFunctions()
	{
		computerToSwitchRouterEthernetConnectorFunction = () ->
		{
			C2Computer.Ethernet.addConnectedPortPeer(SwitchRouter.ethernet1);
			SwitchRouter.ethernet1.addConnectedPortPeer(C2Computer.Ethernet);
		};
		modemRadioToSwitchRouterEthernetConnectorFunction = () ->
		{
			ModemRadio.ethernet.addConnectedPortPeer(SwitchRouter.ethernet0);
			SwitchRouter.ethernet0.addConnectedPortPeer(ModemRadio.ethernet);
		};
		computerToSwitchRouterConnectorFunction = () ->
		{
			C2Computer.IP.addVirtualConnectedPortPeer(SwitchRouter.ip);
			SwitchRouter.ip.addVirtualConnectedPortPeer(C2Computer.IP);
		};
		modemRadioToSwitchRouterConnectorFunction = () ->
		{
			ModemRadio.ip.addVirtualConnectedPortPeer(SwitchRouter.ip);
			SwitchRouter.ip.addVirtualConnectedPortPeer(ModemRadio.ip);
		};
	}

	@Override
	protected void createConnectors()
	{
		computerToSwitchRouterEthernetConnector = new SysMLAssociationBlockConnector(C2Computer, SwitchRouter, computerToSwitchRouterEthernetConnectorFunction);
		modemRadioToSwitchRouterEthernetConnector = new SysMLAssociationBlockConnector(ModemRadio, SwitchRouter, modemRadioToSwitchRouterEthernetConnectorFunction);
		computerToSwitchRouterConnector = new SysMLAssociationBlockConnector(C2Computer, SwitchRouter, computerToSwitchRouterConnectorFunction);
		modemRadioToSwitchRouterConnector = new SysMLAssociationBlockConnector(ModemRadio, SwitchRouter, modemRadioToSwitchRouterConnectorFunction);
	}

	@Override
	protected void createRationales()
	{
		combinedSwitchRouter = new SysMLRationale("Low LAN/WAN node counts enable consolidation of ethernet switching and IP routing functions into single smaller device which will reduce initial and ongoing costs of systems");
	}
}
