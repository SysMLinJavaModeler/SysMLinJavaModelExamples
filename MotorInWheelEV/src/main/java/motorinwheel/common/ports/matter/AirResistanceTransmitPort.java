
package motorinwheel.common.ports.matter;

import motorinwheel.common.signals.AirResistanceSignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;
import sysmlinjava.valuetypes.ForceNewtons;

public class AirResistanceTransmitPort extends SysMLFullPort
{	
	public AirResistanceTransmitPort(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
	{
		SysMLSignal result = null;
		if(object instanceof ForceNewtons)
			result = new AirResistanceSignal((ForceNewtons)object, id);
		return result;
	}
}
