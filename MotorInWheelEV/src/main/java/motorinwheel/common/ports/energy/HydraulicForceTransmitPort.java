
package motorinwheel.common.ports.energy;

import motorinwheel.common.signals.HydraulicPressureSignal;
import sysmlinjava.attributetypes.ForceNewtonsPerMeterSquare;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

public class HydraulicForceTransmitPort extends SysMLPort
{	
	public HydraulicForceTransmitPort(SysMLPart contextPart, Long id)
	{
		super(contextPart, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if(object instanceof ForceNewtonsPerMeterSquare)
			result = new HydraulicPressureSignal((ForceNewtonsPerMeterSquare)object, id);
		else
			logger.warning("unexpected object type: " + object.getClass().getSimpleName());
		return result;
	}
}
