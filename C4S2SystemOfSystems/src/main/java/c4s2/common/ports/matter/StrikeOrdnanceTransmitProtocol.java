package c4s2.common.ports.matter;

import c4s2.common.objects.information.StrikeOrdnance;
import c4s2.common.signals.StrikeOrdnanceSignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port for the transmission of strike ordnance, i.e. firing/releasing the bomb
 * 
 * @author ModelerOne
 *
 */
public class StrikeOrdnanceTransmitProtocol extends SysMLFullPort
{
	public StrikeOrdnanceTransmitProtocol(SysMLBlock parent, Long id)
	{
		super(parent, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
	{
		StrikeOrdnanceSignal result = null;
		if(object instanceof StrikeOrdnance strikeOrdnance)
			result = new StrikeOrdnanceSignal(strikeOrdnance);
		else
			logger.severe("unrecognized object type:" + object.getClass().getSimpleName());
		return result;
	}
}
