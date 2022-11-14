package cablestayedbridge.ports;

import sysmlinjava.blocks.SysMLBlock;

/**
 * SysMLinJava full port representation of a bridge deck interface that
 * transmits the load of a section of the deck to a suspending cable. The
 * {@code DeckToCableLoadTransmitter} simply extends the {@code LoadTransmitter}
 * for transmitting load/weight transmitted from a deck section to a cable.
 * 
 * @author ModelerOne
 *
 */
public class DeckToCableLoadTransmitter extends LoadTransmitter
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock block (presumably a {@code BridgeDeck} in whose context
	 *                     this port will transmit the load
	 * @param index        index of this load transmitter into a set/array of load
	 *                     transmitters
	 * @param name         unique name of the transmitter
	 */
	public DeckToCableLoadTransmitter(SysMLBlock contextBlock, Long index, String name)
	{
		super(contextBlock, index, name);
	}
}
