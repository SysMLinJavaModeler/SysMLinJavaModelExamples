package roboticmower.ports;

import roboticmower.signals.VelocityControlSignal;
import sysmlinjava.attributetypes.VelocityMetersPerSecondRadians;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

/**
 * Full port for transmission of the controlled velocity of the mower by the
 * position controller. The port operates as a default SysMLFullPort for the
 * transmit operation with specialized implmentation of the signal creation
 * operation that is invoked by the receive operation. This port is part of the
 * {@code PositionControlSubsystem}.
 * 
 * @author ModelerOne
 *
 * @see roboticmower.positioncontroller.PositionController
 */
public class VelocityControlTransmitter extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock {@code PositionControlSubsystem} in whose context this
	 *                     port is to operate
	 * @param id           unique ID of the port
	 * @param name         unique name of the port
	 */
	public VelocityControlTransmitter(SysMLPart contextBlock, Long id, String name)
	{
		super(contextBlock, id, name);
	}

	@Action
	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof VelocityMetersPerSecondRadians)
			result = new VelocityControlSignal((VelocityMetersPerSecondRadians)object);
		else
			logger.severe("unrecognized object type for VelocityControlSignal: " + object.getClass().getSimpleName());
		return result;
	}

}
