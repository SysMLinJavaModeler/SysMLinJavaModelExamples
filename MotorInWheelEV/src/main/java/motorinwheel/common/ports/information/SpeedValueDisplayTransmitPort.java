package motorinwheel.common.ports.information;

import motorinwheel.common.signals.SpeedValueDisplaySignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;
import sysmlinjava.valuetypes.SpeedKilometersPerHour;

public class SpeedValueDisplayTransmitPort extends SysMLFullPort
{
	public SpeedValueDisplayTransmitPort(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
	{
		SysMLSignal result = null;
		if (object instanceof SpeedKilometersPerHour)
			result = new SpeedValueDisplaySignal((SpeedKilometersPerHour)object);
		return result;
	}
}
