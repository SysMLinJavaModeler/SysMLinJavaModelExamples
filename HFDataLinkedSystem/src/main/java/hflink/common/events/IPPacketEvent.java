package hflink.common.events;

import hflink.common.objects.IPPacket;
import hflink.common.signals.IPPacketSignal;
import sysmlinjava.events.SysMLSignalEvent;

/**
 * Event for receipt of an IP packet
 * 
 * @author ModelerOne
 *
 */
public class IPPacketEvent extends SysMLSignalEvent
{
	public IPPacketEvent(IPPacket packet)
	{
		super("IPPacket");
		((IPPacketSignal)signal).packet.data = packet.data;
		((IPPacketSignal)signal).packet.destinationAddress = packet.destinationAddress;
		((IPPacketSignal)signal).packet.sourceAddress = packet.sourceAddress;
		((IPPacketSignal)signal).packet.id = packet.id;
	}

	public IPPacketEvent(IPPacketSignal signal)
	{
		super("IPPacket");
		((IPPacketSignal)signal).packet.data = signal.packet.data;
		((IPPacketSignal)signal).packet.destinationAddress = signal.packet.destinationAddress;
		((IPPacketSignal)signal).packet.sourceAddress = signal.packet.sourceAddress;
		((IPPacketSignal)signal).packet.id = signal.packet.id;
	}

	public IPPacket getPacket()
	{
		return ((IPPacketSignal)signal).packet;
	}

	@Override
	public String toString()
	{
		return String.format("IPPacketEvent [signal=%s]", signal);
	}

	@Override
	public void createSignal()
	{
		signal = new IPPacketSignal(new IPPacket());
	}
}
