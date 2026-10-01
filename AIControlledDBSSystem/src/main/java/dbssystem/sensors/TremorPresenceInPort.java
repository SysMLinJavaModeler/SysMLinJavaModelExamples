package dbssystem.sensors;

import java.util.Optional;

import dbssystem.common.TremorPresenceSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.ports.SysMLPort;

/**
 * Port for receiving the tremor presence indications from the tremor sensor by
 * the pulse sensor
 * 
 * @author ModelerOne
 */
public class TremorPresenceInPort extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param pulseSensor parent part
	 * @param id          unique identifier
	 * @param name        unique name
	 */
	public TremorPresenceInPort(PulseSensor pulseSensor, Long id, String name)
	{
		super(pulseSensor, Optional.of(pulseSensor), id, name);
	}

	@Action
	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof TremorPresenceSignal tremorPresenceSignal)
			result = new SysMLSignalEvent(tremorPresenceSignal, "TremorPresenceEvent", 0L);
		return result;
	}

}
