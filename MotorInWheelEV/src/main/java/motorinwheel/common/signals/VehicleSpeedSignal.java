package motorinwheel.common.signals;

import java.util.Optional;
import sysmlinjava.attributetypes.SpeedKilometersPerHour;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal for transmission of a vehicle's speed value *
 * 
 * @author ModelerOne
 *
 */
public class VehicleSpeedSignal extends SysMLSignal
{
	@Attribute
	public SpeedKilometersPerHour speed;

	public VehicleSpeedSignal(SpeedKilometersPerHour kmph)
	{
		super();
		this.speed = kmph;
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.empty());
	}
}
