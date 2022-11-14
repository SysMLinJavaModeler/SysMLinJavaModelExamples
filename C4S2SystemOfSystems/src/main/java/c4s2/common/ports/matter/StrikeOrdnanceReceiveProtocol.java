package c4s2.common.ports.matter;

import java.util.Optional;
import c4s2.common.events.StrikeOrdnanceEvent;
import c4s2.common.signals.StrikeOrdnanceSignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port for the reception of strike ordnance, i.e. getting hit with the bomb
 * 
 * @author ModelerOne
 *
 */
public class StrikeOrdnanceReceiveProtocol extends SysMLFullPort
{
	public StrikeOrdnanceReceiveProtocol(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, Optional.of(contextBlock), id);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent event = null;
		if (signal instanceof StrikeOrdnanceSignal)
			event = new StrikeOrdnanceEvent(((StrikeOrdnanceSignal)signal).ordnance);
		else
			logger.warning("unexpected signal type: " + signal.getClass().getSimpleName());
		return event;
	}
}
