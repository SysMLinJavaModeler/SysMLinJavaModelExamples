package dbssystem.sensors;

import java.net.InetAddress;
import java.util.Optional;

import dbssystem.common.PressureSignalSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.ports.SysMLPort;

/**
 * Port for receiving the blood pressure signals from the patient by the pulse
 * sensor
 * 
 * @author ModelerOne
 *
 */
public class PressureSignalInPort extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param pulseSensor PulseSensor to receive the signals
	 * @param ipAddress    IP address to be used for UDP-based port signal
	 *                     receptions
	 * @param udpPort      UDP port number to be used for UDP-based port signal
	 *                     receptions
	 */
	public PressureSignalInPort(PulseSensor pulseSensor, InetAddress ipAddress, Integer udpPort)
	{
		super(pulseSensor, Optional.of(pulseSensor), ipAddress, udpPort, 0L, "PressureSignalInPort");
	}

	@Action
	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof PressureSignalSignal)
			result = new SysMLSignalEvent(signal, "PressureSignalEvent", 0L);
		return result;
	}

}
