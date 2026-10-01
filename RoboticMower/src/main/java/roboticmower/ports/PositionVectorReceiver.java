package roboticmower.ports;

import java.util.Optional;
import roboticmower.positioncontroller.PositionController;
import roboticmower.signals.Point2DSignal;
import roboticmower.signals.VectorSignal;
import sysmlinjava.attributetypes.Point2D;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.ports.SysMLPort;

/**
 * Full port for transmission of the position controller's position and
 * subsequent receipt of the mower's position vector relative to the position
 * controller. The port operates as default SysMLFullPort for both receive and
 * transmit operations with specialized implmentations of the signal and event
 * creation operations that are invoked by the receive and transmit operations,
 * respectively. This port is part of the {@code PositionController}.
 * 
 * @author ModelerOne
 *
 * @see roboticmower.positioncontroller.PositionController
 */
public class PositionVectorReceiver extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock {@code PositionControlSubsystem} in whose context this
	 *                     port is to operate
	 * @param id           unique ID of the port
	 * @param name         unique name of the port
	 */
	public PositionVectorReceiver(PositionController contextBlock, Long id, String name)
	{
		super(contextBlock, Optional.of(contextBlock), id, name);
	}

	@Action
	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof Point2D)
			result = new Point2DSignal((Point2D)object);
		else
			logger.severe("unrecognized object for Point2DSignal: " + object.getClass().getSimpleName());
		return result;
	}

	@Action
	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof VectorSignal)
			result = new SysMLSignalEvent(signal, "PositionVectorEvent", 0L);
		else
			logger.severe("unrecognized signal for VectorSignalEvent: " + signal.getClass().getSimpleName());
		return result;
	}
}
