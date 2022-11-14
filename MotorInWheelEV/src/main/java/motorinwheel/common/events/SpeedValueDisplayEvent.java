package motorinwheel.common.events;

import motorinwheel.common.signals.SpeedValueDisplaySignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.valuetypes.SpeedKilometersPerHour;

/**
 * Event for the occurence of a value of speed to be displayed to an operator
 * 
 * @author ModelerOne
 *
 */
public class SpeedValueDisplayEvent extends SysMLSignalEvent
{
	public SpeedValueDisplayEvent(SpeedKilometersPerHour speed)
	{
		super("SpeedValueDisplay");
		((SpeedValueDisplaySignal)signal).speed.value = speed.value;
	}

	public SpeedKilometersPerHour getSpeed()
	{
		return ((SpeedValueDisplaySignal)signal).speed;
	}

	@Override
	public void createSignal()
	{
		signal = new SpeedValueDisplaySignal(new SpeedKilometersPerHour());
	}
}
