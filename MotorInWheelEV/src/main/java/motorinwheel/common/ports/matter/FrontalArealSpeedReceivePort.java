package motorinwheel.common.ports.matter;

import java.util.Optional;
import motorinwheel.common.signals.FrontalArealSpeedSignal;
import motorinwheel.systems.atmosphere.Atmosphere;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

public class FrontalArealSpeedReceivePort extends SysMLPort
{
	public FrontalArealSpeedReceivePort(SysMLPart contextPart, Atmosphere eventContextPart, Long id)
	{
		super(contextPart, Optional.of(eventContextPart), id);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if(signal instanceof FrontalArealSpeedSignal)
			result = new SysMLSignalEvent(signal, "FrontalArealSpeed", 0L);
		return result;
	}
}
