package motorinwheel.common.events;

import motorinwheel.common.signals.MechanicalForceSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.valuetypes.ForceNewtons;

/**
 * Event for the occurence of a value of mechanical force for an identified object
 * 
 * @author ModelerOne
 *
 */
public class MechanicalForceEvent extends SysMLSignalEvent
{
	public MechanicalForceEvent(ForceNewtons force, Long id)
	{
		super("MechanicalForce");
		((MechanicalForceSignal)signal).id = id;
		((MechanicalForceSignal)signal).force = new ForceNewtons(force);
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
