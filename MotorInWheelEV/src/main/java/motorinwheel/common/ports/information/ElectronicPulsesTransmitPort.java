package motorinwheel.common.ports.information;

import motorinwheel.common.signals.ElectronicPulseFrequencySignal;
import sysmlinjava.attributetypes.FrequencyHertz;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

public class ElectronicPulsesTransmitPort extends SysMLPort
{
	public ElectronicPulsesTransmitPort(SysMLPart contextPart, Long id)
	{
		super(contextPart, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof FrequencyHertz)
			result = new ElectronicPulseFrequencySignal(((FrequencyHertz)object), id);
		else
			logger.warning("unexpected object type: " + object.getClass().getSimpleName());
		return result;
	}
}
