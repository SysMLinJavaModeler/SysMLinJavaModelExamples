package roboticmower.ports;

import java.util.Optional;
import roboticmower.signals.WheelsControlSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

/**
 * Full port for reception of control data for the wheels of the mower. The port
 * operates as a default SysMLFullPort for the receive operation with
 * specialized implementation of the signal event creation operation that is
 * invoked by the receive operation. This port is part of the
 * {@code MowerDriver}.
 * 
 * @author ModelerOne
 *
 * @see roboticmower.mower.MowerDriver
 */
public class WheelsControlReceiver extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock {@code MowerDriver} in whose context this port is to
	 *                     operate
	 * @param id           unique ID of the port
	 * @param name         unique name of the port
	 */
	public WheelsControlReceiver(SysMLPart contextBlock, Long id, String name)
	{
		super(contextBlock, Optional.of(contextBlock), id, name);
	}

	@Action
	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof WheelsControlSignal)
			result = new SysMLSignalEvent(signal, "WheelsControlEvent", 0L);
		else
			logger.severe("unrecognized signal for WheelsControlSignalEvent: " + signal.getClass().getSimpleName());
		return result;
	}

}
