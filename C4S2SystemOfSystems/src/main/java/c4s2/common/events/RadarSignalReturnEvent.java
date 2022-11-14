package c4s2.common.events;

import c4s2.common.objects.information.RadarSignalReturn;
import c4s2.common.signals.RadarSignalReturnSignal;
import sysmlinjava.events.SysMLSignalEvent;

public class RadarSignalReturnEvent extends SysMLSignalEvent
{
	public RadarSignalReturnEvent(RadarSignalReturn reflection)
	{
		super("RadarSignalReturn");
		((RadarSignalReturnSignal)signal).radarReturn = new RadarSignalReturn(reflection);
	}

	public RadarSignalReturn getReflection()
	{
		return ((RadarSignalReturnSignal)signal).radarReturn;
	}

	@Override
	public void createSignal()
	{
		signal = new RadarSignalReturnSignal(new RadarSignalReturn());
		
	}
}
