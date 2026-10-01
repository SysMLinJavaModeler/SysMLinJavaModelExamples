
package motorinwheel.common.ports.energy;

import motorinwheel.common.signals.MotorTorqueSignal;
import sysmlinjava.attributetypes.TorqueNewtonMeters;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

public class MotorTorqueTransmitPort extends SysMLPort
{
	public MotorTorqueTransmitPort(SysMLPart contextPart, Long id)
	{
		super(contextPart, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if(object instanceof TorqueNewtonMeters torque)
			result = new MotorTorqueSignal(torque);

		
		
		else
			logger.warning("unexpected object type: " + object.getClass().getSimpleName());
		return result;
	}
}
