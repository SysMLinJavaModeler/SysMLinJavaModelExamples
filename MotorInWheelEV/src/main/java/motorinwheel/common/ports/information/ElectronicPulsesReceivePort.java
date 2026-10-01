package motorinwheel.common.ports.information;

import java.util.Optional;
import motorinwheel.common.signals.ElectronicPulseFrequencySignal;
import motorinwheel.components.operatordisplays.OperatorDisplays;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

public class ElectronicPulsesReceivePort extends SysMLPort
{
	public ElectronicPulsesReceivePort(SysMLPart contextPart, OperatorDisplays eventContextPart, Long id)
	{
		super(contextPart, Optional.of(eventContextPart), id);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof ElectronicPulseFrequencySignal)
			result = new SysMLSignalEvent(signal, "ElectronicPulse", 0L);
		else
			logger.warning("unexpected signal type: " + signal.getClass().getSimpleName());
		return result;
	}
}
