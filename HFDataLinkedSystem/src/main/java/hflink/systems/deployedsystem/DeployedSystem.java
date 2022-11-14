package hflink.systems.deployedsystem;

import hflink.components.deployedcomputer.DeployedComputer;
import hflink.components.modemradio.ModemRadio;
import hflink.components.switchrouter.EthernetSwitchIPRouter;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.AssociationConnector;
import sysmlinjava.annotations.AssociationConnectorFunction;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.requirements.RequirementComponent;
import sysmlinjava.annotations.requirements.RequirementInterface;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.connectors.SysMLAssociationBlockConnector;
import sysmlinjava.connectors.SysMLAssociationBlockConnectorFunction;
import sysmlinjava.requirements.SysMLVerificationMethodKind;

/**
 * The {@code DeployedSystem} is the SysMLinJava model of the HFLink system that
 * is deployed to a remote location for the command/control of a remote system.
 * It interacts with the remote system to control and monitor its operation.
 * 
 * The {@code DeployedSystem} consists of three components, each of which is
 * modeled as a SysML part in the system block. These components include a
 * {@code DeployedComputer} to host the web-server and other services that
 * interact with the remotely controlled system, {@code a ModemRadio} to
 * transmit and recieve the monitor/control data via long-range
 * {@code TDMA}-over-{@code HF} communications, and an {@code
 * EthernetSwitchIPRouter} to provide communications between the computer and
 * the modem-radio.
 * 
 * The block also contains each of the connectors between the system parts.
 * These connectors are simply the computer to switch/router connector and the
 * modem-radio to switch/router connector. The connectors between this and other
 * systems are specified in the {@code HFLinkDomain} block.
 * 
 * @author ModelerOne
 *
 */
public class DeployedSystem extends SysMLBlock
{
	/**
	 * Part for the computer
	 */
	@RequirementComponent(requirementVerificationMethod = {SysMLVerificationMethodKind.Inspection, SysMLVerificationMethodKind.Demonstration})
	@Part
	public DeployedComputer DeployedComputer;
	/**
	 * Part for the switch/router
	 */
	@RequirementComponent(requirementVerificationMethod = {SysMLVerificationMethodKind.Inspection, SysMLVerificationMethodKind.Demonstration})
	@Part
	public EthernetSwitchIPRouter DeployedSwitchRouter;
	/**
	 * Part for the modem-radio
	 */
	@RequirementComponent(requirementVerificationMethod = {SysMLVerificationMethodKind.Inspection, SysMLVerificationMethodKind.Demonstration})
	@Part
	public ModemRadio DeployedModemRadio;

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
	public DeployedSystem(String name)
	{
		super(name, 0L);
	}

	@Operation
	@Override
	public void start()
	{
		DeployedComputer.start();
		DeployedSwitchRouter.start();
		DeployedModemRadio.start();
	}

	@Operation
	@Override
	public void stop()
	{
		DeployedComputer.stop();
		DeployedSwitchRouter.stop();
		DeployedModemRadio.stop();
	}

	@Override
	protected void createParts()
	{
		DeployedComputer = new DeployedComputer();
		DeployedSwitchRouter = new EthernetSwitchIPRouter();
		DeployedModemRadio = new ModemRadio(name.get() + "ModemRadio");
	}

	@Override
	protected void createConnectorFunctions()
	{
		computerToSwitchRouterEthernetConnectorFunction = () ->
		{
			DeployedComputer.Ethernet.addConnectedPortPeer(DeployedSwitchRouter.ethernet1);
			DeployedSwitchRouter.ethernet1.addConnectedPortPeer(DeployedComputer.Ethernet);
		};
		modemRadioToSwitchRouterEthernetConnectorFunction = () ->
		{
			DeployedModemRadio.ethernet.addConnectedPortPeer(DeployedSwitchRouter.ethernet0);
			DeployedSwitchRouter.ethernet0.addConnectedPortPeer(DeployedModemRadio.ethernet);
		};
		computerToSwitchRouterConnectorFunction = () ->
		{
			DeployedComputer.IP.addVirtualConnectedPortPeer(DeployedSwitchRouter.ip);
			DeployedSwitchRouter.ip.addVirtualConnectedPortPeer(DeployedComputer.IP);
		};
		modemRadioToSwitchRouterConnectorFunction = () ->
		{
			DeployedModemRadio.ip.addVirtualConnectedPortPeer(DeployedSwitchRouter.ip);
			DeployedSwitchRouter.ip.addVirtualConnectedPortPeer(DeployedModemRadio.ip);
		};
	}

	@Override
	protected void createConnectors()
	{
		computerToSwitchRouterEthernetConnector = new SysMLAssociationBlockConnector(DeployedComputer, DeployedSwitchRouter, computerToSwitchRouterEthernetConnectorFunction);
		modemRadioToSwitchRouterEthernetConnector = new SysMLAssociationBlockConnector(DeployedModemRadio, DeployedSwitchRouter, modemRadioToSwitchRouterEthernetConnectorFunction);
		computerToSwitchRouterConnector = new SysMLAssociationBlockConnector(DeployedComputer, DeployedSwitchRouter, computerToSwitchRouterConnectorFunction);
		modemRadioToSwitchRouterConnector = new SysMLAssociationBlockConnector(DeployedModemRadio, DeployedSwitchRouter, modemRadioToSwitchRouterConnectorFunction);
	}
}
