package hflink.common.signals;

import hflink.common.items.HFPulse;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal for a pulse of HF radio wave. Simulates a "burst" of information in
 * the form of a phase-shift keyed signal carried over an HF radio wave of short
 * duration
 * 
 * @author ModelerOne
 *
 */
public class HFPulseSignal extends SysMLSignal
{
	/**
	 * HF pulse value
	 */
	@Attribute
	public HFPulse pulse;

	/**
	 * Constructor for specified HF pulse value
	 * 
	 * @param pulse HF pulse value
	 */
	public HFPulseSignal(HFPulse pulse)
	{
		super("HFPulse", 0L);
		this.pulse = pulse;
	}

	@Override
	public String stackNamesString()
	{
		return pulse.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("HFPulseSignal [pulse=%s]", pulse);
	}

}
