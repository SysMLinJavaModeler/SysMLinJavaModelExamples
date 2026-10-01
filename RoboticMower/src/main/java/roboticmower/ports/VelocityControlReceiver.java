package roboticmower.ports;

import java.util.Optional;

import roboticmower.signals.VelocityControlSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

/**
 * Full port for reception of the controlled velocity of the mower. The port
 * operates as a default SysMLFullPort for the receive operations with
 * specialized implmentation of the signal event creation operation that is
 * invoked by the receive operations. This port is part of the
 * {@code MowerController}.
 * 
 * @author ModelerOne
 *
 * @see roboticmower.mower.MowerController
 */
public class VelocityControlReceiver extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock {@code MowerController} in whose context this port is to
	 *                     operate
	 * @param id           unique ID of the port
	 * @param name         unique name of the port
	 */
	public VelocityControlReceiver(SysMLPart contextBlock, Long id, String name)
	{
		super(contextBlock, Optional.of(contextBlock), id, name);
	}

	@Action
	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof VelocityControlSignal)
			result = new SysMLSignalEvent(signal, "VectorControlEvent", 0L);
		else
			logger.severe("unrecognized signal for VelocitySignalEvent: " + signal.getClass().getSimpleName());
		return result;
	}
}
