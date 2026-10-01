package hflink.components.modemradio;

import java.util.Optional;

import hflink.common.items.IPPacket;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.ports.ProxyPort;
import sysmlinjava.javaannotations.requirements.RequirementCapability;
import sysmlinjava.javaannotations.requirements.RequirementInterfaceInterface;
import sysmlinjava.javaannotations.requirements.RequirementSpecificationLink;
import sysmlinjava.parts.SysMLPart;

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
public class EthernetIPPacketProcessor extends SysMLPart implements EthernetIPPacketProcessorInterface
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
	public EthernetIPPacketProcessor(SysMLPart contextBlock, long id)
	{
		super(Optional.of(contextBlock), "EthernetPacketProcessor", id);
	}

	@RequirementCapability
	@Action
	@Override
	public void processIPPacket(IPPacket ipPacket)
	{
		((ModemRadio)context.get()).dataLinkTransmit.transmit(ipPacket);
	}

	@Override
	protected void createPorts()
	{
		processorProxy = new EthernetIPPacketProcessorProxy(this, Optional.of(this), "EthernetIPPacketProcessorProxy", 0L);
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		processingSpecification = new SysMLHyperlink("SRS-EthernetIPPacketProcessor", "file://Software Requirements Specification For The Ethernet IP Packet Processor.html");
	}
}
