package dbssystem.sensors;

import java.net.InetAddress;
import java.util.Optional;

import dbssystem.common.MotionSignalSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.ports.SysMLPort;

/**
 * Port for receiving the tremor motion signals from the patient by the tremor
 * motion sensor
 * 
 * @author ModelerOne
 */
public class MotionSignalInPort extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param tremorSensor TremorSensor to receive the signal events
	 * @param ipAddress    IP address to be used for UDP-based port receptions
	 * @param udpPort      UDP port number to be used for UDP-based port receptions
	 */
	public MotionSignalInPort(TremorSensor tremorSensor, InetAddress ipAddress, Integer udpPort)
	{
		super(tremorSensor, Optional.of(tremorSensor), ipAddress, udpPort, 0L, "MotionSignalInPort");
	}

	@Action
	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof MotionSignalSignal motionSignalSignal)
			result = new SysMLSignalEvent(motionSignalSignal, "MotionSignalEvent", 0L);
		return result;
	}
}
