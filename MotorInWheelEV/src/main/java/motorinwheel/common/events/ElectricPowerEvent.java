package motorinwheel.common.events;

import motorinwheel.common.signals.ElectricalPowerSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.valuetypes.PowerWatts;

/**
 * Event for the occurence of a value of electrical power supplied
 * 
 * @author ModelerOne
 *
 */
public class ElectricPowerEvent extends SysMLSignalEvent
{
	public ElectricPowerEvent(PowerWatts power)
	{
		super("ElectricPower");
		((ElectricalPowerSignal)signal).power.value = power.value;
	}

	public PowerWatts getPower()
	{
		return ((ElectricalPowerSignal)signal).power;
	}

	@Override
	public void createSignal()
	{
		signal = new ElectricalPowerSignal(new PowerWatts(), 0L);
	}
}
