package motorinwheel.common.ports.energy;

import java.util.Optional;
import motorinwheel.common.events.BrakeTorqueEvent;
import motorinwheel.common.signals.BrakeTorqueSignal;
import motorinwheel.components.wheel.Wheel;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port to receive the torque on the wheel from the brake
 * 
 * @author ModelerOne
 *
 */
public class BrakeTorqueReceivePort extends SysMLFullPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock      block in whose context the port resides
	 * @param eventContextBlock block to receive the receive events generated by
	 *                          this port
	 * @param id                unique ID
	 */
	public BrakeTorqueReceivePort(SysMLBlock contextBlock, Wheel eventContextBlock, Long id)
	{
		super(contextBlock, Optional.of(eventContextBlock), id);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof BrakeTorqueSignal)
			result = new BrakeTorqueEvent(((BrakeTorqueSignal)signal).torque);
		else
			logger.warning("unexpected signal type: " + signal.getClass().getSimpleName());
		return result;
	}
}