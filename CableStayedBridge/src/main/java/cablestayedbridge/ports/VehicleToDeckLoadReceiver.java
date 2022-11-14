package cablestayedbridge.ports;

import sysmlinjava.blocks.SysMLBlock;

/**
 * SysMLinJava full port representation of a bridge deck interface that receives
 * the load of a vehicle at point on the bridge deck. The
 * {@code VehicleToDeckLoadReceiver} simply extends the {@code LoadReceiver} for
 * receiving load/weight transmitted from a vehicle on the bridge deck.
 * 
 * @author ModelerOne
 *
 */
public class VehicleToDeckLoadReceiver extends LoadReceiver
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock block (presumably a {@code BridgeDeck} indirectly via a
	 *                     {@code CableStayedBridge}) in whose context this port
	 *                     will receive the load
	 * @param index        index of this load receiver into a set/array of load
	 *                     receivers
	 * @param name         unique name of the receiver
	 */
	public VehicleToDeckLoadReceiver(SysMLBlock contextBlock, Long index, String name)
	{
		super(contextBlock, index, name);
	}
}
