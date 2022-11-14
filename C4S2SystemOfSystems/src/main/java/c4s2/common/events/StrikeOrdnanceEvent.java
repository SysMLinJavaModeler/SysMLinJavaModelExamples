package c4s2.common.events;

import c4s2.common.objects.information.StrikeOrdnance;
import c4s2.common.signals.StrikeOrdnanceSignal;
import sysmlinjava.events.SysMLSignalEvent;

public class StrikeOrdnanceEvent extends SysMLSignalEvent
{
	public StrikeOrdnanceEvent(StrikeOrdnance ordnance)
	{
		super("StrikeOrdnance");
		((StrikeOrdnanceSignal)signal).ordnance = new StrikeOrdnance(ordnance);
	}

	@Override
	public void createSignal()
	{
		signal = new StrikeOrdnanceSignal(new StrikeOrdnance());
	}
	
}
