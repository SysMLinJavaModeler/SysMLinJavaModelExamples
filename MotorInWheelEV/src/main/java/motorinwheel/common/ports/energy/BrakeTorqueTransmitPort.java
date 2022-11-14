
package motorinwheel.common.ports.energy;

import motorinwheel.common.signals.BrakeTorqueSignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;
import sysmlinjava.valuetypes.TorqueNewtonMeters;

public class BrakeTorqueTransmitPort extends SysMLFullPort
{		
	public BrakeTorqueTransmitPort(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
	{
		SysMLSignal result = null;
		if(object instanceof TorqueNewtonMeters)
			result = new BrakeTorqueSignal((TorqueNewtonMeters)object);
		return result;
	}
}
