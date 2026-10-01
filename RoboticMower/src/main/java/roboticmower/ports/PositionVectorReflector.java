package roboticmower.ports;

import java.util.Optional;

import roboticmower.signals.Point2DSignal;
import roboticmower.signals.VectorSignal;
import sysmlinjava.attributetypes.Vector2D;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

/**
 * Full port for reception of the position controller's position and subsequent
 * transmission (reflection) of the mower's position vector relative to the
 * position controller. The port operates as a default SysMLFullPort for both
 * receive and transmit operations with specialized implmentations of the signal
 * and event creation operations that are invoked by the receive and transmit
 * operations, respectively. This port is part of the
 * {@code MowerControlSubsystem}.
 * 
 * @author ModelerOne
 *
 * @see roboticmower.mower.MowerController
 */
public class PositionVectorReflector extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock {@code MowerControlSubsystem} in whose context this port
	 *                     is to operate
	 * @param id           unique ID of the port
	 * @param name         unique name of the port
	 */
	public PositionVectorReflector(SysMLPart contextBlock, Long id, String name)
	{
		super(contextBlock, Optional.of(contextBlock), id, name);
	}

	@Action
	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof Vector2D)
			result = new VectorSignal((Vector2D)object);
		else
			logger.severe("unrecognized object for VectorSignal: " + object.getClass().getSimpleName());
		return result;
	}

	@Action
	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof Point2DSignal)
			result = new SysMLSignalEvent(signal, "PositionVectorReflectionEvent", 0L);
		else
			logger.severe("unrecognized signal for PositionVectorSignalEvent: " + signal.getClass().getSimpleName());
		return result;
	}
}
