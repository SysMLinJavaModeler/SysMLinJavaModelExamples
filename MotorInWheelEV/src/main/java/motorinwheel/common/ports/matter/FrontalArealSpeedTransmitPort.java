package motorinwheel.common.ports.matter;

import motorinwheel.common.signals.FrontalArealSpeedSignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;
import sysmlinjava.valuetypes.FrontalArealSpeed;

public class FrontalArealSpeedTransmitPort extends SysMLFullPort
{
	public FrontalArealSpeedTransmitPort(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}
	@Override
	protected SysMLSignal signalFor(SysMLClass object)
	{
		SysMLSignal result = null;
		if (object instanceof FrontalArealSpeed)
			result = new FrontalArealSpeedSignal((FrontalArealSpeed)object);
		return result;
	}
}
