package cablestayedbridge.ports;

import sysmlinjava.parts.SysMLPart;

/**
 * SysMLinJava full port representation of a vehicle interface that transmits
 * the load of a vehicle to a point on the bridge deck. The
 * {@code VehicleToDeckLoadTransmitter} simply extends the
 * {@code LoadTransmitter} for transmitting load/weight transmitted from a
 * vehicle to the bridge deck.
 * 
 * @author ModelerOne
 *
 */
public class VehicleToDeckLoadTransmitter extends LoadTransmitter
{
	/**
	 * Constructor
	 * 
	 * @param context block (presumably a {@code Vehicle} in whose context this
	 *                     port will transmit the load
	 * @param index        index of this load transmitter into a set/array of load
	 *                     transmitters
	 * @param name         unique name of the transmitter
	 */
	public VehicleToDeckLoadTransmitter(SysMLPart context, Long index, String name)
	{
		super(context, index, name);
	}

}
