package hflink.common.objects;

import java.util.Optional;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;

/**
 * Representation of the IP packet transmitted by the IP protocol/port
 * 
 * @author ModelerOne
 *
 */
public class IPPacket extends SysMLClass implements StackedProtocolObject
{
	/**
	 * IP address of the source of the packet
	 */
	@Attribute
	public Integer sourceAddress;
	/**
	 * IP address of the destination of the packet
	 */
	@Attribute
	public Integer destinationAddress;
	/**
	 * Data object carried by the packet
	 */
	@Attribute
	public UDPDatagram data;

	/**
	 * Constructor
	 * 
	 * @param sourceAddress      IP address of the source of the packet
	 * @param destinationAddress IP address of the destination of the packet
	 * @param data               object to be carried by the packet
	 */
	public IPPacket(Integer sourceAddress, Integer destinationAddress, UDPDatagram data)
	{
		super();
		this.sourceAddress = sourceAddress;
		this.destinationAddress = destinationAddress;
		this.data = data;
	}

	public IPPacket(IPPacket data)
	{
		super();
		this.sourceAddress = data.sourceAddress;
		this.destinationAddress = data.destinationAddress;
		this.data = new UDPDatagram(data.data);
	}

	public IPPacket()
	{
		super();
		this.sourceAddress = 0;
		this.destinationAddress = 0;
		this.data = null;
	}

	@Override
	public String stackNamesString()
	{
		String result = "<none>";
		if (data instanceof UDPDatagram)
			result = stackNamesString(this, Optional.of((UDPDatagram)data));
		return result;
	}

	@Override
	public String toString()
	{
		return String.format("IPPacket [sourceAddress=%s, destinationAddress=%s, data=%s]", sourceAddress, destinationAddress, data);
	}
}
