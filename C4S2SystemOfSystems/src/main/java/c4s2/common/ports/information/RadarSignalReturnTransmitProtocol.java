package c4s2.common.ports.information;

import c4s2.common.items.information.RadarSignalReturn;
import c4s2.common.signals.RadarSignalReturnSignal;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

/**
 * Port for the transmission of radar signal returns, i.e. for the reflection of
 * radar signals to the radar receiver.
 * 
 * @author ModelerOne
 *
 */
public class RadarSignalReturnTransmitProtocol extends SysMLPort
{
	public RadarSignalReturnTransmitProtocol(SysMLPart parent, Long id)
	{
		super(parent, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof RadarSignalReturn)
			result = new RadarSignalReturnSignal((RadarSignalReturn)object);
		else
			logger.severe("unrecognized object type: " + object.getClass().getSimpleName());
		return result;
	}
}
