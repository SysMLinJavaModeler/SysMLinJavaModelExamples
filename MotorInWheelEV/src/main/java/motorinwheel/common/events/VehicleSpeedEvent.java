package motorinwheel.common.events;

import motorinwheel.common.signals.VehicleSpeedSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.valuetypes.SpeedKilometersPerHour;

/**
 * Event for the occurence of a value of speed of a vehicle
 * 
 * @author ModelerOne
 *
 */
public class VehicleSpeedEvent extends SysMLSignalEvent
{
	public VehicleSpeedEvent(SpeedKilometersPerHour speed)
	{
		super("VehicleSpeed");
		((VehicleSpeedSignal)signal).speed.value = speed.value;
	}

	@Override
	public void createSignal()
	{
		signal = new VehicleSpeedSignal(new SpeedKilometersPerHour());
	}
}
