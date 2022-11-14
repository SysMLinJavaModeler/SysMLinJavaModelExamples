package c4s2.common.ports.information;

import c4s2.common.objects.information.RadarSignalReturn;
import c4s2.common.signals.RadarSignalReturnSignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port for the transmission of radar signal returns, i.e. for the reflection of
 * radar signals to the radar receiver.
 * 
 * @author ModelerOne
 *
 */
public class RadarSignalReturnTransmitProtocol extends SysMLFullPort
{
	public RadarSignalReturnTransmitProtocol(SysMLBlock parent, Long id)
	{
		super(parent, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
	{
		SysMLSignal result = null;
		if (object instanceof RadarSignalReturn)
			result = new RadarSignalReturnSignal((RadarSignalReturn)object);
		else
			logger.severe("unrecognized object type: " + object.getClass().getSimpleName());
		return result;
	}
}
