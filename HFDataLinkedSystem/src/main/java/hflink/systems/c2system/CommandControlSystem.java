package hflink.systems.c2system;

import hflink.components.c2computer.CommandControlComputer;
import hflink.components.modemradio.ModemRadio;
import hflink.components.switchrouter.EthernetSwitchIPRouter;
import sysmlinjava.connectors.SysMLFlowConnector;
import sysmlinjava.connectors.SysMLFlowConnector.TypesEnum;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.connectors.FlowConnector;
import sysmlinjava.javaannotations.metadata.Rationale;
import sysmlinjava.javaannotations.metadata.StatusInfo;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.javaannotations.requirements.RequirementComponent;
import sysmlinjava.javaannotations.requirements.RequirementInterface;
import sysmlinjava.metadata.SysMLRationale;
import sysmlinjava.metadata.SysMLStatusInfo;
import sysmlinjava.metadata.SysMLStatusKind;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.requirements.SysMLRiskKind;
import sysmlinjava.requirements.SysMLVerificationMethodKind;

/**
 * The {@code CommandControlSystem} is the SysMLinJava model of the HFLink
 * system that performs command/control activities for remotely deployed
 * systems. It interacts with an operator to send controls to and receive
 * monitors from the remote systems. The {@code CommandControlSystem} consists
 * of three components, each of which is modeled as a SysML part in the system
 * part. These components include a {@code CommandControlComputer} to host the
 * web-browser application used by the operator to monitor and control the
 * remote systems, a {@code ModemRadio} to transmit and recieve the
 * monitor/control data via long-range {@code TDMA}-over-{@code HF}
 * communications, and an {@code EthernetSwitchIPRouter} to provide
 * communications between the computer and modem-radio. The part also contains
 * each of the connectors between the system parts. These connectors are simply
 * the computer to switch/router connector and the modem-radio to switch/router
 * connector. The connectors between this and other systems are specified in the
 * {@code HFLinkDomain} block.
 * 
 * @author ModelerOne
 */
public class CommandControlSystem extends SysMLPart
{
	/**
	 * Part for the computer
	 */
	@RequirementComponent(requirementVerificationMethod = { SysMLVerificationMethodKind.Inspection, SysMLVerificationMethodKind.Demonstration })
	@Part
	public CommandControlComputer C2Computer;
	/**
	 * Part for the switch/router
	 */
	@RequirementComponent(requirementVerificationMethod = { SysMLVerificationMethodKind.Inspection, SysMLVerificationMethodKind.Demonstration })
	@Part
	public EthernetSwitchIPRouter SwitchRouter;
	/**
	 * Part for the modem-radio
	 */
	@RequirementComponent(requirementVerificationMethod = { SysMLVerificationMethodKind.Inspection, SysMLVerificationMethodKind.Demonstration })
	@Part
	public ModemRadio ModemRadio;

	/**
	 * Rationale for using a single combined switch/router versus two separate
	 * components
	 */
	@Rationale
	public SysMLRationale combinedSwitchRouter;

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
	 * Status of the C2 System
	 */
	@StatusInfo
	public static SysMLStatusInfo statusInfo;

	/**
	 * Constructor
	 * 
	 * @param name unique name
	 */
	public CommandControlSystem(String name)
	{
		super(name, 0L);
	}

	@Action
	@Override
	public void start()
	{
		C2Computer.start();
		SwitchRouter.start();
		ModemRadio.start();
	}

	@Action
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
	protected void createFlowConnectors()
	{
		computerToSwitchRouterEthernet = new SysMLFlowConnector(TypesEnum.peertopeer, true, C2Computer.Ethernet, SwitchRouter.ethernet1, "", 0L);
		modemRadioToSwitchRouterEthernet = new SysMLFlowConnector(TypesEnum.peertopeer, true, ModemRadio.ethernet, SwitchRouter.ethernet0, "", 0L);
		computerToSwitchRouter = new SysMLFlowConnector(TypesEnum.virtual, true, C2Computer.IP, SwitchRouter.ip, "", 0L);
		modemRadioToSwitchRouter = new SysMLFlowConnector(TypesEnum.virtual, true, ModemRadio.ip, SwitchRouter.ip, "", 0L);
	}

	@Override
	protected void createRationales()
	{
		combinedSwitchRouter = new SysMLRationale(
		"Low LAN/WAN node counts enable consolidation of ethernet switching and IP routing functions into single smaller device which will reduce initial and ongoing costs of systems",
		"singleSmallerDevice", 0L);
	}

	@Override
	protected void createStatusInfo()
	{
		statusInfo = new SysMLStatusInfo("status OK", "Modeler One", "Modeler One", SysMLRiskKind.Low, SysMLStatusKind.done, "C2 status", 0L);
	}
}
