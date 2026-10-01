package hflink.components.modemradio;

import java.util.Optional;

import hflink.common.items.DataLinkFrame;
import hflink.common.items.GPSMessage;
import hflink.common.items.IPPacket;
import hflink.common.ports.DataLinkReceiveProtocol;
import hflink.common.ports.DataLinkTransmitProtocol;
import hflink.common.ports.ElectricalPowerProtocol;
import hflink.common.ports.EthernetProtocol;
import hflink.common.ports.GPSMessagingReceiveProtocol;
import hflink.common.ports.HighFrequencyReceiveProtocol;
import hflink.common.ports.HighFrequencyTransmitProtocol;
import hflink.common.ports.IP;
import hflink.common.ports.PSKProtocol;
import hflink.common.ports.TDMAReceiveProtocol;
import hflink.common.ports.TDMATransmitProtocol;
import hflink.common.ports.ThermalHeatTransferProtocol;
import hflink.requirements.HFDataLinkedSystemRequirements;
import sysmlinjava.annotations.SysMLComment;
import sysmlinjava.annotations.SysMLDocumentation;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.attributetypes.BitsPerSecond;
import sysmlinjava.attributetypes.Cost$US;
import sysmlinjava.attributetypes.HeatWatts;
import sysmlinjava.attributetypes.IInteger;
import sysmlinjava.attributetypes.MassKilograms;
import sysmlinjava.attributetypes.Percent;
import sysmlinjava.attributetypes.PowerWatts;
import sysmlinjava.attributetypes.VolumeMetersCubic;
import sysmlinjava.connectors.SysMLFlowConnector;
import sysmlinjava.connectors.SysMLFlowConnector.TypesEnum;
import sysmlinjava.constraint.BasicConstraintFunction;
import sysmlinjava.constraint.SysMLConstraint;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.annotations.Comment;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.connectors.FlowConnector;
import sysmlinjava.javaannotations.constraint.Constraint;
import sysmlinjava.javaannotations.constraint.ConstraintFunction;
import sysmlinjava.javaannotations.constraint.ConstraintText;
import sysmlinjava.javaannotations.metadata.Issue;
import sysmlinjava.javaannotations.metadata.Rationale;
import sysmlinjava.javaannotations.metadata.Risk;
import sysmlinjava.javaannotations.metadata.RiskLevel;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.javaannotations.ports.ProxyPort;
import sysmlinjava.javaannotations.requirements.RequirementAttribute;
import sysmlinjava.javaannotations.requirements.RequirementCapability;
import sysmlinjava.javaannotations.requirements.RequirementComponent;
import sysmlinjava.javaannotations.requirements.RequirementConstraint;
import sysmlinjava.javaannotations.requirements.RequirementConstraintFunction;
import sysmlinjava.javaannotations.requirements.RequirementConstraintText;
import sysmlinjava.javaannotations.requirements.RequirementCost;
import sysmlinjava.javaannotations.requirements.RequirementInterfaceProtocol;
import sysmlinjava.javaannotations.requirements.RequirementInterfaceProtocolConnector;
import sysmlinjava.javaannotations.requirements.RequirementPerformance;
import sysmlinjava.javaannotations.requirements.RequirementPhysical;
import sysmlinjava.javaannotations.requirements.RequirementReliability;
import sysmlinjava.javaannotations.requirements.RequirementResource;
import sysmlinjava.javaannotations.requirements.SatisfiedRequirement;
import sysmlinjava.metadata.SysMLIssue;
import sysmlinjava.metadata.SysMLLevel;
import sysmlinjava.metadata.SysMLRationale;
import sysmlinjava.metadata.SysMLRisk;
import sysmlinjava.metadata.SysMLRiskLevel;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.requirements.SysMLRequirement;
import sysmlinjava.requirements.SysMLVerificationMethodKind;

