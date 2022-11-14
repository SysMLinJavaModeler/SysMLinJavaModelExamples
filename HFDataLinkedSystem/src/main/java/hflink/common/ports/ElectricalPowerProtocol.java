package hflink.common.ports;

import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port to simulate the input of electrical power
 * 
 * @author ModelerOne
 *
 */
public class ElectricalPowerProtocol extends SysMLFullPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock block in whose context the port resides
	 * @param id           unique ID
	 */
	public ElectricalPowerProtocol(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

}
