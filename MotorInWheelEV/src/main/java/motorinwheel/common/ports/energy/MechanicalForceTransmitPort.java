
package motorinwheel.common.ports.energy;

import motorinwheel.common.signals.MechanicalForceSignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;
import sysmlinjava.valuetypes.ForceNewtons;

public class MechanicalForceTransmitPort extends SysMLFullPort
{	
	public MechanicalForceTransmitPort(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
	{
		SysMLSignal result = null;
		if(object instanceof ForceNewtons)
			result = new MechanicalForceSignal((ForceNewtons)object, id);
		else
			logger.warning("unexpected object type: " + object.getClass().getSimpleName());
		return result;
	}
}
