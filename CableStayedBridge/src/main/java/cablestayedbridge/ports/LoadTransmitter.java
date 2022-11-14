package cablestayedbridge.ports;

import cablestayedbridge.Load;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;

/**
 * SysMLinJava full port representation of a load-bearing component interface
 * that transmits a load (weight, force) to another component's
 * {@code LoadReceiver}.
 * 
 * @author ModelerOne
 *
 */
public class LoadTransmitter extends SysMLFullPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock block (presumably a {@code LoadBearingComponent}) in
	 *                     whose context this port will transmit the load
	 * @param index        index of this load transmitter into a set/array of load
	 *                     trasnmitters
	 * @param name         unique name of the transmitter
	 */
	public LoadTransmitter(SysMLBlock contextBlock, Long index, String name)
	{
		super(contextBlock, index, name);
	}

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
	{
		SysMLSignal result = null;
		if (object instanceof Load)
			result = new LoadSignal((Load)object);
		else
			logger.severe("unrecognized object type: " + object.getClass().getSimpleName());
		return result;
	}
}
