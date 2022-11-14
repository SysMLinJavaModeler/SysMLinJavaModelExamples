package cablestayedbridge.ports;

import sysmlinjava.blocks.SysMLBlock;

/**
 * SysMLinJava full port representation of a ground base interface that receives
 * the load of a bridge pylon. The {@code PylonToGroundLoadReceiver} simply
 * extends the {@code LoadReceiver} for receiving load/weight transmitted from a
 * pylon.
 * 
 * @author ModelerOne
 *
 */
public class PylonToGroundLoadReceiver extends LoadReceiver
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock block (presumably a {@code GroundBase}) in whose context
	 *                     this port will receive the load
	 * @param index        index of this load receiver into a set/array of load
	 *                     receivers
	 * @param name         unique name of the receiver
	 */
	public PylonToGroundLoadReceiver(SysMLBlock contextBlock, Long index, String name)
	{
		super(contextBlock, index, name);
	}
}
