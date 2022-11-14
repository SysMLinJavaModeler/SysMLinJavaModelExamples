package cablestayedbridge.ports;

import sysmlinjava.blocks.SysMLBlock;

/**
 * SysMLinJava full port representation of a cable interface that receives the
 * load of a section of a bridge deck. The {@code DeckToCableLoadReceiver}
 * simply extends the {@code LoadReceiver} for receiving load/weight transmitted
 * from a section of the deck.
 * 
 * @author ModelerOne
 *
 */
public class DeckToCableLoadReceiver extends LoadReceiver
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock block (presumably a {@code Cable}) in whose context this
	 *                     port will receive the load
	 * @param index        index of this load receiver into a set/array of load
	 *                     receivers
	 * @param name         unique name of the receiver
	 */
	public DeckToCableLoadReceiver(SysMLBlock contextBlock, Long index, String name)
	{
		super(contextBlock, index, name);
	}
}
