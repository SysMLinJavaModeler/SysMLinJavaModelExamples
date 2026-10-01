
package motorinwheel.common.signals;

import sysmlinjava.attributetypes.SpeedKilometersPerHour;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

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
