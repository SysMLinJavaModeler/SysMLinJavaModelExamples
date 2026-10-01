package dbssystem.controller;

import java.net.InetAddress;
import java.util.Optional;

import dbssystem.common.TremorLevelSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.ports.SysMLPort;

/**
 * Port to receive tremor level values from the tremor motion sensor by the
 * controller
 * 
 * @author ModelerOne
 *
 */
public class TremorLevelInPort extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param controller   DBSController of which this is a port, to receive signals
	 * @param ipAddress IP address to be used for UDP-based port receptions
	 * @param udpPort   UDP port number to be used for UDP-based port receptions
	 */
	public TremorLevelInPort(DBSController controller, InetAddress ipAddress, Integer udpPort)
	{
		super(controller, Optional.of(controller), ipAddress, udpPort, 0L, "TremorLevelInPort");
	}

	@Action
	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof TremorLevelSignal tremorLevelSignal)
			result = new SysMLSignalEvent(tremorLevelSignal, "Tremor Level", 0L);
		return result;
	}

}
