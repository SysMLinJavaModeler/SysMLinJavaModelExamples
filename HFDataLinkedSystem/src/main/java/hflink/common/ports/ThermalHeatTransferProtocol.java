package hflink.common.ports;

import sysmlinjava.ports.SysMLPort;
import sysmlinjava.states.StateBehaviorContext;

/**
 * Port for the transfer of heat out from the context block
 * 
 * @author ModelerOne
 *
 */
public class ThermalHeatTransferProtocol extends SysMLPort
{

	/**
	 * Conxtrucor
	 * 
	 * @param context block in whose context the port resides
	 * @param id      unique ID
	 */
	public ThermalHeatTransferProtocol(StateBehaviorContext context, Long id)
	{
		super(context, id);
	}
}
