package dbssystem.actuators;

import java.net.InetAddress;
import java.util.Optional;

import dbssystem.common.DBSControlSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.ports.SysMLPort;

/**
 * Port to receive control data by the DBS actuator
 * 
 * @author ModelerOne
 *
 */
public class DBSControlInPort extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock DBSActuator to receive the signals
	 * @param ipAddress    IP address to be used for UDP-based port receptions
	 * @param udpPort      UDP port number to be used for UDP-based port receptions
	 */
	public DBSControlInPort(DBSActuator contextBlock, InetAddress ipAddress, Integer udpPort)
	{
		super(contextBlock, Optional.of(contextBlock), ipAddress, udpPort, 0L, "DBSControlInPort");
	}

	@Action
	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof DBSControlSignal)
			result = new SysMLSignalEvent(signal, "Control", 0L);
		return result;
	}
}
