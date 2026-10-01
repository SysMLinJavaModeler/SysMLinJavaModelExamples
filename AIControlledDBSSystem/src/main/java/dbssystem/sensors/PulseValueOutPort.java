package dbssystem.sensors;

import dbssystem.common.PulseValue;
import dbssystem.common.PulseValueSignal;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.ports.SysMLPort;

/**
 * Port to transmit a pulse value out of the PulseSensor
 * 
 * @author ModelerOne
 *
 */
public class PulseValueOutPort extends SysMLPort
{
	/**
	 * Constructor
	 * @param pulseSensor parent part
	 * @param identifier unique identifier
	 * @param name unique name
	 */
	public PulseValueOutPort(PulseSensor pulseSensor, Long identifier, String name)
	{
		super(pulseSensor, identifier, name);
	}

	@Action
	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if(object instanceof PulseValue pulseValue)
			result = new PulseValueSignal(pulseValue);
		return result;
	}
}
