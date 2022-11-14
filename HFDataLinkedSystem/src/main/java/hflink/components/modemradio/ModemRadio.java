package hflink.components.modemradio;

import java.util.Optional;
import hflink.common.objects.DataLinkFrame;
import hflink.common.objects.GPSMessage;
import hflink.common.objects.IPPacket;
import hflink.common.ports.DataLinkTransmitProtocol;
import hflink.common.ports.DataLinkReceiveProtocol;
import hflink.common.ports.ElectricalPowerProtocol;
import hflink.common.ports.EthernetProtocol;
import hflink.common.ports.GPSMessagingReceiveProtocol;
import hflink.common.ports.HighFrequencyTransmitProtocol;
import hflink.common.ports.IP;
import hflink.common.ports.HighFrequencyReceiveProtocol;
import hflink.common.ports.PSKProtocol;
import hflink.common.ports.TDMATransmitProtocol;
import hflink.common.ports.TDMAReceiveProtocol;
import hflink.common.ports.ThermalHeatTransferProtocol;
import hflink.requirements.HFDataLinkedSystemRequirements;
import sysmlinjava.annotations.AssociationConnector;
import sysmlinjava.annotations.AssociationConnectorFunction;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.ProxyPort;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Requirement;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.comments.Comment;
import sysmlinjava.annotations.comments.ConstraintNote;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.comments.Problem;
import sysmlinjava.annotations.comments.Rationale;
import sysmlinjava.annotations.requirements.RequirementAttribute;
import sysmlinjava.annotations.requirements.RequirementCapability;
import sysmlinjava.annotations.requirements.RequirementComponent;
import sysmlinjava.annotations.requirements.RequirementConstraint;
import sysmlinjava.annotations.requirements.RequirementCost;
import sysmlinjava.annotations.requirements.RequirementInterfaceProtocol;
import sysmlinjava.annotations.requirements.RequirementInterfaceProtocolConnector;
import sysmlinjava.annotations.requirements.RequirementPerformance;
import sysmlinjava.annotations.requirements.RequirementPhysical;
import sysmlinjava.annotations.requirements.RequirementReliability;
import sysmlinjava.annotations.requirements.RequirementResource;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLComment;
import sysmlinjava.comments.SysMLConstraintNote;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.comments.SysMLProblem;
import sysmlinjava.comments.SysMLRationale;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.connectors.SysMLAssociationBlockConnector;
import sysmlinjava.connectors.SysMLAssociationBlockConnectorFunction;
import sysmlinjava.kinds.SysMLFlowDirectionKind;
import sysmlinjava.requirements.SysMLRequirement;
import sysmlinjava.requirements.SysMLVerificationMethodKind;
import sysmlinjava.valuetypes.BitsPerSecond;
import sysmlinjava.valuetypes.Cost$US;
import sysmlinjava.valuetypes.HeatWatts;
import sysmlinjava.valuetypes.IInteger;
import sysmlinjava.valuetypes.MassKilograms;
import sysmlinjava.valuetypes.Percent;
import sysmlinjava.valuetypes.PowerWatts;
import sysmlinjava.valuetypes.VolumeMetersCubic;

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
 *
 */
