package motorinwheel.common.signals;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.valuetypes.ForceNewtons;

/**
 * Signal for transmission of air resistance force
 * 
 * @author ModelerOne
 *
 */
public class AirResistanceSignal extends SysMLSignal
{
	@Attribute
	public ForceNewtons force;
	@Attribute
	public Long id;

	public AirResistanceSignal(ForceNewtons force, Long id)
	{
		super();
		this.force = force;
		this.id = id;
	}

	@Override
	public String stackNamesString()
	{
		return "AirResistance";
	}
}
