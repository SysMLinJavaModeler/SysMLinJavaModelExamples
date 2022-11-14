package hflink.components.modemradio;

import java.util.Optional;
import hflink.common.objects.IPPacket;
import sysmlinjava.annotations.ProxyPort;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.requirements.RequirementCapability;
import sysmlinjava.annotations.requirements.RequirementInterfaceInterface;
import sysmlinjava.annotations.requirements.RequirementSpecificationLink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;

/**
 * Processor of IP packets received via ethernet protocol stack. The
 * {@code EthernetIPPacketProcessor} represents a processor of IP packets that
 * could be as simple as an in-process thread or as complex as a parallel
 * processing of a stream. The use of the proxy port to receive the IP packets
 * represents an implementation-independent model of this particular component
 * of the modem-radio.
 * 
 * @author ModelerOne
 *
 */
public class EthernetIPPacketProcessor extends SysMLBlock implements EthernetIPPacketProcessorInterface
{
	/**
	 * Port for the invocation of the {@code processIPPacket()} operation
	 */
	@RequirementInterfaceInterface
	@ProxyPort
	public EthernetIPPacketProcessorProxy processorProxy;

	/**
	 * Comment for hyperlink to specification of the packet processing to take place
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink processingSpecification;

	/**
	 * Constructor
	 * 
	 * @param contextBlock block in whose context the processor resides
	 * @param id           unique ID
	 */
	public EthernetIPPacketProcessor(SysMLBlock contextBlock, long id)
	{
		super(contextBlock, "EthernetPacketProcessor", (long)id);
	}

	@RequirementCapability
	@Override
	public void processIPPacket(IPPacket ipPacket)
	{
		((ModemRadio)contextBlock.get()).dataLinkTransmit.transmit(ipPacket);
	}

	@Override
	protected void createProxyPorts()
	{
		processorProxy = new EthernetIPPacketProcessorProxy(this, Optional.of(this), 0L);
	}

	@Override
	protected void createHyperlinks()
	{
		processingSpecification = new SysMLHyperlink("SRS-EthernetIPPacketProcessor", "file://Software Requirements Specification For The Ethernet IP Packet Processor.html");
	}
}
