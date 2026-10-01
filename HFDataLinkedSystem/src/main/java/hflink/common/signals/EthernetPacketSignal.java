package hflink.common.signals;

import java.io.Serializable;

import hflink.common.items.EthernetPacket;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.common.StackedProtocolObject;

/**
 * Signal containing the Ethernet packet for transmission via the ethernet
 * protocol/port
 * 
 * @author ModelerOne
 *
 */
public class EthernetPacketSignal extends SysMLSignal implements StackedProtocolObject, Serializable
{
	private static final long serialVersionUID = -7843429046742240566L;

	/**
	 * Ethernet packet value
	 */
	@Attribute
	public EthernetPacket packet;

	/**
	 * Constructor for the specified ethernet packet
	 * 
	 * @param packet ethernet packet value
	 */
	public EthernetPacketSignal(EthernetPacket packet)
	{
		super("EthernetPacket", 0L);
		this.packet = packet;
	}

	@Override
	public String stackNamesString()
	{
		return packet.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("EthernetPacketSignal [packet=%s]", packet);
	}
}
