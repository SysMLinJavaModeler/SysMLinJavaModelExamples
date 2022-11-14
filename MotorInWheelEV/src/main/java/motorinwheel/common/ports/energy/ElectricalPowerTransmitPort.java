
package motorinwheel.common.ports.energy;

import motorinwheel.common.signals.ElectricalPowerSignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;
import sysmlinjava.valuetypes.PowerWatts;

public class ElectricalPowerTransmitPort extends SysMLFullPort
{	
	public ElectricalPowerTransmitPort(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
	{
		SysMLSignal result = null;
		if(object instanceof PowerWatts)
			result = new ElectricalPowerSignal((PowerWatts)object, id);
		return result;
	}
}
