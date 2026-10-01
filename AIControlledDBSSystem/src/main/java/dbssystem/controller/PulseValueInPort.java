package dbssystem.controller;

import java.net.InetAddress;
import java.util.Optional;

import dbssystem.common.PulseValueSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.ports.SysMLPort;

/**
 * Port to receive pulse value signals from the pulse sensor by the controller
 * 
 * @author ModelerOne
 *
 */
public class PulseValueInPort extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param context   DBSController of which this is a port, receives signals
	 * @param ipAddress IP address to be used for UDP-based port receptions
	 * @param udpPort   UDP port number to be used for UDP-based port receptions
	 */
	public PulseValueInPort(DBSController context, InetAddress ipAddress, Integer udpPort)
	{
		super(context, Optional.of(context), ipAddress, udpPort, 0L, "PulseValueInPort");
	}

	@Action
	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof PulseValueSignal pulseValueSignal)
			result = new SysMLSignalEvent(pulseValueSignal, "Pulse", 0L);
		return result;
	}

}
