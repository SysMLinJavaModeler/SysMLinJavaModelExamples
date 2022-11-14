package motorinwheel.common.events;

import motorinwheel.common.signals.RoadwayForcesSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.valuetypes.ForceNewtons;

/**
 * Event for the occurence of a value of force (lift, friction) exerted by a roadway
 * 
 * @author ModelerOne
 *
 */
public class RoadwayForcesEvent extends SysMLSignalEvent
{
	public RoadwayForcesEvent(ForceNewtons force, Long id)
	{
		super("RoadwayForce" + id);
		((RoadwayForcesSignal)signal).force.value = force.value;
		((RoadwayForcesSignal)signal).location = id;
	}

	public ForceNewtons getForce()
	{
		return ((RoadwayForcesSignal)signal).force;
	}

	@Override
	public void createSignal()
	{
		signal = new RoadwayForcesSignal(new ForceNewtons(), 0L);
	}
}
