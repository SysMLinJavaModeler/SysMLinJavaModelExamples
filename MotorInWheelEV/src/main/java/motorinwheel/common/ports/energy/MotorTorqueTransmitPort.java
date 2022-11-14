
package motorinwheel.common.ports.energy;

import motorinwheel.common.signals.MotorTorqueSignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;
import sysmlinjava.valuetypes.TorqueNewtonMeters;

public class MotorTorqueTransmitPort extends SysMLFullPort
{	
	public MotorTorqueTransmitPort(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
	{
		SysMLSignal result = null;
		if(object instanceof TorqueNewtonMeters)
			result = new MotorTorqueSignal((TorqueNewtonMeters)object);

		
		
		else
			logger.warning("unexpected object type: " + object.getClass().getSimpleName());
		return result;
	}
}
