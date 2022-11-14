package motorinwheel.common.ports.information;

import java.util.Optional;
import motorinwheel.common.events.ElectronicPulseFrequencyEvent;
import motorinwheel.common.signals.ElectronicPulseFrequencySignal;
import motorinwheel.components.operatordisplays.OperatorDisplays;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.ports.SysMLFullPort;

public class ElectronicPulsesReceivePort extends SysMLFullPort
{
	public ElectronicPulsesReceivePort(SysMLBlock contextBlock, OperatorDisplays eventContextBlock, Long id)
	{
		super(contextBlock, Optional.of(eventContextBlock), id);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof ElectronicPulseFrequencySignal)
			result = new ElectronicPulseFrequencyEvent(((ElectronicPulseFrequencySignal)signal).frequency, id);
		else
			logger.warning("unexpected signal type: " + signal.getClass().getSimpleName());
		return result;
	}
}
