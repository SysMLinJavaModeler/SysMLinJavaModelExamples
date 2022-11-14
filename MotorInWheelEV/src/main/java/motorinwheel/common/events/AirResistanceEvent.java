package motorinwheel.common.events;

import motorinwheel.common.signals.AirResistanceSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.valuetypes.ForceNewtons;

/**
 * Event for the occurence of a value of force of air resisance
 * 
 * @author ModelerOne
 *
 */
public class AirResistanceEvent extends SysMLSignalEvent
{
	public AirResistanceEvent(ForceNewtons force, Long id)
	{
		super("AirResistance");
		((AirResistanceSignal)signal).id = id;
		((AirResistanceSignal)signal).force.value = force.value;
	}

	public ForceNewtons getForce()
	{
		return ((AirResistanceSignal)signal).force;
	}

	@Override
	public void createSignal()
	{
		signal = new AirResistanceSignal(new ForceNewtons(0), 0L);
	}
}
