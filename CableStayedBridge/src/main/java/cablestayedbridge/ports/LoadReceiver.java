package cablestayedbridge.ports;

import java.util.Optional;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.ports.SysMLPort;
import sysmlinjava.states.StateBehaviorContext;

/**
 * SysMLinJava full port representation of a load-bearing component interface
 * that receives a load (weight, force) from another component's
 * {@code LoadTransmitter}.
 * 
 * @author ModelerOne
 *
 */
public class LoadReceiver extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param context part (presumably a {@code LoadBearingComponent}) in whose
	 *                context this port will receive the load
	 * @param id      index of this load receiver into a set/array of load receivers
	 * @param name    unique name of the receiver
	 */
	public LoadReceiver(StateBehaviorContext context, Long id, String name)
	{
		super(context, Optional.of(context), id, name);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof LoadSignal)
			result = new SysMLSignalEvent((LoadSignal)signal, "LoadEvent", 0L);
		else
			logger.severe("unrecognized signal type: " + signal.getClass().getSimpleName());
		return result;
	}
}
