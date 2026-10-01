package cablestayedbridge.ports;

import sysmlinjava.parts.SysMLPart;

/**
 * SysMLinJava full port representation of a pylon's saddle interface that
 * receives the load of a bridge cable. The {@code CableToPylonLoadReceiver}
 * simply extends the {@code LoadReceiver} for receiving load/weight transmitted
 * from a cablen.
 * 
 * @author ModelerOne
 *
 */
public class CableToPylonLoadReceiver extends LoadReceiver
{
	/**
	 * Constructor
	 * 
	 * @param context block (presumably a {@code Pylon}) in whose context this
	 *                     port will receive the load
	 * @param index        index of this load receiver into a set/array of load
	 *                     receivers
	 * @param name         unique name of the receiver
	 */
	public CableToPylonLoadReceiver(SysMLPart context, Long index, String name)
	{
		super(context, index, name);
	}
}
