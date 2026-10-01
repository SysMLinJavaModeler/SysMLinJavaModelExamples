package hflink.components.modemradio;

import java.util.Optional;

import hflink.common.items.DataLinkFrame;
import hflink.common.items.IPPacket;
import hflink.common.ports.EthernetProtocol;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.attributetypes.IInteger;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.ports.ProxyPort;
import sysmlinjava.javaannotations.requirements.RequirementAttribute;
import sysmlinjava.javaannotations.requirements.RequirementInterfaceInterface;
import sysmlinjava.javaannotations.requirements.RequirementSpecificationLink;
import sysmlinjava.parts.SysMLPart;

/**
 * Processor of IP packets received via the HF data-link protocol stack. The
 * {@code HFIPPacketProcessor} represents a processor of IP packets that could
 * be as simple as an in-process thread or as complex as a parallel processing
 * of a stream. The use of the proxy port to receive the IP packets represents
 * an implementation-independent model of this particular component of the
 * modem-radio.
 * 
 * @author ModelerOne
 *
 */
public class HFIPPacketProcessor extends SysMLPart implements HFIPPacketProcessorInterface
{
	/**
	 * Port for the invocation of the {@code processIPPacket()} operation
	 */
	@RequirementInterfaceInterface
	@ProxyPort
	public HFIPPacketProcessorProxy proxy;

	/**
	 * Comment for hyperlink to specification of the packet processing to take place
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink processingSpecification;

	/**
	 * Value for IP address associated with this modem-radio
	 */
	@RequirementAttribute
	@Attribute
	public IInteger ipAddress;

	/**
	 * Constructor
	 * 
	 * @param context block in whose context the processor resides
	 * @param id           unique ID
	 */
	public HFIPPacketProcessor(SysMLPart context, long id)
	{
		super(Optional.of(context), "DataLinkFrameProcessor", id);
	}

	@Action
	@Override
	public void processDataLinkFrame(DataLinkFrame frame)
	{
		IPPacket ipPacket = retrieveIPPacketFromDataLink(frame);
		if (ipPacketDestinationIsThisDestination(ipPacket))
			transmitIPPacketViaEthernet(ipPacket, ((ModemRadio)context.get()).ethernet);
	}

	/**
	 * Retrieves the IP packet from the data-link fram
	 * 
	 * @param frame data-link frame
	 * @return IP packet
	 */
	@Action
	public IPPacket retrieveIPPacketFromDataLink(DataLinkFrame frame)
	{
		return frame.data;
	}

	/**
	 * Determines if specified IP packet is destined for this modem-radio
	 * 
	 * @param ipPacket subject packet
	 * @return True if packet is for this modem-radio, false otherwise
	 */
	@Action
	public boolean ipPacketDestinationIsThisDestination(IPPacket ipPacket)
	{
		return ipPacket.destinationAddress == ipAddress.value;
	}

	/**
	 * Transmits the receive IP packet via the ethernet protocol to the computer
	 * 
	 * @param ipPacket         packet to transmit
	 * @param ethernetProtocol protocol used to transmit the packet
	 */
	@Action
	public void transmitIPPacketViaEthernet(IPPacket ipPacket, EthernetProtocol ethernetProtocol)
	{
		ethernetProtocol.transmit(ipPacket);
	}

	@Override
	protected void createAttributes()
	{
		ipAddress = new IInteger(0);
	}

	@Override
	protected void createPorts()
	{
		proxy = new HFIPPacketProcessorProxy(this, Optional.of(this), "HFIPPacketProcessorProxy", 0L);
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		processingSpecification = new SysMLHyperlink("SRS-HFIPPacketProcessor", "file://Software Requirements Specification For The HF IP Packet Processor.html");
	}
}
