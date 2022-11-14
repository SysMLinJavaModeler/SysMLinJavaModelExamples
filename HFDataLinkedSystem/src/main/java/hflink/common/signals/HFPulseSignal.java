package hflink.common.signals;

import hflink.common.objects.HFPulse;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

/**
 * Signal for an HF radiowave pulse
 * 
 * @author ModelerOne
 *
 */
public class HFPulseSignal extends SysMLSignal
{
	@Attribute
	public HFPulse pulse;

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
