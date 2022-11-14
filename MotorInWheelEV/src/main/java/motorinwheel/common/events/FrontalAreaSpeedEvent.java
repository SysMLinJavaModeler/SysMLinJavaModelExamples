package motorinwheel.common.events;

import motorinwheel.common.signals.FrontalArealSpeedSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.valuetypes.FrontalArealSpeed;

/**
 * Event for the occurence of a value of frontal areal speed (a frontal area
 * moving at a given speed)
 * 
 * @author ModelerOne
 *
 */
public class FrontalAreaSpeedEvent extends SysMLSignalEvent
{
	public FrontalAreaSpeedEvent(FrontalArealSpeed areaSpeed)
	{
		super("FrontalArealSpeed");
		((FrontalArealSpeedSignal)signal).frontalArealSpeed.value = areaSpeed.value;
		((FrontalArealSpeedSignal)signal).frontalArealSpeed.speed.value = areaSpeed.speed.value;
	}

	public FrontalArealSpeed getAreaSpeed()
	{
		return ((FrontalArealSpeedSignal)signal).frontalArealSpeed;
	}

	@Override
	public void createSignal()
	{
		signal = new FrontalArealSpeedSignal(new FrontalArealSpeed());
	}
}
