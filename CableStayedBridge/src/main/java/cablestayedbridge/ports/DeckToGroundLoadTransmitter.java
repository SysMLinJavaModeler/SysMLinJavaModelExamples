package cablestayedbridge.ports;

import sysmlinjava.blocks.SysMLBlock;

/**
 * SysMLinJava full port representation of a bridge deck interface that
 * transmits the load of a section of the bridge deck to a ground anchor. The
 * {@code DeckToGroundLoadTransmitter} simply extends the
 * {@code LoadTransmitter} for transmitting load/weight transmitted from a point
 * on the bridge deck to a point on a ground anchor.
 * 
 * @author ModelerOne
 *
 */
public class DeckToGroundLoadTransmitter extends LoadTransmitter
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock block (presumably a {@code BridgeDeck}) in whose context
	 *                     this port will trasnmit the load
	 * @param index        index of this load transmitter into a set/array of load
	 *                     transmitters
	 * @param name         unique name of the transmitter
	 */
	public DeckToGroundLoadTransmitter(SysMLBlock contextBlock, Long index, String name)
	{
		super(contextBlock, index, name);
	}
}
