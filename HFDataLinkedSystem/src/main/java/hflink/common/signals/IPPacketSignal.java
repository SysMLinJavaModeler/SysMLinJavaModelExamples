package hflink.common.signals;

import java.util.Optional;
import hflink.common.objects.IPPacket;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

public class IPPacketSignal extends SysMLSignal
{
	@Attribute
	public IPPacket packet;

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
