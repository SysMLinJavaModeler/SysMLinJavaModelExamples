package hflink.common.signals;

import java.util.Optional;

import hflink.common.items.IPPacket;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal containing the IP packet transmitted by the IP protocol/port
 * 
 * @author ModelerOne
 *
 */
public class IPPacketSignal extends SysMLSignal
{
	/**
	 * IP packet value
	 */
	@Attribute
	public IPPacket packet;

	/**
	 * Constructor for specified IP packet value
	 * 
	 * @param packet IP packet value
	 */
	public IPPacketSignal(IPPacket packet)
	{
		super("IPPacket", 0L);
		this.packet = packet;
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.empty());
	}

	@Override
	public String toString()
	{
		return String.format("IPPacketSignal [packet=%s]", packet);
	}
}
