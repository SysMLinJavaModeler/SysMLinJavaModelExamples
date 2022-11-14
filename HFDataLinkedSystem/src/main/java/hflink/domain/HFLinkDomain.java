package hflink.domain;

import java.util.List;
import java.util.Optional;
import hflink.common.objects.DNS;
import hflink.constraintblocks.TDMAWaitTimeFrequencyConstraintBlock;
import hflink.constraintblocks.TDMAWaitTimeFrequencyConstraintBlock.ParamContextBlocksEnum;
import hflink.systems.c2system.CommandControlSystem;
import hflink.systems.deployedsystem.DeployedSystem;
import hflink.systems.gps.GPS;
import sysmlinjava.annotations.AssociationConnector;
import sysmlinjava.annotations.AssociationConnectorFunction;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.comments.ElementGroup;
import sysmlinjava.annotations.comments.Problem;
import sysmlinjava.annotations.parametrics.BindingConnector;
import sysmlinjava.annotations.parametrics.BindingConnectorFunction;
import sysmlinjava.annotations.parametrics.ConstraintBlock;
import sysmlinjava.annotations.requirements.RequirementConstraint;
import sysmlinjava.annotations.requirements.RequirementDesign;
import sysmlinjava.annotations.requirements.RequirementInterface;
import sysmlinjava.annotations.requirements.RequirementSystem;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLElementGroup;
import sysmlinjava.comments.SysMLProblem;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.connectors.SysMLAssociationBlockConnector;
import sysmlinjava.connectors.SysMLAssociationBlockConnectorFunction;
import sysmlinjava.connectors.SysMLBindingConnector;
import sysmlinjava.connectors.SysMLBindingConnectorFunction;
import sysmlinjava.ports.SysMLConstraintParameterPort;
import sysmlinjava.requirements.SysMLVerificationMethodKind;
import sysmlinjava.valuetypes.IInteger;
import sysmlinjava.valuetypes.SString;

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
 * protocol modeled as a {@code SysMLFullPort} to simulate the operations of the
 * actual standard protocol. The combination of the protocols into protocol
 * stacks provides for a more complete and precise simulation of the
 * interactions that must take place correctly for the system-of-systems to
 * operate correctly.
 * <p>
 * This {@code HFLinkDomain} block contains all of the systems that comprise the
 * domain as {@code SysMLBlock} parts. The parts include the single
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
 *
 */
