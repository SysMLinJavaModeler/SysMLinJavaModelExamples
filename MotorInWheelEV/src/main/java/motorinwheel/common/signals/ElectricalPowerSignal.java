package motorinwheel.common.signals;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.valuetypes.PowerWatts;

/**
 * Signal for transmission of electrical power
 * 
 * @author ModelerOne
 *
 */
public class ElectricalPowerSignal extends SysMLSignal
{
	@Attribute
	public PowerWatts power;
	public Long id;

	public ElectricalPowerSignal(PowerWatts power, Long id)
	{
		super();
		this.power = power;
		this.id = id;
	}

	@Override
	public String stackNamesString()
	{
		return "ElectricalPower";
	}
}
