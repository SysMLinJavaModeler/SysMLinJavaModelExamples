package hflink.common.ports;

import sysmlinjava.ports.SysMLPort;
import sysmlinjava.states.StateBehaviorContext;

/**
 * Port/protocol to simulate the input of electrical power
 * 
 * @author ModelerOne
 *
 */
public class ElectricalPowerProtocol extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param context block in whose context the port resides
	 * @param id           unique ID
	 */
	public ElectricalPowerProtocol(StateBehaviorContext context, Long id)
	{
		super(context, id);
	}

}