/**
 * The ModemRadio is a system component that modulates/demodulates a HF/RF
 * signal for datalink-level communications between the command/control and
 * remotely deployed systems. It receives IP packets over an ethernet connection
 * and encapsulates them into datalink frames for transmission via a TDMA
 * protocol over an HD link. It also receives datalink frames via the TDMA
 * protocol over the HD link and decapsulates the IP packet for transmission
 * over the ethernet connection. It uses a GPS time message to synchronize its
 * TDMA protocol with the other ModemRadios in the domain.
 * <p>
 * The ModemRadio block includes full ports for each of the protocols used to
 * communicate with the computers, the GPS, and other ModemRadios. These
 * protocols include IP over ethernet for computer communications, a datalink
 * protocol over TDMA over PSK over HF for modemRadio communications, and a
 * standard GPS protocol for GPS communications. It also includes full ports for
 * power and heat and their corresponding flows - power-in and heat-out.
 * Specified block values include availability, size, weight, speed, and cost.
 * TDMA slot ID and IP address are also block values.
 * <p>
 * The ModemRadio is also specified to include block parts to perform the actual
 * capabilities of the component. These parts include a processor for the IP
 * packets received via the ethernet, a processor for the IP packets received
 * via the TDMA/HF datalink, and a processor for the time messages received via
 * GPS. The ModemRadio's interface with these processors is via proxy-ports
 * which are also included in the block.
 * <p>
 * The block also contains all of the connectors between the parts and ports in
 * the block. These include the connectors with the processor proxy ports and
 * between the full ports that represent the protocol stacks of the external
 * interfaces. Note the connectors between ModemRadio protocols and external
 * systems are specified in the system block and/or the domain block.
 * <p>
 * This model of the modem-radio is annotated with requirements annotations to
 * enable use of the model for the automated generation of the SysML
 * requirements specification by commercial requirements generation tools - more
 * specifically the SysMLinJava TaskMaster&trade; product. Requirements
 * annotations start with the "Requirement" text followed by a text name/phrase
 * that specifies the type of requirement the model element is to be used to
 * specify. The requirements generation tool uses the model element's
 * annotation, declaration, and initialization to generate one or more SysML
 * requirements to include the requirement's ID, title, text, type, verification
 * method, and risk. The tool generates the requirements as a SysMLinJava
 * requirements class (to emulate the requirements diagram) as a java file,
 * and/or as a CSV file, PDF file, SQL file, HTML file, or plain text file, as
 * selected in the requirements generation tool. See SysMLinJava.com for
 * details.
 * 
 * @author ModelerOne
 */
public class ModemRadio extends SysMLPart
{
	/**
	 * Port for data-link transmit protocol that uses the TDMA protocol
	 */
	@RequirementInterfaceProtocol
	@Port
	public DataLinkTransmitProtocol dataLinkTransmit;
	/**
	 * Port for TDMA transmit protocol that uses the PSK protocol
	 */
	@RequirementInterfaceProtocol
	@Port
	public TDMATransmitProtocol tdmaTransmit;
	/**
	 * Port for PSK transmit protocol that uses the HF protocol
	 */
	@RequirementInterfaceProtocol
	@Port
	public PSKProtocol pskTransmit;
	/**
	 * Port for HF transmit protocol
	 */
	@RequirementInterfaceProtocol
	@Port
	public HighFrequencyTransmitProtocol hfTransmit;

