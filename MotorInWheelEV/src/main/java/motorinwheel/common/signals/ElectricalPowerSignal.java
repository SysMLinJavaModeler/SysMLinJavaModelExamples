package motorinwheel.common.signals;

import sysmlinjava.attributetypes.PowerWatts;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

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
