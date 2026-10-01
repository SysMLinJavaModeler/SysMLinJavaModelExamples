package cablestayedbridge.ports;

import cablestayedbridge.Load;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

/**
 * SysMLinJava full port representation of a load-bearing component interface
 * that transmits a load (weight, force) to another component's
 * {@code LoadReceiver}.
 * 
 * @author ModelerOne
 *
 */
public class LoadTransmitter extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param context block (presumably a {@code LoadBearingComponent}) in
	 *                     whose context this port will transmit the load
	 * @param index        index of this load transmitter into a set/array of load
	 *                     trasnmitters
	 * @param name         unique name of the transmitter
	 */
	public LoadTransmitter(SysMLPart context, Long index, String name)
	{
		super(context, index, name);
	}

	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof Load)
			result = new LoadSignal((Load)object);
		else
			logger.severe("unrecognized object type: " + object.getClass().getSimpleName());
		return result;
	}
}
