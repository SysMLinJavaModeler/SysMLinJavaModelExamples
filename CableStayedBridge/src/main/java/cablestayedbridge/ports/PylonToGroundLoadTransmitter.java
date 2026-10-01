package cablestayedbridge.ports;

import sysmlinjava.parts.SysMLPart;

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
	 * @param context block (presumably a {@code Pylon}) in whose context this
	 *                     port will trasnmit the load
	 * @param index        index of this load transmitter into a set/array of load
	 *                     transmitters
	 * @param name         unique name of the transmitter
	 */
	public PylonToGroundLoadTransmitter(SysMLPart context, Long index, String name)
	{
		super(context, index, name);
	}
}
