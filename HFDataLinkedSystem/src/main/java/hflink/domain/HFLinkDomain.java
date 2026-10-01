package hflink.domain;

import java.util.List;
import java.util.Optional;

import hflink.analysis.ParamContextsEnum;
import hflink.analysis.TDMAWaitTimeFrequencyAnalysisCase;
import hflink.common.items.DNS;
import hflink.systems.c2system.CommandControlSystem;
import hflink.systems.deployedsystem.DeployedSystem;
import hflink.systems.gps.GPS;
import sysmlinjava.annotations.SysMLDocumentation;
import sysmlinjava.attributetypes.IInteger;
import sysmlinjava.attributetypes.SString;
import sysmlinjava.connectors.SysMLBindingConnector;
import sysmlinjava.connectors.SysMLFlowConnector;
import sysmlinjava.connectors.SysMLFlowConnector.TypesEnum;
import sysmlinjava.constraint.BasicConstraintFunction;
import sysmlinjava.constraint.SysMLConstraint;
import sysmlinjava.constraint.SysMLConstraintFunction;
import sysmlinjava.javaannotations.analysis.parametrics.ParametricAnalysis;
import sysmlinjava.javaannotations.connectors.BindingConnector;
import sysmlinjava.javaannotations.connectors.FlowConnector;
import sysmlinjava.javaannotations.constraint.Constraint;
import sysmlinjava.javaannotations.constraint.ConstraintFunction;
import sysmlinjava.javaannotations.constraint.ConstraintText;
import sysmlinjava.javaannotations.metadata.ElementFilter;
import sysmlinjava.javaannotations.metadata.Issue;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.javaannotations.requirements.RequirementConstraint;
import sysmlinjava.javaannotations.requirements.RequirementConstraintFunction;
import sysmlinjava.javaannotations.requirements.RequirementConstraintText;
import sysmlinjava.javaannotations.requirements.RequirementDesign;
import sysmlinjava.javaannotations.requirements.RequirementInterface;
import sysmlinjava.javaannotations.requirements.RequirementSystem;
import sysmlinjava.metadata.SysMLElementGroup;
import sysmlinjava.metadata.SysMLIssue;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.requirements.SysMLVerificationMethodKind;

/**
 * The {@code HFLinkDomain} is a SysMLinJava model of a HF data link-based
 * systems domain. A minimal block diagram of the domain is as follows:<br>
 * <img src="doc-files/HFDataLinkedSystemDomainModel.png" alt="PNG file not
 * available" height="450"/><br>
 * The concept of the HF data link-based systems is the command/control of
 * long-range remote systems via basic internet protocols over TDMA over HF
 * communications links. A {@code CommandControlSystem} is used by an operator
 * to communicate with remote {@code DeployedSystem}s that serve as proxies for
 * some remote system such as an oil rig, maritime vessel, weather monitor, etc.
 * The {@code CommandControl} and {@code DeployedSystem}s each have:
 * <ul>
 * <li>a computer that provides the applications and interfaces for the operator
 * or remotely-controlled system
 * <li>a modem-radio that provides the long-distance HF communications between
 * the systems
 * <li>and a switch-router that connects the computer to the modem-radio
 * </ul>
 * <p>
 * GPS is used by the modem-radios to synchronize the TDMA communications
 * between them.
 * <p>
 * The primary focus of the SysMLinJava model of the {@code HFLinkDomain} is the
 * modeling and simulation of the communications protocols that are key to the
 * correct and complete operation of the systems. The model includes all of the
 * protocol stacks in all of the components of all of the systems with each
 * protocol modeled as a {@code SysMLPort} to simulate the operations of the
 * actual standard protocol. The combination of the protocols into protocol
 * stacks provides for a more complete and precise simulation of the
 * interactions that must take place correctly for the system-of-systems to
 * operate correctly.
 * <p>
 * This {@code HFLinkDomain} block contains all of the systems that comprise the
 * domain as {@code SysMLPart} parts. The parts include the single
 * {@code CommandControlSystem}, three {@code DeployedSystem}s, and a
 * {@code GPS} system. The block also contains all of the connectors between the
 * system blocks and their component parts. It also sets all the domain-wide
 * values of the system blocks to include the hostNames of all internetworked
 * components, IP routing tables, and all component-unique names and IDs.
 * <p>
 * The {@code HFLinkDomain} is a SysMLinJava executable model. All of the
 * interface protocols simulate actual protocol operations. As such, each of the
 * systems components and their protocols operate in separate execution threads
 * as implemented by the SysMLinJava asynchronous state machine model code. All
 * of the system parts and their block state machines are started and stopped
 * for model execution via the {@code HFLinkDomain}'s start and stop operations.
 * 
 * @author ModelerOne
 */
