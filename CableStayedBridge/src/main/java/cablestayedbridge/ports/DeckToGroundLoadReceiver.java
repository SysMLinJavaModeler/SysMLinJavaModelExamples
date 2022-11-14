package cablestayedbridge.ports;

import sysmlinjava.blocks.SysMLBlock;

/**
 * SysMLinJava full port representation of a ground anchor interface that
 * receives the load of a bridge deck. The {@code DeckToGroundLoadReceiver}
 * simply extends the {@code LoadReceiver} for receiving load/weight transmitted
 * from the end of a bridge deck.
 * 
 * @author ModelerOne
 *
 */
public class DeckToGroundLoadReceiver extends LoadReceiver
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock block (presumably a {@code GroundAnchor}) in whose
	 *                     context this port will receive the load
	 * @param index        index of this load receiver into a set/array of load
	 *                     receivers
	 * @param name         unique name of the receiver
	 */
	public DeckToGroundLoadReceiver(SysMLBlock contextBlock, Long index, String name)
	{
		super(contextBlock, index, name);
	}
}
