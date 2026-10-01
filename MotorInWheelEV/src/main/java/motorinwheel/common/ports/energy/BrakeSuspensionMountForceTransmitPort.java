
package motorinwheel.common.ports.energy;

import motorinwheel.common.signals.MechanicalForceSignal;
import sysmlinjava.attributetypes.ForceNewtons;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

public class BrakeSuspensionMountForceTransmitPort extends SysMLPort
{	
	public BrakeSuspensionMountForceTransmitPort(SysMLPart contextPart, Long id)
	{
		super(contextPart, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if(object instanceof ForceNewtons)
			result = new MechanicalForceSignal((ForceNewtons)object, id);
		else
			logger.warning("unexpected object type: " + object.getClass().getSimpleName());
		return result;
	}
}
