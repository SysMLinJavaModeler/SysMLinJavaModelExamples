package dbssystem.patient;

import dbssystem.common.PressureSignal;
import dbssystem.common.PressureSignalSignal;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.ports.SysMLPort;

/**
 * Port to transmit the patient's blood pressure signal out to a controller
 * 
 * @author ModelerOne
 *
 */
public class PressureSignalOutPort extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock the Patient from which the pressure signal is to
	 *                     transmitted
	 */
	public PressureSignalOutPort(Patient contextBlock)
	{
		super(contextBlock, 0L, "PressureSignalOutPort");
	}

	@Action
	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof PressureSignal pressureSignal)
			result = new PressureSignalSignal(pressureSignal);
		return result;
	}
}
