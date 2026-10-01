package c4s2.common.ports.matter;

import java.util.Optional;
import c4s2.common.signals.StrikeOrdnanceSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

/**
 * Port for the reception of strike ordnance, i.e. getting hit with the bomb
 * 
 * @author ModelerOne
 *
 */
public class StrikeOrdnanceReceiveProtocol extends SysMLPort
{
	public StrikeOrdnanceReceiveProtocol(SysMLPart contextBlock, Long id)
	{
		super(contextBlock, Optional.of(contextBlock), id);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent event = null;
		if (signal instanceof StrikeOrdnanceSignal)
			event = new SysMLSignalEvent(signal, "StrikeOrdnanceEvent", 0L);
		else
			logger.warning("unexpected signal type: " + signal.getClass().getSimpleName());
		return event;
	}
}
