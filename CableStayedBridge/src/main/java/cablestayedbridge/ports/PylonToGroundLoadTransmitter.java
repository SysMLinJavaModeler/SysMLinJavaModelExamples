package cablestayedbridge.ports;

import sysmlinjava.blocks.SysMLBlock;

/**
 * SysMLinJava full port representation of a pylon interface that transmits the
 * load of the pylon to a ground base. The {@code PylonTo GroundLoadTransmitter}
 * simply extends the {@code LoadTransmitter} for transmitting load/weight
 * transmitted from a pylon to a ground base.
 * 
 * @author ModelerOne
 *
 */
public class PylonToGroundLoadTransmitter extends LoadTransmitter
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock block (presumably a {@code Pylon}) in whose context this
	 *                     port will trasnmit the load
	 * @param index        index of this load transmitter into a set/array of load
	 *                     transmitters
	 * @param name         unique name of the transmitter
	 */
	public PylonToGroundLoadTransmitter(SysMLBlock contextBlock, Long index, String name)
	{
		super(contextBlock, index, name);
	}
}
