package hflink.common.events;

import hflink.common.objects.HFPulse;
import hflink.common.signals.HFPulseSignal;
import sysmlinjava.events.SysMLSignalEvent;

/**
 * Event for the receipt of a pulse of HF radio waves
 * 
 * @author ModelerOne
 *
 */
public class HFPulseEvent extends SysMLSignalEvent
{
	public HFPulseEvent(HFPulse hfPulse)
	{
		super("HFPulse");
		((HFPulseSignal)signal).pulse = new HFPulse(hfPulse);
	}

	public HFPulseEvent(HFPulseSignal signal)
	{
		super("HFPulse");
		((HFPulseSignal)signal).pulse = new HFPulse(signal.pulse);
	}

	public HFPulse getView()
	{
	return ((HFPulseSignal)signal).pulse;
	}

	@Override
	public String toString()
	{
		return String.format("HFPulseEvent [signal=%s]", signal);
	}

	@Override
	public void createSignal()
	{
		signal = new HFPulseSignal(new HFPulse());
	}
}
