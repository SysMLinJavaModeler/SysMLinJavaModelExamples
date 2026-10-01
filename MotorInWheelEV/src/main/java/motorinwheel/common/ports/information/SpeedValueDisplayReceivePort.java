package motorinwheel.common.ports.information;

import java.util.Optional;
import motorinwheel.common.signals.SpeedValueDisplaySignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

public class SpeedValueDisplayReceivePort extends SysMLPort
{
	public SpeedValueDisplayReceivePort(SysMLPart contextPart, SysMLPart eventContextPart, Long id)
	{
		super(contextPart, Optional.of(eventContextPart), id);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if(signal instanceof SpeedValueDisplaySignal)
			result = new SysMLSignalEvent(signal, "SpeedValueDisplay", 0L);
		return result;
	}
}
