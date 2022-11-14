package cablestayedbridge.ports;

import sysmlinjava.blocks.SysMLBlock;

/**
 * SysMLinJava full port representation of a cable interface that transmits the
 * load of a cable to saddle on a pylon. The {@code CableToPylonLoadTransmitter}
 * simply extends the {@code LoadTransmitter} for transmitting load/weight
 * transmitted from a cable to the pylon deck.
 * 
 * @author ModelerOne
 *
 */
public class CableToPylonLoadTransmitter extends LoadTransmitter
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock block (presumably a {@code Cable}) in whose context this
	 *                     port will trasnmit the load
	 * @param index        index of this load transmitter into a set/array of load
	 *                     transmitters
	 * @param name         unique name of the transmitter
	 */
	public CableToPylonLoadTransmitter(SysMLBlock contextBlock, Long index, String name)
	{
		super(contextBlock, index, name);
	}
}
