package hflink.components.modemradio;

import java.util.Optional;
import hflink.common.objects.DataLinkFrame;
import hflink.common.objects.IPPacket;
import hflink.common.ports.EthernetProtocol;
import sysmlinjava.annotations.ProxyPort;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.requirements.RequirementAttribute;
import sysmlinjava.annotations.requirements.RequirementInterfaceInterface;
import sysmlinjava.annotations.requirements.RequirementSpecificationLink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.valuetypes.IInteger;

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
public class HFIPPacketProcessor extends SysMLBlock implements HFIPPacketProcessorInterface
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
	@Value
	public IInteger ipAddress;

	/**
	 * Constructor
	 * 
	 * @param contextBlock block in whose context the processor resides
	 * @param id           unique ID
	 */
	public HFIPPacketProcessor(SysMLBlock contextBlock, long id)
	{
		super(contextBlock, "DataLinkFrameProcessor", (long)id);
	}

	@Override
	public void processDataLinkFrame(DataLinkFrame frame)
	{
		IPPacket ipPacket = retrieveIPPacketFromDataLink(frame);
		if (ipPacketDestinationIsThisDestination(ipPacket))
			transmitIPPacketViaEthernet(ipPacket, ((ModemRadio)contextBlock.get()).ethernet);
	}

	/**
	 * Retrieves the IP packet from the data-link fram
	 * 
	 * @param frame data-link frame
	 * @return IP packet
	 */
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
	public void transmitIPPacketViaEthernet(IPPacket ipPacket, EthernetProtocol ethernetProtocol)
	{
		ethernetProtocol.transmit(ipPacket);
	}

	@Override
	protected void createValues()
	{
		ipAddress = new IInteger(IInteger.dynamicallyAssignedValue);
	}

	@Override
	protected void createProxyPorts()
	{
		proxy = new HFIPPacketProcessorProxy(this, Optional.of(this), 0L);
	}

	@Override
	protected void createHyperlinks()
	{
		processingSpecification = new SysMLHyperlink("SRS-HFIPPacketProcessor", "file://Software Requirements Specification For The HF IP Packet Processor.html");
	}
}
