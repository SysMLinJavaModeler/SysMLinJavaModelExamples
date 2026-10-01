package dbssystem.sensors;

import dbssystem.common.TremorLevel;
import dbssystem.common.TremorLevelSignal;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.ports.SysMLPort;

/**
 * Port to transmit a tremor level out of the TremorSensor
 * 
 * @author ModelerOne
 */
public class TremorLevelOutPort extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param tremorSensor TremorSensor in whose context this port executes
	 * @param id           unique identifier
	 * @param name         unique name
	 */
	public TremorLevelOutPort(TremorSensor tremorSensor, Long id, String name)
	{
		super(tremorSensor, id, name);
	}

	@Action
	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof TremorLevel tremorLevel)
			result = new TremorLevelSignal(tremorLevel);
		return result;
	}
}
