
package motorinwheel.common.ports.matter;

import motorinwheel.common.signals.AirResistanceSignal;
import sysmlinjava.attributetypes.ForceNewtons;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

public class AirResistanceTransmitPort extends SysMLPort
{	
	public AirResistanceTransmitPort(SysMLPart contextPart, Long id)
	{
		super(contextPart, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if(object instanceof ForceNewtons)
			result = new AirResistanceSignal((ForceNewtons)object, id);
		return result;
	}
}
