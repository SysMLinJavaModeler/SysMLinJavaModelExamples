
package motorinwheel.common.signals;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.valuetypes.SpeedKilometersPerHour;

/**
 * Signal for transmission of a speed value to be displayed to the operator
 * 
 * @author ModelerOne
 *
 */
public class SpeedValueDisplaySignal extends SysMLSignal
{
	@Attribute
	public SpeedKilometersPerHour speed;

	public SpeedValueDisplaySignal(SpeedKilometersPerHour kmph)
	{
		super();
		this.speed = kmph;
	}

	@Override
	public String stackNamesString()
	{
		return "SpeedValueDisplay";
	}
}
