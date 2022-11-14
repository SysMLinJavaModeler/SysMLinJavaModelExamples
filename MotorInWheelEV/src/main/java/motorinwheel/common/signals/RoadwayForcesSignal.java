package motorinwheel.common.signals;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.valuetypes.ForceNewtons;

/**
 * Signal for transmission of roadway forces on a located wheel
 * 
 * @author ModelerOne
 *
 */
public class RoadwayForcesSignal extends SysMLSignal
{
	@Attribute
	public ForceNewtons force;
	@Attribute
	public Long location;

	public RoadwayForcesSignal(ForceNewtons force, Long location)
	{
		super();
		this.force = force;
		this.location = location;
	}

	@Override
	public String stackNamesString()
	{
		return "RoadwayForces";
	}
}
