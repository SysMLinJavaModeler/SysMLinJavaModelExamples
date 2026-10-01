package dbssystem.sensors;

import dbssystem.common.TremorPresenceSignal;
import sysmlinjava.attributetypes.BBoolean;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.ports.SysMLPort;

/**
 * Port to transmit a tremor presence indicator out of the TremorSensor
 * 
 * @author ModelerOne
 */
public class TremorPresenceOutPort extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param tremorSensor parent part
	 * @param id           unique identifier
	 * @param name         unique name
	 */
	public TremorPresenceOutPort(TremorSensor tremorSensor, Long id, String name)
	{
		super(tremorSensor, id, name);
	}

	@Action
	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof BBoolean bboolean)
			result = new TremorPresenceSignal(bboolean);
		return result;
	}
}