public class ModemRadio extends SysMLBlock
{
	/**
	 * Port for data-link transmit protocol that uses the TDMA protocol
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public DataLinkTransmitProtocol dataLinkTransmit;
	/**
	 * Port for TDMA transmit protocol that uses the PSK protocol
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public TDMATransmitProtocol tdmaTransmit;
	/**
	 * Port for PSK transmit protocol that uses the HF protocol
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public PSKProtocol pskTransmit;
	/**
	 * Port for HF transmit protocol
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public HighFrequencyTransmitProtocol hfTransmit;

	/**
	 * Port for data-link receive protocol that uses the TDMA protocol
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public DataLinkReceiveProtocol dataLinkReceive;
	/**
	 * Port for TDMA transmit protocol that uses the PSK protocol
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public TDMAReceiveProtocol tdmaReceive;
	/**
	 * Port for PSK receive protocol that uses the HF protocol
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public PSKProtocol pskReceive;
	/**
	 * Port for HF receive protocol
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public HighFrequencyReceiveProtocol hfReceive;
	/**
	 * Port for IP protocol
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public IP ip;
	/**
	 * Port for Ethernet protocol
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public EthernetProtocol ethernet;
	/**
	 * Port for GPS messaging protocol
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public GPSMessagingReceiveProtocol GPSMessaging;
	/**
	 * Port for electrical power input
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public ElectricalPowerProtocol electricPower;
	/**
	 * Port for thermal heat output
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public ThermalHeatTransferProtocol thermalHeat;

	/**
	 * Flow for heat out
	 */
	@RequirementResource(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Flow(direction = SysMLFlowDirectionKind.out)
	public HeatWatts maxHeatOut;
	/**
	 * Flow for power in
	 */
	@RequirementResource(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Flow(direction = SysMLFlowDirectionKind.in)
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
	 * modem-radio(s) via the HF-based data link protocol
	 */
	@ProxyPort
	public HFIPPacketProcessorProxy hfIPPacketProcessorProxy;
	/**
	 * Port for the invocation of the processor of IP packets received from the
	 * computer via the ethernet protocol
	 */
	@ProxyPort
	public EthernetIPPacketProcessorProxy ethernetIPPacketProcessorProxy;
	/**
	 * Port for the invocation of the processor of GPS messages received from GPS
	 * via the GPS messaging protocol
	 */
	@ProxyPort
	public GPSMessageProcessorProxy gpsMessageProcessorProxy;

	/**
	 * Value for the calculated volume of this modem-radio
	 */
	@Value
	public VolumeMetersCubic size;
	/**
	 * Value for the maximum size of this modem-radio
	 */
	@RequirementPhysical(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Value
	public VolumeMetersCubic maximumSize;
	/**
	 * Value for the calculated weight of this modem-radio
	 */
	@Value
	public MassKilograms weight;
	/**
	 * Value for the maximum weight required of this modem-radio
	 */
	@RequirementPhysical(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Value
	public MassKilograms maximumWeight;
	/**
	 * Value for the bit-rate for the HF datalink of this modem-radio
	 */
	@Value
	public BitsPerSecond hfLinkSpeed;
	/**
	 * Value for the minimum bit-rate for the HF datalink required of this
	 * modem-radio
	 */
	@RequirementPerformance(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Value
	public BitsPerSecond minimumHFLinkSpeed;
	/**
	 * Value for the calculated availability of this modem-radio
	 */
	@Value
	public Percent availability;
	/**
	 * Value for the minimum availability (Ao) of the modem-radio
	 */
	@RequirementReliability(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Value
	public Percent minimumAvailability;
	/**
	 * Value for the calculated cost of the modem-radio
	 */
	@Value
	public Cost$US cost;
	/**
	 * Value for the maximum cost of this modem-radio
	 */
	@RequirementCost(requirementVerificationMethod = SysMLVerificationMethodKind.Analysis)
	@Value
	public Cost$US maximumCost;

	/**
	 * Value for the slot of the TDMA protocol assigned to this modem-radio
	 */
	@RequirementAttribute(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Value
	public IInteger tdmaSlotID;
	/**
	 * Value for the (simulated) IP address assigned to this modem-radio
	 */
	@RequirementAttribute(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Value
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
	 * Function to make the connections between the PSK and HF protocols of the HF
	 * data link transmit protocols stack
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction pskToHFTransmitStackConnectorFunction;
	/**
	 * Function to make the connections between the TDMA and PSK protocols of the HF
	 * data link transmit protocols stack
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction tdmaToPSKTransmitStackConnectorFunction;
	/**
	 * Function to make the connections between the DataLink and TDMA protocols of
	 * the HF data link transmit protocols stack
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction dataLinkToTDMATransmitStackConnectorFunction;
	/**
	 * Function to make the connections between the HF and PSK protocols of the HF
	 * data link transmit protocols stack
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction hfToPSKReceiveStackConnectorFunction;
	/**
	 * Function to make the connections between the PSK and TDMA protocols of the HF
	 * data link transmit protocols stack
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction pskToTDMAReceiveStackConnectorFunction;
	/**
	 * Function to make the connections between the TDMA and DataLink protocols of
	 * the HF data link transmit protocols stack
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction tdmaToDataLinkReceiveStackConnectorFunction;

	/**
	 * to make the connections between the PSK and HF protocols of the HF data link
	 * transmit protocols stack
	 */
	@RequirementInterfaceProtocolConnector
	@AssociationConnector
	public SysMLAssociationBlockConnector pskToHFTransmitStackConnector;
	/**
	 * to make the connections between the TDMA and PSK protocols of the HF data
	 * link transmit protocols stack
	 */
	@RequirementInterfaceProtocolConnector
	@AssociationConnector
	public SysMLAssociationBlockConnector tdmaToPSKTransmitStackConnector;
	/**
	 * Connector between the DataLink and TDMA protocols of the HF data link
	 * transmit protocols stack
	 */
	@RequirementInterfaceProtocolConnector
	@AssociationConnector
	public SysMLAssociationBlockConnector dataLinkToTDMATransmitStackConnector;
	/**
	 * Connector between the HF and PSK protocols of the HF data link transmit
	 * protocols stack
	 */
	@RequirementInterfaceProtocolConnector
	@AssociationConnector
	public SysMLAssociationBlockConnector hfToPSKReceiveStackConnector;
	/**
	 * Connector between the PSK and TDMA protocols of the HF data link transmit
	 * protocols stack
	 */
	@RequirementInterfaceProtocolConnector
	@AssociationConnector
	public SysMLAssociationBlockConnector pskToTDMAReceiveStackConnector;
	/**
	 * Connector between the TDMA and DataLink protocols of the HF data link
	 * transmit protocols stack
	 */
	@RequirementInterfaceProtocolConnector
	@AssociationConnector
	public SysMLAssociationBlockConnector tdmaToDataLinkReceiveStackConnector;

	/**
	 * FunctionConnector between the protocols of the ethernet protocols stack
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction ethernetToIPStackConnectorFunction;
	/**
	 * Connector that invokes the function to make the connections between the
	 * protocols of the ethernet protocols stack
	 */
	@RequirementInterfaceProtocolConnector
	@AssociationConnector
	public SysMLAssociationBlockConnector ethernetToIPStackConnector;
	/**
	 * Function to make the connection between the HF IP packet processor proxy port
	 * and the HF IP packet processor
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction hfIPPacketProcessorConnectorFunction;
	/**
	 * Connector that invokes the function to make the connection between the HF IP
	 * packet processor proxy port and the HF IP packet processor
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector hfIPPacketProcessorConnector;
	/**
	 * Function to make the connection between the ethernet IP packet processor
	 * proxy port and the Ethernet IP packet processor
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction ethernetIPPacketProcessorConnectorFunction;
	/**
	 * Connector that invokes the function to make the connection between the
	 * ethernet IP packet processor proxy port and the Ethernet IP packet processor
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector ethernetIPPacketProcessorConnector;
	/**
	 * Function to make the connection between the GPS message processor proxy port
	 * and the GPS message processorf
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction gpsMessageProcessorConnectorFunction;
	/**
	 * Connector that invokes the function to make the connection between the GPS
	 * message processor proxy port and the GPS message processorf
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector gpsMessageProcessorConnector;
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
	@Problem
	public SysMLProblem modemRadioProblem;
	/**
	 * Rationale for the modem-radio
	 */
	@Rationale
	public SysMLRationale modemRadioRationale;
	/**
	 * Hyperlink for the modem-radio
	 */
	@Hyperlink
	public SysMLHyperlink modemRadioHyperlink;
	/**
	 * Constraint note for the modem-radio
	 */
	@ConstraintNote
	public SysMLConstraintNote modemRadioConstraintNote;
	/**
	 * Requirement for the modem-radio
	 */
	@Requirement
	public SysMLRequirement modemRadioRequirement;

	/**
	 * Constructor
	 * 
	 * @param name unique name
	 */
	public ModemRadio(String name)
	{
		super(name, (long)0);
	}

	/**
	 * Operation to set the modem-radio's associated (simulated) IP address. Note
	 * operation also sets the TDMA slot ID which is same as IP address.
	 * 
	 * @param ipAddress assigned address
	 */
	@RequirementCapability(requirementVerificationMethod = SysMLVerificationMethodKind.Inspection)
	@Operation
	public void setIPAddress(IInteger ipAddress)
	{
		this.ipAddress.value = ipAddress.value;
		hfIPPacketProcessor.ipAddress.value = ipAddress.value;
		tdmaTransmit.tdmaSlotID.value = ipAddress.value;
	}

	/**
	 * Reception to react to the receipt of an IP packet via the ethernet connection
	 * 
	 * @param ipPacket packet received via ethernet
	 */
	@RequirementCapability(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Reception
	public void processIPPacketFromEthernet(IPPacket ipPacket)
	{
		ethernetIPPacketProcessorProxy.processIPPacket(ipPacket);
	}

	/**
	 * Reception to react to the receipt of a data frame via the datalink connection
	 * 
	 * @param frame data frame received via the datalink
	 */
	@RequirementCapability(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Reception
	public void processIPPacketFromDataLink(DataLinkFrame frame)
	{
		hfIPPacketProcessorProxy.processDataLinkFrame(frame);
	}

	/**
	 * Reception to react to the receipt of a GPS time message via the GPS messaging
	 * protocol
	 * 
	 * @param gpsMessage received time message
	 */
	@RequirementCapability(requirementVerificationMethod = SysMLVerificationMethodKind.Test)
	@Reception
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
	protected void createValues()
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
	}

	@Override
	protected void createFlows()
	{
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
	protected void createFullPorts()
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
	}

	@Override
	protected void createProxyPorts()
	{
		hfIPPacketProcessorProxy = new HFIPPacketProcessorProxy(this, Optional.empty(), 0L);
		ethernetIPPacketProcessorProxy = new EthernetIPPacketProcessorProxy(this, Optional.empty(), 0L);
		gpsMessageProcessorProxy = new GPSMessageProcessorProxy(this, Optional.empty(), 0L);
	}

	@Override
	protected void createConnectorFunctions()
	{
		ethernetToIPStackConnectorFunction = () ->
		{
			ethernet.addConnectedPortClient(ip);
			ip.addConnectedPortServer(ethernet);
		};

		hfToPSKReceiveStackConnectorFunction = () -> hfReceive.addConnectedPortClient(pskReceive);
		pskToTDMAReceiveStackConnectorFunction = () -> pskReceive.addConnectedPortClient(tdmaReceive);
		tdmaToDataLinkReceiveStackConnectorFunction = () -> tdmaReceive.addConnectedPortClient(dataLinkReceive);

		dataLinkToTDMATransmitStackConnectorFunction = () -> dataLinkTransmit.addConnectedPortServer(tdmaTransmit);
		tdmaToPSKTransmitStackConnectorFunction = () -> tdmaTransmit.addConnectedPortServer(pskTransmit);
		pskToHFTransmitStackConnectorFunction = () -> pskTransmit.addConnectedPortServer(hfTransmit);

		hfIPPacketProcessorConnectorFunction = () -> hfIPPacketProcessorProxy.addConnectedPortPeer(hfIPPacketProcessor.proxy);
		ethernetIPPacketProcessorConnectorFunction = () -> ethernetIPPacketProcessorProxy.addConnectedPortPeer(ethernetIPPacketProcessor.processorProxy);
		gpsMessageProcessorConnectorFunction = () -> gpsMessageProcessorProxy.addConnectedPortPeer(gpsMessageProcessor.proxy);
	}

	@Override
	protected void createConnectors()
	{
		ethernetToIPStackConnector = new SysMLAssociationBlockConnector(ethernet, ip, ethernetToIPStackConnectorFunction);

		hfToPSKReceiveStackConnector = new SysMLAssociationBlockConnector(hfReceive, pskReceive, hfToPSKReceiveStackConnectorFunction);
		pskToTDMAReceiveStackConnector = new SysMLAssociationBlockConnector(pskReceive, tdmaReceive, pskToTDMAReceiveStackConnectorFunction);
		tdmaToDataLinkReceiveStackConnector = new SysMLAssociationBlockConnector(tdmaReceive, dataLinkReceive, tdmaToDataLinkReceiveStackConnectorFunction);
		pskToHFTransmitStackConnector = new SysMLAssociationBlockConnector(pskTransmit, hfTransmit, pskToHFTransmitStackConnectorFunction);
		tdmaToPSKTransmitStackConnector = new SysMLAssociationBlockConnector(tdmaTransmit, pskTransmit, tdmaToPSKTransmitStackConnectorFunction);
		dataLinkToTDMATransmitStackConnector = new SysMLAssociationBlockConnector(dataLinkTransmit, tdmaTransmit, dataLinkToTDMATransmitStackConnectorFunction);

		hfIPPacketProcessorConnector = new SysMLAssociationBlockConnector(this, hfIPPacketProcessor, hfIPPacketProcessorConnectorFunction);
		ethernetIPPacketProcessorConnector = new SysMLAssociationBlockConnector(this, hfIPPacketProcessor, ethernetIPPacketProcessorConnectorFunction);
		gpsMessageProcessorConnector = new SysMLAssociationBlockConnector(this, hfIPPacketProcessor, gpsMessageProcessorConnectorFunction);
	}

	@Override
	protected void createConstraints()
	{
		availabilityConstraint = () -> availability.greaterThanOrEqualTo(minimumAvailability);
		costConstraint = () -> cost.lessThanOrEqualTo(maximumCost);
	}

	@Override
	protected void createComments()
	{
		modemRadioComment = new SysMLComment("Modem radio model is work in progress");
	}

	@Override
	protected void createProblems()
	{
		modemRadioProblem = new SysMLProblem("TDMA wait times need to be reduced as time slots appear to be more than enough bandwidth for data volumes being transmitted");
	}

	@Override
	protected void createRationales()
	{
		modemRadioRationale = new SysMLRationale("Modem and radio have been integrated into single component as current technology allows it and SWAP-C is reduced by doing so");
	}

	@Override
	protected void createConstraintNotes()
	{
		modemRadioConstraintNote = new SysMLConstraintNote(() -> maximumCost.value = 21.0);
	}

	@Override
	protected void createHyperlinks()
	{
		modemRadioHyperlink = new SysMLHyperlink("Modem-Radio Specification", "http://modemradios.com/specs/Modem-Radio Specification.pdf");
	}

	@Override
	protected void createRequirements()
	{
		modemRadioRequirement = HFDataLinkedSystemRequirements.id1_3_2_1_2_4;
	}
}
