package motorinwheel.common.ports.information;

import java.util.Optional;
import motorinwheel.common.events.SpeedValueDisplayEvent;
import motorinwheel.common.signals.SpeedValueDisplaySignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.ports.SysMLFullPort;

public class SpeedValueDisplayReceivePort extends SysMLFullPort
{
	public SpeedValueDisplayReceivePort(SysMLBlock contextBlock, SysMLBlock eventContextBlock, Long id)
	{
		super(contextBlock, Optional.of(eventContextBlock), id);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if(signal instanceof SpeedValueDisplaySignal)
			result = new SpeedValueDisplayEvent(((SpeedValueDisplaySignal)signal).speed);
		return result;
	}
}
