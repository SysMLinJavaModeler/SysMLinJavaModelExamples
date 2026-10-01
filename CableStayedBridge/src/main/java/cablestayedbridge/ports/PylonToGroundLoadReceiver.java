package cablestayedbridge.ports;

import sysmlinjava.parts.SysMLPart;

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
	 * @param context block (presumably a {@code GroundBase}) in whose context
	 *                     this port will receive the load
	 * @param index        index of this load receiver into a set/array of load
	 *                     receivers
	 * @param name         unique name of the receiver
	 */
	public PylonToGroundLoadReceiver(SysMLPart context, Long index, String name)
	{
		super(context, index, name);
	}
}
