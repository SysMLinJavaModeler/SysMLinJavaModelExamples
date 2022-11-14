package hflink.common.signals;

import java.io.Serializable;
import hflink.common.objects.EthernetPacket;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

/**
 * Signal for an ethernet packet
 * 
 * @author ModelerOne
 *
 */
public class EthernetPacketSignal extends SysMLSignal implements StackedProtocolObject, Serializable
{
	private static final long serialVersionUID = -7843429046742240566L;

	@Attribute
	public EthernetPacket packet;

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
