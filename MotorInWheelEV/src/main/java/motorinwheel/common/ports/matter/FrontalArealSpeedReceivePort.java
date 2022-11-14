package motorinwheel.common.ports.matter;

import java.util.Optional;
import motorinwheel.common.events.FrontalAreaSpeedEvent;
import motorinwheel.common.signals.FrontalArealSpeedSignal;
import motorinwheel.systems.atmosphere.Atmosphere;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.ports.SysMLFullPort;

public class FrontalArealSpeedReceivePort extends SysMLFullPort
{
	public FrontalArealSpeedReceivePort(SysMLBlock contextBlock, Atmosphere eventContextBlock, Long id)
	{
		super(contextBlock, Optional.of(eventContextBlock), id);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if(signal instanceof FrontalArealSpeedSignal)
			result = new FrontalAreaSpeedEvent(((FrontalArealSpeedSignal)signal).frontalArealSpeed);
		return result;
	}
}
