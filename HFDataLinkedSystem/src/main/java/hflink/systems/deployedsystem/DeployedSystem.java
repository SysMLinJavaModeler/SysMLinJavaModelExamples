package hflink.systems.deployedsystem;

import hflink.components.deployedcomputer.DeployedComputer;
import hflink.components.modemradio.ModemRadio;
import hflink.components.switchrouter.EthernetSwitchIPRouter;
import sysmlinjava.connectors.SysMLFlowConnector;
import sysmlinjava.connectors.SysMLFlowConnector.TypesEnum;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.connectors.FlowConnector;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.javaannotations.requirements.RequirementComponent;
import sysmlinjava.javaannotations.requirements.RequirementInterface;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.requirements.SysMLVerificationMethodKind;

/**
 * The {@code DeployedSystem} is the SysMLinJava model of the HFLink system that
 * is deployed to a remote location for the command/control of a remote system.
 * It interacts with the remote system to control and monitor its operation. The
 * {@code DeployedSystem} consists of three components, each of which is modeled
 * as a SysML part in the system block. These components include a
 * {@code DeployedComputer} to host the web-server and other services that
 * interact with the remotely controlled system, {@code a ModemRadio} to
 * transmit and recieve the monitor/control data via long-range
 * {@code TDMA}-over-{@code HF} communications, and an {@code
 * EthernetSwitchIPRouter} to provide communications between the computer and
 * the modem-radio. The block also contains each of the connectors between the
 * system parts. These connectors are simply the computer to switch/router
 * connector and the modem-radio to switch/router connector. The connectors
 * between this and other systems are specified in the {@code HFLinkDomain}
 * block.
 * 
 * @author ModelerOne
 */
public class DeployedSystem extends SysMLPart
{
	/**
	 * Part for the computer
	 */
	@RequirementComponent(requirementVerificationMethod = { SysMLVerificationMethodKind.Inspection, SysMLVerificationMethodKind.Demonstration })
	@Part
	public DeployedComputer DeployedComputer;
	/**
	 * Part for the switch/router
	 */
	@RequirementComponent(requirementVerificationMethod = { SysMLVerificationMethodKind.Inspection, SysMLVerificationMethodKind.Demonstration })
	@Part
	public EthernetSwitchIPRouter DeployedSwitchRouter;
	/**
	 * Part for the modem-radio
	 */
	@RequirementComponent(requirementVerificationMethod = { SysMLVerificationMethodKind.Inspection, SysMLVerificationMethodKind.Demonstration })
	@Part
	public ModemRadio DeployedModemRadio;

	/**
	 * Connector between the computer and the switch/router.
	 */
	@FlowConnector
	public SysMLFlowConnector computerToSwitchRouterEthernet;
	/**
	 * Connector between the modem-radio and the switch/router.
	 */
	@FlowConnector
	public SysMLFlowConnector modemRadioToSwitchRouterEthernet;
	/**
	 * Connector between the computer and the switch/router. <b>Note:</b>This is a
	 * "virtual" connector for use by automated requirements generation tools for
	 * generating interface requirements.
	 */
	@RequirementInterface
	@FlowConnector
	public SysMLFlowConnector computerToSwitchRouter;
	/**
	 * Connector between the modem-radio and the switch/router. <b>Note:</b>This is
	 * a "virtual" connector function for use by automated requirements generation
	 * tools for generating interface requirements.
	 */
	@RequirementInterface
	@FlowConnector
	public SysMLFlowConnector modemRadioToSwitchRouter;

	/**
	 * Constructor
	 * 
	 * @param name unique name
	 */
	public DeployedSystem(String name)
	{
		super(name, 0L);
	}

	@Action
	@Override
	public void start()
	{
		DeployedComputer.start();
		DeployedSwitchRouter.start();
		DeployedModemRadio.start();
	}

	@Action
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
	protected void createFlowConnectors()
	{
		computerToSwitchRouterEthernet = new SysMLFlowConnector(TypesEnum.peertopeer, true, DeployedComputer.Ethernet, DeployedSwitchRouter.ethernet1, "", 0L);
		modemRadioToSwitchRouterEthernet = new SysMLFlowConnector(TypesEnum.peertopeer, true, DeployedModemRadio.ethernet, DeployedSwitchRouter.ethernet0, "", 0L);
		computerToSwitchRouter = new SysMLFlowConnector(TypesEnum.virtual, true, DeployedComputer.IP, DeployedSwitchRouter.ip, "", 0L);
		modemRadioToSwitchRouter = new SysMLFlowConnector(TypesEnum.virtual, true, DeployedModemRadio.ip, DeployedSwitchRouter.ip, "", 0L);
	}
}
