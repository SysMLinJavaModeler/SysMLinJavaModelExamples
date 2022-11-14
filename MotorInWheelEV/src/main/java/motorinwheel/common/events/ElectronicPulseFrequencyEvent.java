package motorinwheel.common.events;

import motorinwheel.common.signals.ElectronicPulseFrequencySignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.valuetypes.FrequencyHertz;

/**
 * Event for the occurence of a value of frequency of an electronic pulse
 * 
 * @author ModelerOne
 *
 */
public class ElectronicPulseFrequencyEvent extends SysMLSignalEvent
{
	public ElectronicPulseFrequencyEvent(FrequencyHertz frequency, Long id)
	{
		super("ElectronicPulseFrequency");
		((ElectronicPulseFrequencySignal)signal).id = id;
		((ElectronicPulseFrequencySignal)signal).frequency.value = frequency.value;
	}

	public FrequencyHertz getFrequency()
	{
		return ((ElectronicPulseFrequencySignal)signal).frequency;
	}

	public Long getID()
	{
		return ((ElectronicPulseFrequencySignal)signal).id;
	}

	@Override
	public void createSignal()
	{
		signal = new ElectronicPulseFrequencySignal(new FrequencyHertz(), 0L);
	}
}
