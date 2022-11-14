package motorinwheel.common.ports.energy;

import java.util.Optional;
import motorinwheel.common.events.RoadwayForcesEvent;
import motorinwheel.common.signals.RoadwayForcesSignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port to receive the force (lift, friction) of the roadway surface
 * 
 * @author ModelerOne
 *
 */
public class RoadwaySurfaceForceReceivePort extends SysMLFullPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock      block in whose context the port resides
	 * @param eventContextBlock block to receive the receive events generated by
	 *                          this port
	 * @param id                unique ID
	 */
	public RoadwaySurfaceForceReceivePort(SysMLBlock contextBlock, SysMLBlock eventContextBlock, Long id)
	{
		super(contextBlock, Optional.of(eventContextBlock), id);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof RoadwayForcesSignal)
			result = new RoadwayForcesEvent(((RoadwayForcesSignal)signal).force, id);
		else
			logger.warning("unexpected signal type: " + signal.getClass().getSimpleName());
		return result;
	}
}