	/**
	 * Port for data-link receive protocol that uses the TDMA protocol
	 */
	@RequirementInterfaceProtocol
	@Port
	public DataLinkReceiveProtocol dataLinkReceive;
	/**
	 * Port for TDMA transmit protocol that uses the PSK protocol
	 */
	@RequirementInterfaceProtocol
	@Port
	public TDMAReceiveProtocol tdmaReceive;
	/**
	 * Port for PSK receive protocol that uses the HF protocol
	 */
	@RequirementInterfaceProtocol
	@Port
	public PSKProtocol pskReceive;
	/**
	 * Port for HF receive protocol
	 */
	@RequirementInterfaceProtocol
	@Port
	public HighFrequencyReceiveProtocol hfReceive;
	/**
	 * Port for IP protocol
	 */
	@RequirementInterfaceProtocol
	@Port
	public IP ip;
	/**
	 * Port for Ethernet protocol
	 */
	@RequirementInterfaceProtocol
	@Port
	public EthernetProtocol ethernet;
	/**
	 * Port for GPS messaging protocol
	 */
	@RequirementInterfaceProtocol
	@Port
	public GPSMessagingReceiveProtocol GPSMessaging;
	/**
	 * Port for electrical power input
	 */
	@RequirementInterfaceProtocol
	@Port
	public ElectricalPowerProtocol electricPower;
	/**
	 * Port for thermal heat output
	 */
	@RequirementInterfaceProtocol
	@Port
	public ThermalHeatTransferProtocol thermalHeat;

