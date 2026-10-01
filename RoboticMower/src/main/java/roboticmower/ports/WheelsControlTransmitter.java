package roboticmower.ports;

import roboticmower.info.WheelsControl;
import roboticmower.signals.WheelsControlSignal;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

/**
 * Full port for transmission of the wheel control data to the mower driver. The
 * port operates as default SysMLFullPort for just transmit operations with
 * specialized implmentations of the signal creation operation that is invoked
 * by the transmit operation. This port is part of the {@code MowerContoller}.
 * 
 * @author ModelerOne
 *
 * @see roboticmower.mower.MowerController
 */
public class WheelsControlTransmitter extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock {@code MowerController} in whose context this port is to
	 *                     operate
	 * @param id           unique ID of the port
	 * @param name         unique name of the port
	 */
	public WheelsControlTransmitter(SysMLPart contextBlock, Long id, String name)
	{
		super(contextBlock, id, name);
	}

	@Action
	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof WheelsControl)
			result = new WheelsControlSignal((WheelsControl)object);
		else
			logger.severe("unexpected object type for WheelsControlSignal: " + object.getClass().getSimpleName());
		return result;
	}
}
