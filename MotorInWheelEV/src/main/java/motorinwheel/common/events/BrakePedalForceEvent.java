package motorinwheel.common.events;

import motorinwheel.common.signals.BrakePedalForceSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.valuetypes.ForceNewtons;

/**
 * Event for the occurence of a value of force on the brake pedal
 * 
 * @author ModelerOne
 *
 */
public class BrakePedalForceEvent extends SysMLSignalEvent
{
	public BrakePedalForceEvent(ForceNewtons force, Long id)
	{
		super("BrakePedalForce");
		((BrakePedalForceSignal)signal).id = id;
		((BrakePedalForceSignal)signal).force.value = force.value;
	}

	public ForceNewtons getForce()
	{
		return ((BrakePedalForceSignal)signal).force;
	}

	@Override
	public void createSignal()
	{
		signal = new BrakePedalForceSignal(new ForceNewtons(), 0L);
	}
}
