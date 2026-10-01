
package motorinwheel.common.ports.energy;

import motorinwheel.common.signals.ElectricalPowerSignal;
import sysmlinjava.attributetypes.PowerWatts;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

public class ElectricalPowerTransmitPort extends SysMLPort
{	
	public ElectricalPowerTransmitPort(SysMLPart contextPart, Long id)
	{
		super(contextPart, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if(object instanceof PowerWatts)
			result = new ElectricalPowerSignal((PowerWatts)object, id);
		return result;
	}
}
