package motorinwheel.common.ports.matter;

import motorinwheel.common.signals.FrontalArealSpeedSignal;
import sysmlinjava.attributetypes.FrontalArealSpeed;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

public class FrontalArealSpeedTransmitPort extends SysMLPort
{
	public FrontalArealSpeedTransmitPort(SysMLPart contextPart, Long id)
	{
		super(contextPart, id);
	}
	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof FrontalArealSpeed)
			result = new FrontalArealSpeedSignal((FrontalArealSpeed)object);
		return result;
	}
}
