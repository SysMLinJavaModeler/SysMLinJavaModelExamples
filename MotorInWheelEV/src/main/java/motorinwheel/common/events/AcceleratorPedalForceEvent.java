package motorinwheel.common.events;

import motorinwheel.common.signals.AcceleratorPedalForceSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.valuetypes.ForceNewtons;

/**
 * Event for the occurence of a value of force on the accelerator pedal
 * 
 * @author ModelerOne
 *
 */
public class AcceleratorPedalForceEvent extends SysMLSignalEvent
{
	/**
	 * Constructor
	 * 
	 * @param force value of force on the pedal
	 * @param id    unique ID
	 */
	public AcceleratorPedalForceEvent(ForceNewtons force, Long id)
	{
		super("AcceleratorPedalForce");
		((AcceleratorPedalForceSignal)signal).id = id;
		((AcceleratorPedalForceSignal)signal).force.value = force.value;
	}

	public ForceNewtons getForce()
	{
		return ((AcceleratorPedalForceSignal)signal).force;
	}

	@Override
	public void createSignal()
	{
		signal = new AcceleratorPedalForceSignal(new ForceNewtons(), 0L);
	}
}
