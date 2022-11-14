
package motorinwheel.common.ports.energy;

import motorinwheel.common.signals.RoadwayForcesSignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;
import sysmlinjava.valuetypes.ForceNewtons;

public class RoadwaySurfaceForceTransmitPort extends SysMLFullPort
{	
	public RoadwaySurfaceForceTransmitPort(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
	{
		SysMLSignal result = null;
		if(object instanceof ForceNewtons)
			result = new RoadwayForcesSignal((ForceNewtons)object, id);
		else
			logger.warning("unexpected object type: " + object.getClass().getSimpleName());
		return result;
	}
}
