package c4s2.common.events;

import c4s2.common.objects.information.RadarSignalTransmission;
import c4s2.common.signals.RadarSignalTransmissionSignal;
import sysmlinjava.events.SysMLSignalEvent;

public class RadarSignalTransmissionEvent extends SysMLSignalEvent
{
	public RadarSignalTransmissionEvent(RadarSignalTransmission transmission)
	{
		super("RadarSignalTransmission");
		((RadarSignalTransmissionSignal)signal).transmission = new RadarSignalTransmission(transmission);
	}

	@Override
	public void createSignal()
	{
		signal = new RadarSignalTransmissionSignal(new RadarSignalTransmission());
	}
}
