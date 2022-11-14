package cablestayedbridge.ports;

import sysmlinjava.blocks.SysMLBlock;

/**
 * SysMLinJava full port representation of a bridge component that receives the
 * load of a point on the bridge deck onto the pylon. The
 * {@code DeckToPylonLoadReceiver} simply extends the {@code LoadReceiver} for
 * receiving load/weight transmitted from points on the bridge deck.
 * 
 * @author ModelerOne
 *
 */
public class DeckToPylonLoadReceiver extends LoadReceiver
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock block (presumably a {@code Pylon}) in whose context this
	 *                     port will receive the load
	 * @param index        index of this load receiver into a set/array of load
	 *                     receivers
	 * @param name         unique name of the receiver
	 */
	public DeckToPylonLoadReceiver(SysMLBlock contextBlock, Long index, String name)
	{
		super(contextBlock, index, name);
	}
}
