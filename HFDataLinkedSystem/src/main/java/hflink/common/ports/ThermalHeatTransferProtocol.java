package hflink.common.ports;

import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port for the transfer of heat out from the context block
 * 
 * @author ModelerOne
 *
 */
public class ThermalHeatTransferProtocol extends SysMLFullPort
{

	/**
	 * Conxtrucor
	 * 
	 * @param contextBlock block in whose context the port resides
	 * @param id           unique ID
	 */
	public ThermalHeatTransferProtocol(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}
}
