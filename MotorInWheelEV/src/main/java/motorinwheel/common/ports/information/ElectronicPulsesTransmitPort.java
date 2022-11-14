package motorinwheel.common.ports.information;

import motorinwheel.common.signals.ElectronicPulseFrequencySignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;
import sysmlinjava.valuetypes.FrequencyHertz;

public class ElectronicPulsesTransmitPort extends SysMLFullPort
{
	public ElectronicPulsesTransmitPort(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
	{
		SysMLSignal result = null;
		if (object instanceof FrequencyHertz)
			result = new ElectronicPulseFrequencySignal(((FrequencyHertz)object), id);
		else
			logger.warning("unexpected object type: " + object.getClass().getSimpleName());
		return result;
	}
}