public class HFLinkDomain extends SysMLBlock
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
	@ElementGroup
	public SysMLElementGroup requirementSystems;

	/**
	 * Declaration of the current "problem" cited for the modeled/simulated systems,
	 * i.e. that TDMA slot times are too long, should be shortened, as evidenced by
	 * the amount of unused slot time that was exposed by the simulation.
	 */
	@Problem
	public SysMLProblem tdmaSlotTimesTooLong;

	/**
	 * Connector functions that connect the systems of the HFLink domain, i.e. the
	 * C2 System, the 3 Deployed Systems, and the GPS.
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction c2ToSiteAlphaHFConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction c2ToSiteBravoHFConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction c2ToSiteCharlieHFConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction gpsToC2MessagingConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction gpsToSiteAlphaMessagingConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction gpsToSiteBravoMessagingConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction gpsToSiteCharlieMessagingConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction gpsToC2SystemConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction gpsToSiteAlphaSystemConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction gpsToSiteBravoSystemConnectorFunction;
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction gpsToSiteCharlieSystemConnectorFunction;

	/**
	 * Connector that connects the HF protocol of the C2 System with the HF protocol
	 * of the Alpha Deployed System.
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector c2ToSiteAlphaHFConnector;
	/**
	 * Connector that connects the HF protocol of the C2 System with the HF protocol
	 * of the Bravo Deployed System.
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector c2ToSiteBravoHFConnector;
	/**
	 * Connector that connects the HF protocol of the C2 System with the HF protocol
	 * of the Charlie Deployed System.
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector c2ToSiteCharlieHFConnector;
	/**
	 * Connector that connects the protocol of the GPS System with the protocol of
	 * the C2 System.
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector gpsToC2MessagingConnector;
	/**
	 * Connector that connects the protocol of the GPS System with the protocol of
	 * the Alpha Deployed System.
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector gpsToSiteAlphaMessagingConnector;
	/**
	 * Connector that connects the protocol of the GPS System with the protocol of
	 * the Bravo Deployed System.
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector gpsToSiteBravoMessagingConnector;
	/**
	 * Connector that connects the protocol of the GPS System with the protocol of
	 * the Charlie Deployed System.
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector gpsToSiteCharlieMessagingConnector;

	/**
	 * Connector function that connects the C2 computer to the Alpha site computer.
	 * Note this is a "virtual" connector that is used to identify an interface
	 * requirement.
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction c2SystemToSiteAlphaSystemConnectorFunction;
	/**
	 * Connector function that connects the C2 computer to the site Bravo computer.
	 * Note this is a "virtual" connector that is used to identify an interface
	 * requirement.
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction c2SystemToSiteBravoSystemConnectorFunction;
	/**
	 * Connector function that connects the C2 computer to the site Charlie
	 * computer. Note this is a "virtual" connector that is used to identify an
	 * interface requirement.
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction c2SystemToSiteCharlieSystemConnectorFunction;

	/**
	 * Connector between the c2Computer and the site Alpha computer. Note this is a
	 * "virtual" connector used to identify an interface requirement.
	 */
	@RequirementInterface
	@RequirementDesign(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@AssociationConnector
	public SysMLAssociationBlockConnector C2SystemToSiteAlphaSystemInterface;
	/**
	 * Connector between the c2Computer and the site Bravo computer. Note this is a
	 * "virtual" connector used to identify an interface requirement.
	 */
	@RequirementInterface
	@RequirementDesign(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@AssociationConnector
	public SysMLAssociationBlockConnector C2SystemToSiteBravoSystemInterface;
	/**
	 * Connector between the c2Computer and the site Charlie computer. Note this is
	 * a "virtual" connector used to identify an interface requirement.
	 */
	@RequirementInterface
	@RequirementDesign(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@AssociationConnector
	public SysMLAssociationBlockConnector C2SystemToSiteCharlieSystemInterface;

	/**
	 * Connector between the {@code GPS} and the {@code C2System}. Note this is a
	 * "virtual" connector used to identify an interface requirement.
	 */
	@RequirementInterface
	@AssociationConnector
	public SysMLAssociationBlockConnector GPSToC2SystemInterface;
	/**
	 * Connector between the {@code GPS} and the {@code SiteAlphaSystem}. Note this
	 * is a "virtual" connector used to identify an interface requirement.
	 */
	@RequirementInterface
	@AssociationConnector
	public SysMLAssociationBlockConnector GPSToSiteAlphaSystemInterface;
	/**
	 * Connector between the {@code GPS} and the {@code SiteBravoSystem}. Note this
	 * is a "virtual" connector used to identify an interface requirement.
	 */
	@RequirementInterface
	@AssociationConnector
	public SysMLAssociationBlockConnector GPSToSiteBravoSystemInterface;
	/**
	 * Connector between the {@code GPS} and the {@code SiteCharlieSystem}. Note
	 * this is a "virtual" connector used to identify an interface requirement.
	 */
	@RequirementInterface
	@AssociationConnector
	public SysMLAssociationBlockConnector GPSToSiteCharlieSystemInterface;
	/**
	 * Constraint block used to calculate the frequency of TDMA wait times
	 * experienced by the systems, i.e. a proxy for protocol stack efficiency.
	 */
	@ConstraintBlock
	public TDMAWaitTimeFrequencyConstraintBlock tdmaWaitTimeFrequencies;

	/**
	 * Binding connector function that makes the "connections" between the TDMA wait
	 * times for each system's modem-radio and the associated parameter ports of the
	 * constraint block
	 */
	@BindingConnectorFunction
	public SysMLBindingConnectorFunction tdmaWaitTimeParamsConnnectorFunction;

	/**
	 * Binding connector that invokes the function that makes the "connections"
	 * between the TDMA wait times for each system's modem-radio and the associated
	 * parameter ports of the constraint block
	 */
	@BindingConnector
	public SysMLBindingConnector tdmaWaitTimeParamsConnnector;

	/**
	 * Specifies the unique names of each of the systems components. These are names
	 * in the context of the model vs. hostNames which are in the context of the
	 * network simulation.
	 */
	@RequirementConstraint(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Constraint()
	public SysMLConstraint componentNamesConstraint;

	/**
	 * Specifies the unique IDs for each of the systems components. Unique IDs are
	 * simply the unique (pseudo) IP address.
	 */
	@RequirementConstraint(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Constraint()
	public SysMLConstraint componentIdentifiersConstraint;

	/**
	 * Specifies the (pseudo) IP addresses of the systems computers and builds the
	 * routing tables (maps) for the network
	 */
	@RequirementConstraint(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Constraint()
	public SysMLConstraint ipRoutesConstraint;

	/**
	 * Specifies the hostNames for each of the systems computers on the network
	 */
	@RequirementConstraint(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Constraint()
	public SysMLConstraint hostNamesConstraint;

	/**
	 * Constructor
	 */
	public HFLinkDomain()
	{
		super();
		hostNamesConstraint.apply();
		ipRoutesConstraint.apply();
		componentIdentifiersConstraint.apply();
		componentNamesConstraint.apply();
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
	protected void createConstraints()
	{
		hostNamesConstraint = () ->
		{
			C2System.C2Computer.hostName = new SString("www.hflinkedc2.com");
			SiteAlphaDeployedSystem.DeployedComputer.hostName = new SString("www.hflinkeddep1.com");
			SiteBravoDeployedSystem.DeployedComputer.hostName = new SString("www.hflinkeddep2.com");
			SiteCharlieDeployedSystem.DeployedComputer.hostName = new SString("www.hflinkeddep3.com");
		};

		ipRoutesConstraint = () ->
		{
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
		};

		componentIdentifiersConstraint = () ->
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
		};

		componentNamesConstraint = () ->
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
		};
	}

	@Override
	protected void createConstraintBlocks()
	{
		tdmaWaitTimeFrequencies = new TDMAWaitTimeFrequencyConstraintBlock();
	}

	@Override
	protected void createConstraintParameterConnectorFunctions()
	{
		tdmaWaitTimeParamsConnnectorFunction = () ->
		{
			SysMLConstraintParameterPort c2Port = tdmaWaitTimeFrequencies.paramPorts.get(ParamContextBlocksEnum.c2.toString());
			c2Port.setParameterContextBlock(C2System.ModemRadio);
			C2System.ModemRadio.tdmaTransmit.timeWaited.bindTo(c2Port);

			SysMLConstraintParameterPort alphaPort = tdmaWaitTimeFrequencies.paramPorts.get(ParamContextBlocksEnum.alpha.toString());
			alphaPort.setParameterContextBlock(SiteAlphaDeployedSystem.DeployedModemRadio);
			SiteAlphaDeployedSystem.DeployedModemRadio.tdmaTransmit.timeWaited.bindTo(alphaPort);
			
			SysMLConstraintParameterPort bravoPort = tdmaWaitTimeFrequencies.paramPorts.get(ParamContextBlocksEnum.bravo.toString());
			bravoPort.setParameterContextBlock(SiteBravoDeployedSystem.DeployedModemRadio);
			SiteBravoDeployedSystem.DeployedModemRadio.tdmaTransmit.timeWaited.bindTo(bravoPort);

			SysMLConstraintParameterPort charliePort = tdmaWaitTimeFrequencies.paramPorts.get(ParamContextBlocksEnum.charlie.toString());
			charliePort.setParameterContextBlock(SiteCharlieDeployedSystem.DeployedModemRadio);
			SiteCharlieDeployedSystem.DeployedModemRadio.tdmaTransmit.timeWaited.bindTo(charliePort);
		};
	}

	@Override
	protected void createConstraintParameterConnectors()
	{
		tdmaWaitTimeParamsConnnector = new SysMLBindingConnector(List.of(C2System.ModemRadio, SiteAlphaDeployedSystem.DeployedModemRadio, SiteBravoDeployedSystem.DeployedModemRadio, SiteCharlieDeployedSystem.DeployedModemRadio),
			tdmaWaitTimeFrequencies, tdmaWaitTimeParamsConnnectorFunction);
	}

	@Override
	protected void createConnectorFunctions()
	{
		c2ToSiteAlphaHFConnectorFunction = () ->
		{
			C2System.ModemRadio.hfTransmit.addConnectedPortPeer(SiteAlphaDeployedSystem.DeployedModemRadio.hfReceive);
			SiteAlphaDeployedSystem.DeployedModemRadio.hfTransmit.addConnectedPortPeer(C2System.ModemRadio.hfReceive);
		};
		c2ToSiteBravoHFConnectorFunction = () ->
		{
			C2System.ModemRadio.hfTransmit.addConnectedPortPeer(SiteBravoDeployedSystem.DeployedModemRadio.hfReceive);
			SiteBravoDeployedSystem.DeployedModemRadio.hfTransmit.addConnectedPortPeer(C2System.ModemRadio.hfReceive);
		};
		c2ToSiteCharlieHFConnectorFunction = () ->
		{
			C2System.ModemRadio.hfTransmit.addConnectedPortPeer(SiteCharlieDeployedSystem.DeployedModemRadio.hfReceive);
			SiteCharlieDeployedSystem.DeployedModemRadio.hfTransmit.addConnectedPortPeer(C2System.ModemRadio.hfReceive);
		};
		gpsToC2MessagingConnectorFunction = () -> GPS.GPSMessaging.addConnectedPortPeer(C2System.ModemRadio.GPSMessaging);
		gpsToSiteAlphaMessagingConnectorFunction = () -> GPS.GPSMessaging.addConnectedPortPeer(SiteAlphaDeployedSystem.DeployedModemRadio.GPSMessaging);
		gpsToSiteBravoMessagingConnectorFunction = () -> GPS.GPSMessaging.addConnectedPortPeer(SiteBravoDeployedSystem.DeployedModemRadio.GPSMessaging);
		gpsToSiteCharlieMessagingConnectorFunction = () -> GPS.GPSMessaging.addConnectedPortPeer(SiteCharlieDeployedSystem.DeployedModemRadio.GPSMessaging);

		c2SystemToSiteAlphaSystemConnectorFunction = () ->
		{
			C2System.C2Computer.C2WebBrowser.WebBrowserHTTP.addVirtualConnectedPortPeer(SiteAlphaDeployedSystem.DeployedComputer.DeployedWebServer.WebServerHTTP);
			SiteAlphaDeployedSystem.DeployedComputer.DeployedWebServer.WebServerHTTP.addVirtualConnectedPortPeer(C2System.C2Computer.C2WebBrowser.WebBrowserHTTP);
		};
		c2SystemToSiteBravoSystemConnectorFunction = () ->
		{
			C2System.C2Computer.C2WebBrowser.WebBrowserHTTP.addVirtualConnectedPortPeer(SiteBravoDeployedSystem.DeployedComputer.DeployedWebServer.WebServerHTTP);
			SiteBravoDeployedSystem.DeployedComputer.DeployedWebServer.WebServerHTTP.addVirtualConnectedPortPeer(C2System.C2Computer.C2WebBrowser.WebBrowserHTTP);
		};
		c2SystemToSiteCharlieSystemConnectorFunction = () ->
		{
			C2System.C2Computer.C2WebBrowser.WebBrowserHTTP.addVirtualConnectedPortPeer(SiteCharlieDeployedSystem.DeployedComputer.DeployedWebServer.WebServerHTTP);
			SiteCharlieDeployedSystem.DeployedComputer.DeployedWebServer.WebServerHTTP.addVirtualConnectedPortPeer(C2System.C2Computer.C2WebBrowser.WebBrowserHTTP);
		};
		gpsToC2SystemConnectorFunction = () -> GPS.GPSMessaging.addVirtualConnectedPortPeer(C2System.ModemRadio.GPSMessaging);
		gpsToSiteAlphaSystemConnectorFunction = () -> GPS.GPSMessaging.addVirtualConnectedPortPeer(SiteAlphaDeployedSystem.DeployedModemRadio.GPSMessaging);
		gpsToSiteBravoSystemConnectorFunction = () -> GPS.GPSMessaging.addVirtualConnectedPortPeer(SiteBravoDeployedSystem.DeployedModemRadio.GPSMessaging);
		gpsToSiteCharlieSystemConnectorFunction = () -> GPS.GPSMessaging.addVirtualConnectedPortPeer(SiteCharlieDeployedSystem.DeployedModemRadio.GPSMessaging);
	}

	@Override
	protected void createConnectors()
	{
		c2ToSiteAlphaHFConnector = new SysMLAssociationBlockConnector(C2System, SiteAlphaDeployedSystem, c2ToSiteAlphaHFConnectorFunction);
		c2ToSiteBravoHFConnector = new SysMLAssociationBlockConnector(C2System, SiteBravoDeployedSystem, c2ToSiteBravoHFConnectorFunction);
		c2ToSiteCharlieHFConnector = new SysMLAssociationBlockConnector(C2System, SiteCharlieDeployedSystem, c2ToSiteCharlieHFConnectorFunction);

		gpsToC2MessagingConnector = new SysMLAssociationBlockConnector(GPS, C2System, gpsToC2MessagingConnectorFunction);
		gpsToSiteAlphaMessagingConnector = new SysMLAssociationBlockConnector(GPS, SiteAlphaDeployedSystem, gpsToSiteAlphaMessagingConnectorFunction);
		gpsToSiteBravoMessagingConnector = new SysMLAssociationBlockConnector(GPS, SiteBravoDeployedSystem, gpsToSiteBravoMessagingConnectorFunction);
		gpsToSiteCharlieMessagingConnector = new SysMLAssociationBlockConnector(GPS, SiteCharlieDeployedSystem, gpsToSiteCharlieMessagingConnectorFunction);

		C2SystemToSiteAlphaSystemInterface = new SysMLAssociationBlockConnector(C2System, SiteAlphaDeployedSystem, c2SystemToSiteAlphaSystemConnectorFunction);
		C2SystemToSiteBravoSystemInterface = new SysMLAssociationBlockConnector(C2System, SiteBravoDeployedSystem, c2SystemToSiteBravoSystemConnectorFunction);
		C2SystemToSiteCharlieSystemInterface = new SysMLAssociationBlockConnector(C2System, SiteCharlieDeployedSystem, c2SystemToSiteCharlieSystemConnectorFunction);

		GPSToC2SystemInterface = new SysMLAssociationBlockConnector(GPS, C2System, gpsToC2SystemConnectorFunction);
		GPSToSiteAlphaSystemInterface = new SysMLAssociationBlockConnector(GPS, SiteAlphaDeployedSystem, gpsToSiteAlphaSystemConnectorFunction);
		GPSToSiteBravoSystemInterface = new SysMLAssociationBlockConnector(GPS, SiteBravoDeployedSystem, gpsToSiteBravoSystemConnectorFunction);
		GPSToSiteCharlieSystemInterface = new SysMLAssociationBlockConnector(GPS, SiteCharlieDeployedSystem, gpsToSiteCharlieSystemConnectorFunction);
	}

	@Override
	protected void createProblems()
	{
		tdmaSlotTimesTooLong = new SysMLProblem("Tests suggest TDMA slot times are not fully utilized.  Shortening these times could significantly improve HF link performance");
	}

	@Override
	protected void createElementGroups()
	{
		requirementSystems = new SysMLElementGroup("requirementSystems", "systems of the HFLink domain model for which the model is the requirements", "requirement system", Optional.empty(),
			Optional.of(List.of(C2System, SiteAlphaDeployedSystem, SiteBravoDeployedSystem, SiteCharlieDeployedSystem)));
	}

}
