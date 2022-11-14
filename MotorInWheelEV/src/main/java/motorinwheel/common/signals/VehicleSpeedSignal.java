package motorinwheel.common.signals;

import java.util.Optional;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.valuetypes.SpeedKilometersPerHour;

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
