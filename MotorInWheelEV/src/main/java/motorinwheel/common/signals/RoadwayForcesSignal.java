package motorinwheel.common.signals;

import sysmlinjava.attributetypes.ForceNewtons;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

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
