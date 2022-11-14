
package motorinwheel.common.ports.energy;

import motorinwheel.common.signals.AcceleratorPedalForceSignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;
import sysmlinjava.valuetypes.ForceNewtons;

public class AcceleratorPedalForceTransmitPort extends SysMLFullPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock block in whose context the port resides
	 * @param id           unique ID
	 */
	public AcceleratorPedalForceTransmitPort(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
	{
		SysMLSignal result = null;
		if (object instanceof ForceNewtons)
			result = new AcceleratorPedalForceSignal((ForceNewtons)object, id);
		else
			logger.warning("unexpected object type: " + object.getClass().getSimpleName());
		return result;
	}
}