public class HFLinkDomain extends SysMLPart
{
	/**
	 * Part representing the Command/Control System
	 */
	@RequirementSystem
	@RequirementDesign(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Part
	public CommandControlSystem C2System;
	/**
	 * Part representing the Deployed System at Site Alpha
	 */
	@RequirementSystem
	@RequirementDesign(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Part
	public DeployedSystem SiteAlphaDeployedSystem;
	/**
	 * Part representing the Deployed System at Site Bravo
	 */
	@RequirementSystem
	@RequirementDesign(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Part
	public DeployedSystem SiteBravoDeployedSystem;
	/**
	 * Part representing the Deployed System at Site Charlie
	 */
	@RequirementSystem
	@RequirementDesign(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Part
	public DeployedSystem SiteCharlieDeployedSystem;
	/**
	 * Part representing the GPS
	 */
	@Part
	public GPS GPS;

	/**
	 * Declaration of the elements in the domain that represent system requirements
	 * specifications, i.e. elements for which "the model is the requirements".
	 */
	@ElementFilter
	public SysMLElementGroup requirementSystems;

	/**
	 * Declaration of the current "problem" cited for the modeled/simulated systems,
	 * i.e. that TDMA slot times are too long, should be shortened, as evidenced by
	 * the amount of unused slot time that was exposed by the simulation.
	 */
	@Issue
	public SysMLIssue tdmaSlotTimesTooLong;

	/**
	 * Connector that connects the HF protocol of the C2 System with the HF protocol
	 * of the Alpha Deployed System.
	 */
	@FlowConnector
	public SysMLFlowConnector c2ToSiteAlphaHFConnector;
	/**
	 * Connector that connects the HF protocol of the Alpha Deployed System with the
	 * HF protocol of the C2 System.
	 */
	@FlowConnector
	private SysMLFlowConnector siteAlphaToC2HFConnector;
	/**
	 * Connector that connects the HF protocol of the C2 System with the HF protocol
	 * of the Bravo Deployed System.
	 */
	@FlowConnector
	public SysMLFlowConnector c2ToSiteBravoHFConnector;
	/**
	 * Connector that connects the HF protocol of the Bravo Deployed System with the
	 * HF protocol of the C2 System.
	 */
	@FlowConnector
	private SysMLFlowConnector siteBravoToC2HFConnector;
	/**
	 * Connector that connects the HF protocol of the C2 System with the HF protocol
	 * of the Charlie Deployed System.
	 */
	@FlowConnector
	public SysMLFlowConnector c2ToSiteCharlieHFConnector;
	/**
	 * Connector that connects the HF protocol of the Charlie Deployed System with
	 * the HF protocol of the C2 System.
	 */
	@FlowConnector
	private SysMLFlowConnector siteCharlieToC2HFConnector;
	/**
	 * Connector that connects the protocol of the GPS System with the protocol of
	 * the C2 System.
	 */
	@FlowConnector
	public SysMLFlowConnector gpsToC2MessagingConnector;
	/**
	 * Connector that connects the protocol of the GPS System with the protocol of
	 * the Alpha Deployed System.
	 */
	@FlowConnector
	public SysMLFlowConnector gpsToSiteAlphaMessagingConnector;
	/**
	 * Connector that connects the protocol of the GPS System with the protocol of
	 * the Bravo Deployed System.
	 */
	@FlowConnector
	public SysMLFlowConnector gpsToSiteBravoMessagingConnector;
	/**
	 * Connector that connects the protocol of the GPS System with the protocol of
	 * the Charlie Deployed System.
	 */
	@FlowConnector
	public SysMLFlowConnector gpsToSiteCharlieMessagingConnector;

	/**
	 * Connector between the c2Computer and the site Alpha computer. Note this is a
	 * "virtual" connector used to identify an interface requirement.
	 */
	@RequirementInterface
	@RequirementDesign(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@FlowConnector
	public SysMLFlowConnector C2SystemToSiteAlphaSystemInterface;
	/**
	 * Connector between the c2Computer and the site Bravo computer. Note this is a
	 * "virtual" connector used to identify an interface requirement.
	 */
	@RequirementInterface
	@RequirementDesign(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@FlowConnector
	public SysMLFlowConnector C2SystemToSiteBravoSystemInterface;
	/**
	 * Connector between the c2Computer and the site Charlie computer. Note this is
	 * a "virtual" connector used to identify an interface requirement.
	 */
	@RequirementInterface
	@RequirementDesign(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@FlowConnector
	public SysMLFlowConnector C2SystemToSiteCharlieSystemInterface;

	/**
	 * Connector between the {@code GPS} and the {@code C2System}. Note this is a
	 * "virtual" connector used to identify an interface requirement.
	 */
	@RequirementInterface
	@FlowConnector
	public SysMLFlowConnector GPSToC2SystemInterface;
	/**
	 * Connector between the {@code GPS} and the {@code SiteAlphaSystem}. Note this
	 * is a "virtual" connector used to identify an interface requirement.
	 */
	@RequirementInterface
	@FlowConnector
	public SysMLFlowConnector GPSToSiteAlphaSystemInterface;
	/**
	 * Connector between the {@code GPS} and the {@code SiteBravoSystem}. Note this
	 * is a "virtual" connector used to identify an interface requirement.
	 */
	@RequirementInterface
	@FlowConnector
	public SysMLFlowConnector GPSToSiteBravoSystemInterface;
	/**
	 * Connector between the {@code GPS} and the {@code SiteCharlieSystem}. Note
	 * this is a "virtual" connector used to identify an interface requirement.
	 */
	@RequirementInterface
	@FlowConnector
	public SysMLFlowConnector GPSToSiteCharlieSystemInterface;
	/**
	 * Constraint block used to calculate the frequency of TDMA wait times
	 * experienced by the systems, i.e. a proxy for protocol stack efficiency.
	 */
	@ParametricAnalysis
	public TDMAWaitTimeFrequencyAnalysisCase tdmaWaitTimeFrequencies;

	/**
	 * Binding connector that invokes the connector function that binds the TDMA
	 * wait times for the C2 system's modem-radio to the associated attribute in the
	 * analysis case.
	 */
	@BindingConnector
	public SysMLBindingConnector c2SystemTDMAWaitTimeBindingConnnector;

	/**
	 * Binding connector that invokes the connector function that binds the TDMA
	 * wait times for the Alpha Deployed system's modem-radio to the associated
	 * attribute in the analysis case.
	 */
	@BindingConnector
	public SysMLBindingConnector alphaDeployedSystemTDMAWaitTimeBindingConnnector;

	/**
	 * Binding connector that invokes the connector function that binds the TDMA
	 * wait times for the Bravo Deployed system's modem-radio to the associated
	 * attribute in the analysis case.
	 */
	@BindingConnector
	public SysMLBindingConnector bravoDeployedSystemTDMAWaitTimeBindingConnnector;

	/**
	 * Binding connector that invokes the connector function that binds the TDMA
	 * wait times for the Charlie Deployed system's modem-radio to the associated
	 * attribute in the analysis case.
	 */
	@BindingConnector
	public SysMLBindingConnector charlieDeployedSystemTDMAWaitTimeBindingConnnector;

	/**
	 * Specifies (as function) the unique names of each of the systems components.
	 * These are names in the context of the model vs. hostNames which are in the
	 * context of the network simulation.
	 */
	@RequirementConstraintFunction
	@ConstraintFunction
	public SysMLConstraintFunction componentNamesConstraintFunction;

	/**
	 * Specifies (as text) the unique names of each of the systems components. These
	 * are names in the context of the model vs. hostNames which are in the context
	 * of the network simulation.
	 */
	@RequirementConstraintText
	@ConstraintText
	public SysMLDocumentation componentNamesConstraintText;

	/**
	 * Specifies the unique names of each of the systems components. These are names
	 * in the context of the model vs. hostNames which are in the context of the
	 * network simulation.
	 */
	@RequirementConstraint
	@Constraint
	public SysMLConstraint componentNamesConstraint;

	/**
	 * Specifies ()as function the unique IDs for each of the systems components.
	 * Unique IDs are simply the unique (pseudo) IP address.
	 */
	@RequirementConstraint
	@ConstraintFunction
	public SysMLConstraintFunction componentIdentifiersConstraintFunction;

	/**
	 * Specifies (as tezxt) the unique IDs for each of the systems components.
	 * Unique IDs are simply the unique (pseudo) IP address.
	 */
	@RequirementConstraintText
	@ConstraintText
	public SysMLDocumentation componentIdentifiersConstraintText;

	/**
	 * Specifies the unique IDs for each of the systems components. Unique IDs are
	 * simply the unique (pseudo) IP address.
	 */
	@RequirementConstraint
	@Constraint
	public SysMLConstraint componentIdentifiersConstraint;

	/**
	 * Specifies(as function) the (pseudo) IP addresses of the systems computers and
	 * builds the routing tables (maps) for the network
	 */
	@RequirementConstraintFunction
	@ConstraintFunction
	public SysMLConstraintFunction ipRoutesConstraintFunction;

	/**
	 * Specifies(as text) the (pseudo) IP addresses of the systems computers and
	 * builds the routing tables (maps) for the network
	 */
	@RequirementConstraintText
	@ConstraintText
	public SysMLDocumentation ipRoutesConstraintText;

	/**
	 * Specifies the (pseudo) IP addresses of the systems computers and builds the
	 * routing tables (maps) for the network
	 */
	@RequirementConstraint
	@Constraint()
	public SysMLConstraint ipRoutesConstraint;

	/**
	 * Specifies (as function) the hostNames for each of the systems computers on
	 * the network
	 */
	@RequirementConstraintFunction
	@ConstraintFunction
	public SysMLConstraintFunction hostNamesConstraintFunction;

	/**
	 * Specifies (as text) the hostNames for each of the systems computers on the
	 * network
	 */
	@RequirementConstraintText
	@ConstraintText
	public SysMLDocumentation hostNamesConstraintText;

	/**
	 * Specifies the hostNames for each of the systems computers on the network
	 */
	@RequirementConstraint
	@Constraint()
	public SysMLConstraint hostNamesConstraint;

	/**
	 * Constructor
	 */
	public HFLinkDomain()
	{
		super();
		boolean constrained =
		   ((BasicConstraintFunction) hostNamesConstraint.function.get()).satisfied() &&
			((BasicConstraintFunction) ipRoutesConstraint.function.get()).satisfied() &&
			((BasicConstraintFunction) componentIdentifiersConstraint.function.get()).satisfied() &&
			((BasicConstraintFunction) componentNamesConstraint.function.get()).satisfied();
		if (!constrained)
			logger.warning("domain configuration failed");
	}

	/**
	 * Starts all of the systems, i.e. starts the executable model
	 */
	@Override
	public void start()
	{
		SiteAlphaDeployedSystem.start();
		SiteBravoDeployedSystem.start();
		SiteCharlieDeployedSystem.start();
		C2System.start();
		GPS.start();
		tdmaWaitTimeFrequencies.start();
	}

	/**
	 * Stops the systems, i.e. stops the executable model
	 */
	@Override
	public void stop()
	{
		tdmaWaitTimeFrequencies.stop();
		GPS.stop();
		C2System.stop();
		SiteAlphaDeployedSystem.stop();
		SiteBravoDeployedSystem.stop();
		SiteCharlieDeployedSystem.stop();
	}

	@Override
	protected void createParts()
	{
		SiteAlphaDeployedSystem = new DeployedSystem("SiteAlphaSystem");
		SiteBravoDeployedSystem = new DeployedSystem("SiteBravoSystem");
		SiteCharlieDeployedSystem = new DeployedSystem("SiteCharlieSystem");
		C2System = new CommandControlSystem("C2System");
		GPS = new GPS();
	}

	@Override
	protected void createConstraintFunctions()
	{
		hostNamesConstraintFunction = (BasicConstraintFunction) () ->
		{
			C2System.C2Computer.hostName = new SString("www.hflinkedc2.com");
			SiteAlphaDeployedSystem.DeployedComputer.hostName = new SString("www.hflinkeddep1.com");
			SiteBravoDeployedSystem.DeployedComputer.hostName = new SString("www.hflinkeddep2.com");
			SiteCharlieDeployedSystem.DeployedComputer.hostName = new SString("www.hflinkeddep3.com");
			return true;
		};
		ipRoutesConstraintFunction = (BasicConstraintFunction) () ->
		{
			IInteger c2ComputerIPAddress = IInteger.of(DNS.ipAddressFor(C2System.C2Computer.hostName.value));
			IInteger siteAlphaComputerIPAddress = IInteger.of(DNS.ipAddressFor(SiteAlphaDeployedSystem.DeployedComputer.hostName.value));
			IInteger siteBravoComputerIPAddress = IInteger.of(DNS.ipAddressFor(SiteBravoDeployedSystem.DeployedComputer.hostName.value));
			IInteger siteCharlieComputerIPAddress = IInteger.of(DNS.ipAddressFor(SiteCharlieDeployedSystem.DeployedComputer.hostName.value));

			C2System.ModemRadio.setIPAddress(c2ComputerIPAddress);
			C2System.SwitchRouter.ipToEthernetMap.put((int) c2ComputerIPAddress.value, 1);
			C2System.SwitchRouter.ipToEthernetMap.put((int) siteAlphaComputerIPAddress.value, 0);
			C2System.SwitchRouter.ipToEthernetMap.put((int) siteBravoComputerIPAddress.value, 0);
			C2System.SwitchRouter.ipToEthernetMap.put((int) siteCharlieComputerIPAddress.value, 0);

			SiteAlphaDeployedSystem.DeployedModemRadio.setIPAddress(siteAlphaComputerIPAddress);
			SiteAlphaDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int) c2ComputerIPAddress.value, 0);
			SiteAlphaDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int) siteAlphaComputerIPAddress.value, 1);
			SiteAlphaDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int) siteBravoComputerIPAddress.value, 0);
			SiteAlphaDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int) siteCharlieComputerIPAddress.value, 0);

			SiteBravoDeployedSystem.DeployedModemRadio.setIPAddress(siteBravoComputerIPAddress);
			SiteBravoDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int) c2ComputerIPAddress.value, 0);
			SiteBravoDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int) siteAlphaComputerIPAddress.value, 0);
			SiteBravoDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int) siteBravoComputerIPAddress.value, 1);
			SiteBravoDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int) siteCharlieComputerIPAddress.value, 0);

			SiteCharlieDeployedSystem.DeployedModemRadio.setIPAddress(siteCharlieComputerIPAddress);
			SiteCharlieDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int) c2ComputerIPAddress.value, 0);
			SiteCharlieDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int) siteAlphaComputerIPAddress.value, 0);
			SiteCharlieDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int) siteBravoComputerIPAddress.value, 0);
			SiteCharlieDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int) siteCharlieComputerIPAddress.value, 1);
			return true;
		};
		componentIdentifiersConstraintFunction = (BasicConstraintFunction) () ->
		{
			IInteger c2ComputerIPAddress = IInteger.of(DNS.ipAddressFor(C2System.C2Computer.hostName.value));
			IInteger siteAlphaComputerIPAddress = IInteger.of(DNS.ipAddressFor(SiteAlphaDeployedSystem.DeployedComputer.hostName.value));
			IInteger siteBravoComputerIPAddress = IInteger.of(DNS.ipAddressFor(SiteBravoDeployedSystem.DeployedComputer.hostName.value));
			IInteger siteCharlieComputerIPAddress = IInteger.of(DNS.ipAddressFor(SiteCharlieDeployedSystem.DeployedComputer.hostName.value));

			C2System.ModemRadio.id = c2ComputerIPAddress.value;
			C2System.ModemRadio.hfIPPacketProcessor.id = c2ComputerIPAddress.value;
			C2System.ModemRadio.ethernetIPPacketProcessor.id = c2ComputerIPAddress.value;
			C2System.ModemRadio.gpsMessageProcessor.id = c2ComputerIPAddress.value;
			C2System.SwitchRouter.id = c2ComputerIPAddress.value;
			C2System.C2Computer.id = c2ComputerIPAddress.value;

			SiteAlphaDeployedSystem.DeployedModemRadio.id = siteAlphaComputerIPAddress.value;
			SiteAlphaDeployedSystem.DeployedModemRadio.hfIPPacketProcessor.id = siteAlphaComputerIPAddress.value;
			SiteAlphaDeployedSystem.DeployedModemRadio.ethernetIPPacketProcessor.id = siteAlphaComputerIPAddress.value;
			SiteAlphaDeployedSystem.DeployedModemRadio.gpsMessageProcessor.id = siteAlphaComputerIPAddress.value;
			SiteAlphaDeployedSystem.DeployedComputer.id = siteAlphaComputerIPAddress.value;
			SiteAlphaDeployedSystem.DeployedSwitchRouter.id = siteAlphaComputerIPAddress.value;

			SiteBravoDeployedSystem.DeployedModemRadio.id = siteBravoComputerIPAddress.value;
			SiteBravoDeployedSystem.DeployedModemRadio.hfIPPacketProcessor.id = siteBravoComputerIPAddress.value;
			SiteBravoDeployedSystem.DeployedModemRadio.ethernetIPPacketProcessor.id = siteBravoComputerIPAddress.value;
			SiteBravoDeployedSystem.DeployedModemRadio.gpsMessageProcessor.id = siteBravoComputerIPAddress.value;
			SiteBravoDeployedSystem.DeployedSwitchRouter.id = siteBravoComputerIPAddress.value;
			SiteBravoDeployedSystem.DeployedComputer.id = siteBravoComputerIPAddress.value;

			SiteCharlieDeployedSystem.DeployedModemRadio.id = siteCharlieComputerIPAddress.value;
			SiteCharlieDeployedSystem.DeployedModemRadio.hfIPPacketProcessor.id = siteCharlieComputerIPAddress.value;
			SiteCharlieDeployedSystem.DeployedModemRadio.ethernetIPPacketProcessor.id = siteCharlieComputerIPAddress.value;
			SiteCharlieDeployedSystem.DeployedModemRadio.gpsMessageProcessor.id = siteCharlieComputerIPAddress.value;
			SiteCharlieDeployedSystem.DeployedSwitchRouter.id = siteCharlieComputerIPAddress.value;
			SiteCharlieDeployedSystem.DeployedComputer.id = siteCharlieComputerIPAddress.value;

			GPS.id = Long.valueOf(0);
			return true;
		};

		componentNamesConstraintFunction = (BasicConstraintFunction) () ->
		{
			C2System.ModemRadio.name = Optional.of("C2ModemRadio");
			C2System.ModemRadio.hfIPPacketProcessor.name = Optional.of("C2DLP");
			C2System.ModemRadio.ethernetIPPacketProcessor.name = Optional.of("C2EPP");
			C2System.ModemRadio.gpsMessageProcessor.name = Optional.of("C2GPSMP");
			C2System.SwitchRouter.name = Optional.of("C2SwitchRouter");
			C2System.C2Computer.name = Optional.of("C2Computer");

			SiteAlphaDeployedSystem.DeployedModemRadio.name = Optional.of("AlphaModemRadio");
			SiteAlphaDeployedSystem.DeployedModemRadio.hfIPPacketProcessor.name = Optional.of("AlphaDLP");
			SiteAlphaDeployedSystem.DeployedModemRadio.ethernetIPPacketProcessor.name = Optional.of("AlphaEPP");
			SiteAlphaDeployedSystem.DeployedModemRadio.gpsMessageProcessor.name = Optional.of("AlphaGPSMP");
			SiteAlphaDeployedSystem.DeployedSwitchRouter.name = Optional.of("AlphaSwitchRouter");
			SiteAlphaDeployedSystem.DeployedComputer.name = Optional.of("AlphaComputer");

			SiteBravoDeployedSystem.DeployedModemRadio.name = Optional.of("BravoModemRadio");
			SiteBravoDeployedSystem.DeployedModemRadio.hfIPPacketProcessor.name = Optional.of("BravoDLP");
			SiteBravoDeployedSystem.DeployedModemRadio.ethernetIPPacketProcessor.name = Optional.of("BravoEPP");
			SiteBravoDeployedSystem.DeployedModemRadio.gpsMessageProcessor.name = Optional.of("BravoGPSMP");
			SiteBravoDeployedSystem.DeployedSwitchRouter.name = Optional.of("BravoSwitchRouter");
			SiteBravoDeployedSystem.DeployedComputer.name = Optional.of("BravoComputer");

			SiteCharlieDeployedSystem.DeployedModemRadio.name = Optional.of("CharlieModemRadio");
			SiteCharlieDeployedSystem.DeployedModemRadio.hfIPPacketProcessor.name = Optional.of("CharlieDLP");
			SiteCharlieDeployedSystem.DeployedModemRadio.ethernetIPPacketProcessor.name = Optional.of("CharlieEPP");
			SiteCharlieDeployedSystem.DeployedModemRadio.gpsMessageProcessor.name = Optional.of("CharlieGPSMP");
			SiteCharlieDeployedSystem.DeployedSwitchRouter.name = Optional.of("CharlieSwitchRouter");
			SiteCharlieDeployedSystem.DeployedComputer.name = Optional.of("CharlieComputer");

			GPS.name = Optional.of("GPS");
			return true;
		};
	}

	@Override
	protected void createConstraintTexts()
	{
		hostNamesConstraintText = new SysMLDocumentation("""
		C2System.C2Computer.hostName = new SString("www.hflinkedc2.com");
		SiteAlphaDeployedSystem.DeployedComputer.hostName = new SString("www.hflinkeddep1.com");
		SiteBravoDeployedSystem.DeployedComputer.hostName = new SString("www.hflinkeddep2.com");
		SiteCharlieDeployedSystem.DeployedComputer.hostName = new SString("www.hflinkeddep3.com");
		""");
		ipRoutesConstraintText = new SysMLDocumentation("""
		IInteger c2ComputerIPAddress = IInteger.of(DNS.ipAddressFor(C2System.C2Computer.hostName.value));
		IInteger siteAlphaComputerIPAddress = IInteger.of(DNS.ipAddressFor(SiteAlphaDeployedSystem.DeployedComputer.hostName.value));
		IInteger siteBravoComputerIPAddress = IInteger.of(DNS.ipAddressFor(SiteBravoDeployedSystem.DeployedComputer.hostName.value));
		IInteger siteCharlieComputerIPAddress = IInteger.of(DNS.ipAddressFor(SiteCharlieDeployedSystem.DeployedComputer.hostName.value));

		C2System.ModemRadio.setIPAddress(c2ComputerIPAddress);
		C2System.SwitchRouter.ipToEthernetMap.put((int)(int)c2ComputerIPAddress.value, 1);
		C2System.SwitchRouter.ipToEthernetMap.put((int)siteAlphaComputerIPAddress.value, 0);
		C2System.SwitchRouter.ipToEthernetMap.put((int)siteBravoComputerIPAddress.value, 0);
		C2System.SwitchRouter.ipToEthernetMap.put((int)siteCharlieComputerIPAddress.value, 0);

		SiteAlphaDeployedSystem.DeployedModemRadio.setIPAddress(siteAlphaComputerIPAddress);
		SiteAlphaDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int)c2ComputerIPAddress.value, 0);
		SiteAlphaDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int)siteAlphaComputerIPAddress.value, 1);
		SiteAlphaDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int)siteBravoComputerIPAddress.value, 0);
		SiteAlphaDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int)siteCharlieComputerIPAddress.value, 0);

		SiteBravoDeployedSystem.DeployedModemRadio.setIPAddress(siteBravoComputerIPAddress);
		SiteBravoDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int)c2ComputerIPAddress.value, 0);
		SiteBravoDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int)siteAlphaComputerIPAddress.value, 0);
		SiteBravoDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int)siteBravoComputerIPAddress.value, 1);
		SiteBravoDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int)siteCharlieComputerIPAddress.value, 0);

		SiteCharlieDeployedSystem.DeployedModemRadio.setIPAddress(siteCharlieComputerIPAddress);
		SiteCharlieDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int)c2ComputerIPAddress.value, 0);
		SiteCharlieDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int)siteAlphaComputerIPAddress.value, 0);
		SiteCharlieDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int)siteBravoComputerIPAddress.value, 0);
		SiteCharlieDeployedSystem.DeployedSwitchRouter.ipToEthernetMap.put((int)siteCharlieComputerIPAddress.value, 1);
		""");
		componentIdentifiersConstraintText = new SysMLDocumentation("""
		IInteger c2ComputerIPAddress = IInteger.of(DNS.ipAddressFor(C2System.C2Computer.hostName.value));
		IInteger siteAlphaComputerIPAddress = IInteger.of(DNS.ipAddressFor(SiteAlphaDeployedSystem.DeployedComputer.hostName.value));
		IInteger siteBravoComputerIPAddress = IInteger.of(DNS.ipAddressFor(SiteBravoDeployedSystem.DeployedComputer.hostName.value));
		IInteger siteCharlieComputerIPAddress = IInteger.of(DNS.ipAddressFor(SiteCharlieDeployedSystem.DeployedComputer.hostName.value));

		C2System.ModemRadio.id = c2ComputerIPAddress.value;
		C2System.ModemRadio.hfIPPacketProcessor.id = c2ComputerIPAddress.value;
		C2System.ModemRadio.ethernetIPPacketProcessor.id = c2ComputerIPAddress.value;
		C2System.ModemRadio.gpsMessageProcessor.id = c2ComputerIPAddress.value;
		C2System.SwitchRouter.id = c2ComputerIPAddress.value;
		C2System.C2Computer.id = c2ComputerIPAddress.value;

		SiteAlphaDeployedSystem.DeployedModemRadio.id = siteAlphaComputerIPAddress.value;
		SiteAlphaDeployedSystem.DeployedModemRadio.hfIPPacketProcessor.id = siteAlphaComputerIPAddress.value;
		SiteAlphaDeployedSystem.DeployedModemRadio.ethernetIPPacketProcessor.id = siteAlphaComputerIPAddress.value;
		SiteAlphaDeployedSystem.DeployedModemRadio.gpsMessageProcessor.id = siteAlphaComputerIPAddress.value;
		SiteAlphaDeployedSystem.DeployedComputer.id = siteAlphaComputerIPAddress.value;
		SiteAlphaDeployedSystem.DeployedSwitchRouter.id = siteAlphaComputerIPAddress.value;

		SiteBravoDeployedSystem.DeployedModemRadio.id = siteBravoComputerIPAddress.value;
		SiteBravoDeployedSystem.DeployedModemRadio.hfIPPacketProcessor.id = siteBravoComputerIPAddress.value;
		SiteBravoDeployedSystem.DeployedModemRadio.ethernetIPPacketProcessor.id = siteBravoComputerIPAddress.value;
		SiteBravoDeployedSystem.DeployedModemRadio.gpsMessageProcessor.id = siteBravoComputerIPAddress.value;
		SiteBravoDeployedSystem.DeployedSwitchRouter.id = siteBravoComputerIPAddress.value;
		SiteBravoDeployedSystem.DeployedComputer.id = siteBravoComputerIPAddress.value;

		SiteCharlieDeployedSystem.DeployedModemRadio.id = siteCharlieComputerIPAddress.value;
		SiteCharlieDeployedSystem.DeployedModemRadio.hfIPPacketProcessor.id = siteCharlieComputerIPAddress.value;
		SiteCharlieDeployedSystem.DeployedModemRadio.ethernetIPPacketProcessor.id = siteCharlieComputerIPAddress.value;
		SiteCharlieDeployedSystem.DeployedModemRadio.gpsMessageProcessor.id = siteCharlieComputerIPAddress.value;
		SiteCharlieDeployedSystem.DeployedSwitchRouter.id = siteCharlieComputerIPAddress.value;
		SiteCharlieDeployedSystem.DeployedComputer.id = siteCharlieComputerIPAddress.value;

		GPS.id = Long.valueOf(0);
		""");

		componentNamesConstraintText = new SysMLDocumentation("""
		C2System.ModemRadio.name = Optional.of("C2ModemRadio");
		C2System.ModemRadio.hfIPPacketProcessor.name = Optional.of("C2DLP");
		C2System.ModemRadio.ethernetIPPacketProcessor.name = Optional.of("C2EPP");
		C2System.ModemRadio.gpsMessageProcessor.name = Optional.of("C2GPSMP");
		C2System.SwitchRouter.name = Optional.of("C2SwitchRouter");
		C2System.C2Computer.name = Optional.of("C2Computer");

		SiteAlphaDeployedSystem.DeployedModemRadio.name = Optional.of("AlphaModemRadio");
		SiteAlphaDeployedSystem.DeployedModemRadio.hfIPPacketProcessor.name = Optional.of("AlphaDLP");
		SiteAlphaDeployedSystem.DeployedModemRadio.ethernetIPPacketProcessor.name = Optional.of("AlphaEPP");
		SiteAlphaDeployedSystem.DeployedModemRadio.gpsMessageProcessor.name = Optional.of("AlphaGPSMP");
		SiteAlphaDeployedSystem.DeployedSwitchRouter.name = Optional.of("AlphaSwitchRouter");
		SiteAlphaDeployedSystem.DeployedComputer.name = Optional.of("AlphaComputer");

		SiteBravoDeployedSystem.DeployedModemRadio.name = Optional.of("BravoModemRadio");
		SiteBravoDeployedSystem.DeployedModemRadio.hfIPPacketProcessor.name = Optional.of("BravoDLP");
		SiteBravoDeployedSystem.DeployedModemRadio.ethernetIPPacketProcessor.name = Optional.of("BravoEPP");
		SiteBravoDeployedSystem.DeployedModemRadio.gpsMessageProcessor.name = Optional.of("BravoGPSMP");
		SiteBravoDeployedSystem.DeployedSwitchRouter.name = Optional.of("BravoSwitchRouter");
		SiteBravoDeployedSystem.DeployedComputer.name = Optional.of("BravoComputer");

		SiteCharlieDeployedSystem.DeployedModemRadio.name = Optional.of("CharlieModemRadio");
		SiteCharlieDeployedSystem.DeployedModemRadio.hfIPPacketProcessor.name = Optional.of("CharlieDLP");
		SiteCharlieDeployedSystem.DeployedModemRadio.ethernetIPPacketProcessor.name = Optional.of("CharlieEPP");
		SiteCharlieDeployedSystem.DeployedModemRadio.gpsMessageProcessor.name = Optional.of("CharlieGPSMP");
		SiteCharlieDeployedSystem.DeployedSwitchRouter.name = Optional.of("CharlieSwitchRouter");
		SiteCharlieDeployedSystem.DeployedComputer.name = Optional.of("CharlieComputer");

		GPS.name = Optional.of("GPS");
		""");
	}

	@Override
	protected void createConstraints()
	{
		hostNamesConstraint = new SysMLConstraint(Optional.of(hostNamesConstraintFunction), hostNamesConstraintText, "hostNamesConstraint", 0L);
		ipRoutesConstraint = new SysMLConstraint(Optional.of(ipRoutesConstraintFunction), ipRoutesConstraintText, "ipRoutesConstraint", 0L);
		componentIdentifiersConstraint = new SysMLConstraint(Optional.of(componentIdentifiersConstraintFunction), componentIdentifiersConstraintText,
		"componentIdentifiersConstraint", 0L);
		componentNamesConstraint = new SysMLConstraint(Optional.of(componentNamesConstraintFunction), componentNamesConstraintText, "componentNamesConstraint", 0L);
	}

	@Override
	protected void createAnalysisCases()
	{
		tdmaWaitTimeFrequencies = new TDMAWaitTimeFrequencyAnalysisCase();
	}

	@Override
	protected void createBindingConnectors()
	{
		c2SystemTDMAWaitTimeBindingConnnector = new SysMLBindingConnector(C2System.ModemRadio.tdmaTransmit.timeWaited, tdmaWaitTimeFrequencies, ParamContextsEnum.c2.toString());
		alphaDeployedSystemTDMAWaitTimeBindingConnnector = new SysMLBindingConnector(SiteAlphaDeployedSystem.DeployedModemRadio.tdmaTransmit.timeWaited, tdmaWaitTimeFrequencies,
		ParamContextsEnum.alpha.toString());
		bravoDeployedSystemTDMAWaitTimeBindingConnnector = new SysMLBindingConnector(SiteBravoDeployedSystem.DeployedModemRadio.tdmaTransmit.timeWaited, tdmaWaitTimeFrequencies,
		ParamContextsEnum.bravo.toString());
		charlieDeployedSystemTDMAWaitTimeBindingConnnector = new SysMLBindingConnector(SiteCharlieDeployedSystem.DeployedModemRadio.tdmaTransmit.timeWaited, tdmaWaitTimeFrequencies,
		ParamContextsEnum.charlie.toString());

		tdmaWaitTimeFrequencies.paramConnectors.put(ParamContextsEnum.c2.toString(), c2SystemTDMAWaitTimeBindingConnnector);
		tdmaWaitTimeFrequencies.paramConnectors.put(ParamContextsEnum.alpha.toString(), alphaDeployedSystemTDMAWaitTimeBindingConnnector);
		tdmaWaitTimeFrequencies.paramConnectors.put(ParamContextsEnum.bravo.toString(), bravoDeployedSystemTDMAWaitTimeBindingConnnector);
		tdmaWaitTimeFrequencies.paramConnectors.put(ParamContextsEnum.charlie.toString(), charlieDeployedSystemTDMAWaitTimeBindingConnnector);
	}

	@Override
	protected void createFlowConnectors()
	{
		//Connectors for executable model
		c2ToSiteAlphaHFConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, C2System.ModemRadio.hfTransmit, SiteAlphaDeployedSystem.DeployedModemRadio.hfReceive, "", 0L);
		siteAlphaToC2HFConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, SiteAlphaDeployedSystem.DeployedModemRadio.hfTransmit, C2System.ModemRadio.hfReceive, "", 0L);
		c2ToSiteBravoHFConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, C2System.ModemRadio.hfTransmit, SiteBravoDeployedSystem.DeployedModemRadio.hfReceive, "", 0L);
		siteBravoToC2HFConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, SiteBravoDeployedSystem.DeployedModemRadio.hfTransmit, C2System.ModemRadio.hfReceive, "", 0L);
		c2ToSiteCharlieHFConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, C2System.ModemRadio.hfTransmit, SiteCharlieDeployedSystem.DeployedModemRadio.hfReceive, "",
		0L);
		siteCharlieToC2HFConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, SiteCharlieDeployedSystem.DeployedModemRadio.hfTransmit, C2System.ModemRadio.hfReceive, "",
		0L);

		gpsToC2MessagingConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, GPS.GPSMessaging, C2System.ModemRadio.GPSMessaging, "", 0L);
		gpsToSiteAlphaMessagingConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, GPS.GPSMessaging, SiteAlphaDeployedSystem.DeployedModemRadio.GPSMessaging, "", 0L);
		gpsToSiteBravoMessagingConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, GPS.GPSMessaging, SiteBravoDeployedSystem.DeployedModemRadio.GPSMessaging, "", 0L);
		gpsToSiteCharlieMessagingConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, GPS.GPSMessaging, SiteCharlieDeployedSystem.DeployedModemRadio.GPSMessaging, "", 0L);

		//Connectors for requirements model
		  C2SystemToSiteAlphaSystemInterface = new SysMLFlowConnector(TypesEnum.virtual, true, C2System.C2Computer.C2WebBrowser,
		SiteAlphaDeployedSystem.DeployedComputer.DeployedWebServer, "", 0L);
		  C2SystemToSiteBravoSystemInterface = new SysMLFlowConnector(TypesEnum.virtual, true, C2System.C2Computer.C2WebBrowser,
		SiteBravoDeployedSystem.DeployedComputer.DeployedWebServer, "", 0L);
		C2SystemToSiteCharlieSystemInterface = new SysMLFlowConnector(TypesEnum.virtual, true, C2System.C2Computer.C2WebBrowser,
		SiteCharlieDeployedSystem.DeployedComputer.DeployedWebServer, "", 0L);

		         GPSToC2SystemInterface = new SysMLFlowConnector(TypesEnum.virtual, false, GPS.GPSMessaging, C2System.ModemRadio.GPSMessaging, "", 0L);
		  GPSToSiteAlphaSystemInterface = new SysMLFlowConnector(TypesEnum.virtual, false, GPS.GPSMessaging, SiteAlphaDeployedSystem.DeployedModemRadio.GPSMessaging, "", 0L);
		  GPSToSiteBravoSystemInterface = new SysMLFlowConnector(TypesEnum.virtual, false, GPS.GPSMessaging, SiteBravoDeployedSystem.DeployedModemRadio.GPSMessaging, "", 0L);
		GPSToSiteCharlieSystemInterface = new SysMLFlowConnector(TypesEnum.virtual, false, GPS.GPSMessaging, SiteCharlieDeployedSystem.DeployedModemRadio.GPSMessaging, "", 0L);
	}

	@Override
	protected void createIssues()
	{
		tdmaSlotTimesTooLong = new SysMLIssue("Tests suggest TDMA slot times are not fully utilized.  Shortening these times could significantly improve HF link performance",
		"slotTimesNotFullyUtilized", 0L);
	}

	@Override
	protected void createElementFilters()
	{
		requirementSystems = new SysMLElementGroup(true, List.of(), List.of(C2System, SiteAlphaDeployedSystem, SiteBravoDeployedSystem, SiteCharlieDeployedSystem),
		"requirementSystems", 0L);
	}
}
