package motorinwheel.common.events;

import motorinwheel.common.signals.MechanicalForceSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.valuetypes.ForceNewtons;

/**
 * Event for the occurence of a value of weight force by a motor device
 * 
 * @author ModelerOne
 *
 */
public class MotorWeightEvent extends SysMLSignalEvent
{
	public MotorWeightEvent(ForceNewtons force, Long id)
	{
		super("MotorWeight");
		((MechanicalForceSignal)signal).force.value = force.value;
		((MechanicalForceSignal)signal).id = id;
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
