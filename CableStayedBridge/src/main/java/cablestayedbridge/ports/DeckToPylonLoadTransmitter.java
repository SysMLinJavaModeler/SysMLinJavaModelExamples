package cablestayedbridge.ports;

import sysmlinjava.parts.SysMLPart;

/**
 * SysMLinJava full port representation of a bridge deck interface that
 * transmits the load of a section of the deck to a pylon. The
 * {@code DeckToPylonLoadTransmitter} simply extends the {@code LoadTransmitter}
 * for transmitting load/weight transmitted from a section of the deck to a
 * point on the pylon.
 * 
 * @author ModelerOne
 *
 */
public class DeckToPylonLoadTransmitter extends LoadTransmitter
{
	/**
	 * Constructor
	 * 
	 * @param context block (presumably a {@code BridgeDeck}) in whose context
	 *                     this port will trasnmit the load
	 * @param index        index of this load transmitter into a set/array of load
	 *                     transmitters
	 * @param name         unique name of the transmitter
	 */
	public DeckToPylonLoadTransmitter(SysMLPart context, Long index, String name)
	{
		super(context, index, name);
	}
}
