package hflink.common.events;

import hflink.common.objects.EthernetPacket;
import hflink.common.signals.EthernetPacketSignal;
import sysmlinjava.events.SysMLSignalEvent;

/**
 * Event for the receipt of an ethernet packet
 * 
 * @author ModelerOne
 *
 */
public class EthernetPacketEvent extends SysMLSignalEvent
{
	public EthernetPacketEvent(EthernetPacket packet)
	{
		super("EthernetPacket");
		((EthernetPacketSignal)signal).packet = new EthernetPacket(packet);
	}

	public EthernetPacketEvent(EthernetPacketSignal signal)
	{
		super("EthernetPacket");
		((EthernetPacketSignal)signal).packet = new EthernetPacket(signal.packet);
	}

	public EthernetPacket getPacket()
	{
		return ((EthernetPacketSignal)signal).packet;
	}

	@Override
	public String toString()
	{
		return String.format("EthernetPacketEvent [signal=%s]", signal);
	}

	@Override
	public void createSignal()
	{
		signal = new EthernetPacketSignal(new EthernetPacket());
	}
}
