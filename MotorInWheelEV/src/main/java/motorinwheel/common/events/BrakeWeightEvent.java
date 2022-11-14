package motorinwheel.common.events;

import motorinwheel.common.signals.MechanicalForceSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.valuetypes.ForceNewtons;

/**
 * Event for the occurence of a value of weight force by a brake device
 * 
 * @author ModelerOne
 *
 */
public class BrakeWeightEvent extends SysMLSignalEvent
{
	public BrakeWeightEvent(ForceNewtons force, Long id)
	{
		super("BrakeWeight");
		((MechanicalForceSignal)signal).id = id;
		((MechanicalForceSignal)signal).force.value = force.value;
	}

	public ForceNewtons getForce()
	{
		return ((MechanicalForceSignal)signal).force;
	}

	public Long getID()
	{
		return ((MechanicalForceSignal)signal).id;
	}

	@Override
	public void createSignal()
	{
		signal = new MechanicalForceSignal(new ForceNewtons(), 0L);
	}
}
