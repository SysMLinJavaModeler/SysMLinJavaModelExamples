package hflink.common.objects;

import java.util.Optional;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;

/**
 * Representation of the Ethernet packet for transmission via the ethernet
 * protocol/port
 * 
 * @author ModelerOne
 *
 */
public class EthernetPacket extends SysMLClass implements StackedProtocolObject
{
	/**
	 * Address of source of packet
	 */
	@Attribute
	public Long sourceAddress;
	/**
	 * Address of destination of packet
	 */
	@Attribute
	public Long destinationAddress;
	/**
	 * Frame of data carried by the packet
	 */
	@Attribute
	public IPPacket frame;

	/**
	 * Constructor
	 * 
	 * @param sourceAddress      address of packet source
	 * @param destinationAddress of packet destination
	 * @param frame              data carried by packet
	 */
	public EthernetPacket(Long sourceAddress, Long destinationAddress, IPPacket frame)
	{
		super();
		this.sourceAddress = sourceAddress;
		this.destinationAddress = destinationAddress;
		this.frame = frame;
	}

	public EthernetPacket(EthernetPacket packet)
	{
		super();
		this.sourceAddress = packet.sourceAddress;
		this.destinationAddress = packet.destinationAddress;
		this.frame = new IPPacket(packet.frame);
	}

	public EthernetPacket()
	{
		super();
		this.sourceAddress = 0L;
		this.destinationAddress = 0L;
		this.frame = new IPPacket();
	}

	@Override
	public String toString()
	{
		return String.format("EthernetPacket [sourceAddress=%s, destinationAddress=%s, frame=%s]", sourceAddress, destinationAddress, frame);
	}

	@Override
	public String stackNamesString()
	{
		String result = "<none>";
		if (frame instanceof IPPacket)
			result = stackNamesString(this, Optional.of((IPPacket)frame));
		return result;
	}
}
