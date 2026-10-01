package dbssystem.patient;

import dbssystem.common.MotionSignal;
import dbssystem.common.MotionSignalSignal;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.ports.SysMLPort;

/**
 * Port to transmit the patient's motion (tremor) signal out from the patient
 * 
 * @author ModelerOne
 *
 */
public class MotionSignalOutPort extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock the Patient from which the motion signal is to be
	 *                     transmitted
	 */
	public MotionSignalOutPort(Patient contextBlock)
	{
		super(contextBlock, 0L, "MotionSignalOutPort");
	}

	@Action
	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof MotionSignal motionSignal)
			result = new MotionSignalSignal(motionSignal);
		return result;
	}
}
