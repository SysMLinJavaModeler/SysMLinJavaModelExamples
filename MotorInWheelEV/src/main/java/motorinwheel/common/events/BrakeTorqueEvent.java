package motorinwheel.common.events;

import motorinwheel.common.signals.BrakeTorqueSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.valuetypes.TorqueNewtonMeters;

/**
 * Event for the occurence of a value of torque generated by a brake
 * 
 * @author ModelerOne
 *
 */
public class BrakeTorqueEvent extends SysMLSignalEvent
{
	public BrakeTorqueEvent(TorqueNewtonMeters torque)
	{
		super("BrakeTorque");
		((BrakeTorqueSignal)signal).torque.value = torque.value;
	}

	public TorqueNewtonMeters getTorque()
	{
		return ((BrakeTorqueSignal)signal).torque;
	}

	@Override
	public void createSignal()
	{
		signal = new BrakeTorqueSignal(new TorqueNewtonMeters());
		
	}
}