	/**
	 * Flow for heat out
	 */
	@RequirementResource(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Attribute
	public HeatWatts maxHeatOut;
	/**
	 * Flow for power in
	 */
	@RequirementResource(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Attribute
	public PowerWatts maxPowerIn;

	/**
	 * Part for the processor of the IP packets recieved via the HF data-link
	 * protocols stack
	 */
	@RequirementComponent(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Part
	public HFIPPacketProcessor hfIPPacketProcessor;
	/**
	 * Part for the processor of the IP packets recieved via the ethernet protocol
	 * stack
	 */
	@RequirementComponent(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Part
	public EthernetIPPacketProcessor ethernetIPPacketProcessor;
	/**
	 * Part for the processor of the GPS time messages received via the GPS
	 * messaging protocol
	 */
	@RequirementComponent(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Part
	public GPSMessageProcessor gpsMessageProcessor;

	/**
	 * Port for the invocation of the processor of IP packets received from other
	 * modem-radio(s) via the HF-based data link protocol. Defined as a proxy port
	 * to enable replacement or upgrade of the packet processor without change to
	 * modem radio
	 */
	@ProxyPort
	public HFIPPacketProcessorProxy hfIPPacketProcessorProxy;
	/**
	 * Port for the invocation of the processor of IP packets received from the
	 * computer via the ethernet protocol. Defined as a proxy port to enable
	 * replacement or upgrade of the packet processor without change to modem radio
	 */
	@ProxyPort
	public EthernetIPPacketProcessorProxy ethernetIPPacketProcessorProxy;
	/**
	 * Port for the invocation of the processor of GPS messages received from GPS
	 * via the GPS messaging protocol. Defined as a proxy port to enable replacement
	 * or upgrade of the GPS message processor without change to modem radio
	 */
	@ProxyPort
	public GPSMessageProcessorProxy gpsMessageProcessorProxy;

	/**
	 * Value for the calculated volume of this modem-radio
	 */
	@Attribute
	public VolumeMetersCubic size;
	/**
	 * Value for the maximum size of this modem-radio
	 */
	@RequirementPhysical(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Attribute
	public VolumeMetersCubic maximumSize;
	/**
	 * Value for the calculated weight of this modem-radio
	 */
	@Attribute
	public MassKilograms weight;
	/**
	 * Value for the maximum weight required of this modem-radio
	 */
	@RequirementPhysical(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Attribute
	public MassKilograms maximumWeight;
	/**
	 * Value for the bit-rate for the HF datalink of this modem-radio
	 */
	@Attribute
	public BitsPerSecond hfLinkSpeed;
	/**
	 * Value for the minimum bit-rate for the HF datalink required of this
	 * modem-radio
	 */
	@RequirementPerformance(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Attribute
	public BitsPerSecond minimumHFLinkSpeed;
	/**
	 * Value for the calculated availability of this modem-radio
	 */
	@Attribute
	public Percent availability;
	/**
	 * Value for the minimum availability (Ao) of the modem-radio
	 */
	@RequirementReliability(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)

	@Attribute
	public Percent minimumAvailability;

	/**
	 * Value for the calculated cost of the modem-radio
	 */
	@Attribute
	public Cost$US cost;

	/**
	 * Level of risk for development cost
	 */
	@RiskLevel
	public SysMLRiskLevel costRiskLevel;

	/**
	 * Level of risk for development schedule
	 */
	@RiskLevel
	public SysMLRiskLevel scheduleRiskLevel;

	/**
	 * Level of risk for technical development
	 */
	@RiskLevel
	public SysMLRiskLevel technicalRiskLevel;

	/**
	 * Level of risk for total development
	 */
	@RiskLevel
	public SysMLRiskLevel totalRiskLevel;

	/**
	 * ModemRadio development risk
	 */
	@Risk
	public SysMLRisk risk;

	/**
	 * Value for the maximum cost of this modem-radio
	 */
	@RequirementCost(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Attribute
	public Cost$US maximumCost;

	/**
	 * Value for the slot of the TDMA protocol assigned to this modem-radio
	 */
	@RequirementAttribute(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Attribute
	public IInteger tdmaSlotID;
	/**
	 * Value for the (simulated) IP address assigned to this modem-radio
	 */
	@RequirementAttribute(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Attribute
	public IInteger ipAddress;

	/**
	 * Constraint for the availability of this modem-radio
	 */
	@RequirementConstraint(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Constraint
	public SysMLConstraint availabilityConstraint;
	/**
	 * Constraint for the cost of this modem-radio
	 */
	@RequirementConstraint(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Constraint
	public SysMLConstraint costConstraint;

	/**
	 * Connector between the PSK and HF protocols of the HF data link transmit
	 * protocols stack
	 */
	@RequirementInterfaceProtocolConnector
	@FlowConnector
	public SysMLFlowConnector pskToHFTransmitStackConnector;
	/**
	 * Connector between the TDMA and PSK protocols of the HF data link transmit
	 * protocols stack
	 */
	@RequirementInterfaceProtocolConnector
	@FlowConnector
	public SysMLFlowConnector tdmaToPSKTransmitStackConnector;
	/**
	 * Connector between the DataLink and TDMA protocols of the HF data link
	 * transmit protocols stack
	 */
	@RequirementInterfaceProtocolConnector
	@FlowConnector
	public SysMLFlowConnector dataLinkToTDMATransmitStackConnector;
	/**
	 * Connector between the HF and PSK protocols of the HF data link transmit
	 * protocols stack
	 */
	@RequirementInterfaceProtocolConnector
	@FlowConnector
	public SysMLFlowConnector hfToPSKReceiveStackConnector;
	/**
	 * Connector between the PSK and TDMA protocols of the HF data link transmit
	 * protocols stack
	 */
	@RequirementInterfaceProtocolConnector
	@FlowConnector
	public SysMLFlowConnector pskToTDMAReceiveStackConnector;
	/**
	 * Connector between the TDMA and DataLink protocols of the HF data link
	 * transmit protocols stack
	 */
	@RequirementInterfaceProtocolConnector
	@FlowConnector
	public SysMLFlowConnector tdmaToDataLinkReceiveStackConnector;

	/**
	 * Connector between the protocols of the ethernet protocols stack
	 */
	@RequirementInterfaceProtocolConnector
	@FlowConnector
	public SysMLFlowConnector ipToEthernetStackConnector;
	/**
	 * Connector between the HF IP packet processor proxy port and the HF IP packet
	 * processor
	 */
	@FlowConnector
	public SysMLFlowConnector hfIPPacketProcessorConnector;
	/**
	 * Connector between the ethernet IP packet processor proxy port and the
	 * Ethernet IP packet processor
	 */
	@FlowConnector
	public SysMLFlowConnector ethernetIPPacketProcessorConnector;
	/**
	 * Connector between the GPS message processor proxy port and the GPS message
	 * processor
	 */
	@FlowConnector
	public SysMLFlowConnector gpsMessageProcessorConnector;
	/**
	 * Long number (0) used to indicate that the initialized value is assigned an
	 * actual value dynamically, i.e. sometime after initialization.
	 */
	private static final long dynamicallyAssignedValue = 0;

	/**
	 * Comment on the modem-radio
	 */
	@Comment
	public SysMLComment modemRadioComment;
	/**
	 * Problem with the modem-radio
	 */
	@Issue
	public SysMLIssue modemRadioProblem;
	/**
	 * Rationale for the modem-radio
	 */
	@Rationale
	public SysMLRationale modemRadioRationale;
	/**
	 * Rationale for the proxy components
	 */
	@Rationale
	public SysMLRationale modemRadioProxyComponentsRationale;
	/**
	 * Hyperlink for the modem-radio supporting info
	 */
	@Hyperlink
	public SysMLHyperlink modemRadioHyperlink;
	/**
	 * Text version of the modem-radio max cost constraint
	 */
	@RequirementConstraintFunction
	@ConstraintFunction
	private BasicConstraintFunction modemRadioMaxCostConstraintFunction;
	/**
	 * Text version of the modem-radio max cost constraint
	 */
	@RequirementConstraintText
	@ConstraintText
	private SysMLDocumentation modemRadioMaxCostConstraintText;
	/**
	 * Constraint for the modem-radio cost
	 */
	@RequirementConstraint
	@Constraint
	public SysMLConstraint modemRadioMaxCostConstraint;
	/**
	 * Requirement for the modem-radio
	 */
	@SatisfiedRequirement
	public SysMLRequirement modemRadioRequirement;
	/**
	 * Text version of the modem-radio availability constraint
	 */
	@RequirementConstraintText
	@ConstraintText
	private SysMLDocumentation availabilityConstraintText;
	/**
	 * Text version of the modem-radio cost constraint
	 */
	@RequirementConstraintText
	@ConstraintText
	private SysMLDocumentation costConstraintText;
	/**
	 * Function for the modem-radio availabiity constraint
	 */
	@RequirementConstraintFunction
	@ConstraintFunction
	private BasicConstraintFunction availabilityConstraintFunction;
	/**
	 * Function for the modem-radio cost constraint
	 */
	@RequirementConstraintFunction
	@ConstraintFunction
	private BasicConstraintFunction costConstraintFunction;

	/**
	 * Constructor
	 * 
	 * @param name unique name
	 */
	public ModemRadio(String name)
	{
		super(name, (long) 0);
	}

	/**
	 * Operation to set the modem-radio's associated (simulated) IP address. Note
	 * operation also sets the TDMA slot ID which is same as IP address.
	 * 
	 * @param ipAddress assigned address
	 */
	@RequirementCapability(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Action
	public void setIPAddress(IInteger ipAddress)
	{
		this.ipAddress.value = ipAddress.value;
		hfIPPacketProcessor.ipAddress.value = ipAddress.value;
		tdmaTransmit.tdmaSlotID.value = ipAddress.value;
	}

	/**
	 * Action to react to the receipt of an IP packet via the ethernet connection
	 * 
	 * @param ipPacket packet received via ethernet
	 */
	@RequirementCapability(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Action
	public void processIPPacketFromEthernet(IPPacket ipPacket)
	{
		ethernetIPPacketProcessorProxy.processIPPacket(ipPacket);
	}

	/**
	 * Action to react to the receipt of a data frame via the datalink connection
	 * 
	 * @param frame data frame received via the datalink
	 */
	@RequirementCapability(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Action
	public void processIPPacketFromDataLink(DataLinkFrame frame)
	{
		hfIPPacketProcessorProxy.processDataLinkFrame(frame);
	}

	/**
	 * Action to react to the receipt of a GPS time message via the GPS messaging
	 * protocol
	 * 
	 * @param gpsMessage received time message
	 */
	@RequirementCapability(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Action
	public void processTDMASlotTimeFromGPS(GPSMessage gpsMessage)
	{
		gpsMessageProcessorProxy.processGPSMessage(gpsMessage);
	}

	@Override
	public void start()
	{
		ethernet.start();
		super.start();
	}

	@Override
	public void stop()
	{
		ethernet.stop();
		super.stop();
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new ModemRadioStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		availability = new Percent(99.999);
		minimumAvailability = new Percent(99.998);
		size = new VolumeMetersCubic(0.09);
		maximumSize = new VolumeMetersCubic(0.1);
		weight = new MassKilograms(9.3);
		maximumWeight = new MassKilograms(9.5);
		hfLinkSpeed = new BitsPerSecond(1200);
		minimumHFLinkSpeed = new BitsPerSecond(1200);
		cost = new Cost$US(9_000);
		maximumCost = new Cost$US(10_000);
		ipAddress = new IInteger(dynamicallyAssignedValue);
		tdmaSlotID = new IInteger(dynamicallyAssignedValue);
		maxPowerIn = new PowerWatts(1_500);
		maxHeatOut = new HeatWatts(1_500);
	}

	@Override
	protected void createParts()
	{
		hfIPPacketProcessor = new HFIPPacketProcessor(this, 0L);
		ethernetIPPacketProcessor = new EthernetIPPacketProcessor(this, 0L);
		gpsMessageProcessor = new GPSMessageProcessor(this, 0L);
	}

	@Override
	protected void createPorts()
	{
		ethernet = new EthernetProtocol(this, 0L);
		ip = new IP(this, this, 0L);

		dataLinkReceive = new DataLinkReceiveProtocol(this, this, 0L);
		tdmaReceive = new TDMAReceiveProtocol(this, 0L);
		pskReceive = new PSKProtocol(this, 0L);
		hfReceive = new HighFrequencyReceiveProtocol(this, 0L);

		dataLinkTransmit = new DataLinkTransmitProtocol(this, 0L);
		tdmaTransmit = new TDMATransmitProtocol(this, name.get() + "TDMATransmitProtocol");
		pskTransmit = new PSKProtocol(this, 0L);
		hfTransmit = new HighFrequencyTransmitProtocol(this, 0L);

		GPSMessaging = new GPSMessagingReceiveProtocol(this, this, 0L);

		electricPower = new ElectricalPowerProtocol(this, 0L);
		thermalHeat = new ThermalHeatTransferProtocol(this, 0L);

		hfIPPacketProcessorProxy = new HFIPPacketProcessorProxy(this, Optional.empty(), "HFIPPacketProcessorProxy", 0L);
		ethernetIPPacketProcessorProxy = new EthernetIPPacketProcessorProxy(this, Optional.empty(), "EthernetIPPacketProcessorProxy", 0L);
		gpsMessageProcessorProxy = new GPSMessageProcessorProxy(this, Optional.empty(), "GPSMessageProcessorProxy", 0L);
	}

	@Override
	protected void createFlowConnectors()
	{
		ipToEthernetStackConnector = new SysMLFlowConnector(TypesEnum.clienttoserver, true, ip, ethernet, "", 0L);

		dataLinkToTDMATransmitStackConnector = new SysMLFlowConnector(TypesEnum.clienttoserver, false, dataLinkTransmit, tdmaTransmit, "", 0L);
		tdmaToPSKTransmitStackConnector = new SysMLFlowConnector(TypesEnum.clienttoserver, false, tdmaTransmit, pskTransmit, "", 0L);
		pskToHFTransmitStackConnector = new SysMLFlowConnector(TypesEnum.clienttoserver, false, pskTransmit, hfTransmit, "", 0L);

		hfToPSKReceiveStackConnector = new SysMLFlowConnector(TypesEnum.servertoclient, false, hfReceive, pskReceive, "", 0L);
		pskToTDMAReceiveStackConnector = new SysMLFlowConnector(TypesEnum.servertoclient, false, pskReceive, tdmaReceive, "", 0L);
		tdmaToDataLinkReceiveStackConnector = new SysMLFlowConnector(TypesEnum.servertoclient, false, tdmaReceive, dataLinkReceive, "", 0L);

		hfIPPacketProcessorConnector = new SysMLFlowConnector(hfIPPacketProcessorProxy, hfIPPacketProcessor.proxy, "", 0L);
		ethernetIPPacketProcessorConnector = new SysMLFlowConnector(ethernetIPPacketProcessorProxy, ethernetIPPacketProcessor.processorProxy, "", 0L);
		gpsMessageProcessorConnector = new SysMLFlowConnector(gpsMessageProcessorProxy, gpsMessageProcessor.proxy, "", 0L);
	}

	@Override
	protected void createConstraintFunctions()
	{
		availabilityConstraintFunction = (BasicConstraintFunction) () -> availability.greaterThanOrEqualTo(minimumAvailability);
		costConstraintFunction = (BasicConstraintFunction) () -> cost.lessThanOrEqualTo(maximumCost);
		modemRadioMaxCostConstraintFunction = (BasicConstraintFunction) () -> maximumCost == new Cost$US(10_000);
	}

	@Override
	protected void createConstraintTexts()
	{
		availabilityConstraintText = new SysMLDocumentation("availability.greaterThanOrEqualTo(minimumAvailability)");
		costConstraintText = new SysMLDocumentation("cost.lessThanOrEqualTo(maximumCost)");
		modemRadioMaxCostConstraintText = new SysMLDocumentation("maximumCost is $10,000");
	}

	@Override
	protected void createConstraints()
	{
		availabilityConstraint = new SysMLConstraint(Optional.of(availabilityConstraintFunction), availabilityConstraintText, "Availability OK", 0L);
		costConstraint = new SysMLConstraint(Optional.of(costConstraintFunction), costConstraintText, "Cost OK", 0L);
		modemRadioMaxCostConstraint = new SysMLConstraint(Optional.of(modemRadioMaxCostConstraintFunction), modemRadioMaxCostConstraintText, "MaxCost", 0L);
	}

	@Override
	protected void createComments()
	{
		modemRadioComment = new SysMLComment("Modem radio model is work in progress");
	}

	@Override
	protected void createIssues()
	{
		modemRadioProblem = new SysMLIssue("TDMA wait times need to be reduced as time slots appear to be more than enough bandwidth for data volumes being transmitted",
		"excessiveTDMAWaitTimes", 0L);
	}

	@Override
	protected void createRationales()
	{
		modemRadioRationale = new SysMLRationale("Modem and radio have been integrated into single component as current technology allows it and SWAP-C is reduced by doing so",
		"reducedSWAPC", 0L);
		modemRadioProxyComponentsRationale = new SysMLRationale("Proxies for protocol processors enable easy tech upgrades", "proxyComponents", 0L);
	}

	@Override
	protected void createRiskLevels()
	{
		costRiskLevel = new SysMLRiskLevel(new SysMLLevel(0.5), new SysMLLevel(0.5));
		scheduleRiskLevel = new SysMLRiskLevel(new SysMLLevel(0.5), new SysMLLevel(0.5));
		technicalRiskLevel = new SysMLRiskLevel(new SysMLLevel(0.5), new SysMLLevel(0.5));
		totalRiskLevel = new SysMLRiskLevel(new SysMLLevel(0.5), new SysMLLevel(0.5));
	}

	@Override
	protected void createRisk()
	{
		risk = new SysMLRisk(costRiskLevel, scheduleRiskLevel, technicalRiskLevel, totalRiskLevel, "ModemRadio Risk", 0L);
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		modemRadioHyperlink = new SysMLHyperlink("Modem-Radio Specification", "http://modemradios.com/specs/Modem-Radio Specification.pdf");
	}

	@Override
	protected void createRequirements()
	{
		modemRadioRequirement = HFDataLinkedSystemRequirements.rid1_5_3;
	}
}
