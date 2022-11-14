package hflink.common.events;

import hflink.common.objects.GPSMessage;
import hflink.common.signals.GPSMessageSignal;
import sysmlinjava.events.SysMLSignalEvent;

/**
 * Event for the receipt of a GPS message
 * 
 * @author ModelerOne
 *
 */
public class GPSMessageEvent extends SysMLSignalEvent
{
	public GPSMessageEvent(GPSMessage gpsMessage)
	{
		super("GPSMessage");
		((GPSMessageSignal)signal).message = new GPSMessage(gpsMessage);
	}

	public GPSMessageEvent(GPSMessageSignal signal)
	{
		super("GPSMessage");
		((GPSMessageSignal)signal).message = new GPSMessage(signal.message);
	}

	public GPSMessage getMessage()
	{
		return ((GPSMessageSignal)signal).message;
	}

	@Override
	public String toString()
	{
		return String.format("GPSMessageEvent [signal=%s]", signal);
	}

	@Override
	public void createSignal()
	{
		signal = new GPSMessageSignal(new GPSMessage());
	}
}
