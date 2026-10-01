
package motorinwheel.common.ports.energy;

import motorinwheel.common.signals.BrakeTorqueSignal;
import sysmlinjava.attributetypes.TorqueNewtonMeters;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

public class BrakeTorqueTransmitPort extends SysMLPort
{		
	public BrakeTorqueTransmitPort(SysMLPart contextPart, Long id)
	{
		super(contextPart, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if(object instanceof TorqueNewtonMeters)
			result = new BrakeTorqueSignal((TorqueNewtonMeters)object);
		return result;
	}
}
