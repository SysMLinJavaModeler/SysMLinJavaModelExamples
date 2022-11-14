package cablestayedbridge.ports;

import java.util.Optional;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.ports.SysMLFullPort;

/**
 * SysMLinJava full port representation of a load-bearing component interface
 * that receives a load (weight, force) from another component's
 * {@code LoadTransmitter}.
 * 
 * @author ModelerOne
 *
 */
public class LoadReceiver extends SysMLFullPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock block (presumably a {@code LoadBearingComponent}) in
	 *                     whose context this port will receive the load
	 * @param id        index of this load receiver into a set/array of load
	 *                     receivers
	 * @param name         unique name of the receiver
	 */
	public LoadReceiver(SysMLBlock contextBlock, Long id, String name)
	{
		super(contextBlock, Optional.of(contextBlock), id, name);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof LoadSignal)
			result = new LoadSignalEvent((LoadSignal)signal);
		else
			logger.severe("unrecognized signal type: " + signal.getClass().getSimpleName());
		return result;
	}
}
