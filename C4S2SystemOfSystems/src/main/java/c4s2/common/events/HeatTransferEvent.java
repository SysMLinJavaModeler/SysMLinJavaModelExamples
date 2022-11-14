package c4s2.common.events;

import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.valuetypes.HeatWatts;
import sysmlinjavalibrary.common.signals.HeatSignal;

public class HeatTransferEvent extends SysMLSignalEvent
{
	public HeatTransferEvent(HeatWatts heat)
	{
		super("HeatTransfer");
		((HeatSignal)signal).heat.value = heat.value;
	}

	@Override
	public void createSignal()
	{
		signal = new HeatSignal(new HeatWatts(0));
	}
}
