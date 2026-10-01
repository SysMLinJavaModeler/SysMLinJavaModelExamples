package motorinwheel.common.ports.information;

import motorinwheel.common.signals.SpeedValueDisplaySignal;
import sysmlinjava.attributetypes.SpeedKilometersPerHour;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

public class SpeedValueDisplayTransmitPort extends SysMLPort
{
	public SpeedValueDisplayTransmitPort(SysMLPart contextPart, Long id)
	{
		super(contextPart, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof SpeedKilometersPerHour)
			result = new SpeedValueDisplaySignal((SpeedKilometersPerHour)object);
		return result;
	}
}
