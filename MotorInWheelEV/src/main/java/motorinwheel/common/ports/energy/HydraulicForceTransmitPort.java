
package motorinwheel.common.ports.energy;

import motorinwheel.common.signals.HydraulicPressureSignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;
import sysmlinjava.valuetypes.ForceNewtonsPerMeterSquare;

public class HydraulicForceTransmitPort extends SysMLFullPort
{	
	public HydraulicForceTransmitPort(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
	{
		SysMLSignal result = null;
		if(object instanceof ForceNewtonsPerMeterSquare)
			result = new HydraulicPressureSignal((ForceNewtonsPerMeterSquare)object, id);
		else
			logger.warning("unexpected object type: " + object.getClass().getSimpleName());
		return result;
	}
}
