package motorinwheel.common.signals;

import sysmlinjava.attributetypes.FrequencyHertz;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal for transmission of an electronic pulse of a specified frequency
 * 
 * @author ModelerOne
 *
 */
public class ElectronicPulseFrequencySignal extends SysMLSignal
{
	@Attribute
	public FrequencyHertz frequency;
	@Attribute
	public Long id;

	public ElectronicPulseFrequencySignal(FrequencyHertz frequency, Long id)
	{
		super();
		this.frequency = frequency;
		this.id = id;
	}

	@Override
	public String stackNamesString()
	{
		return "ElectronicPulseFrequency";
	}
}
