package motorinwheel.common.signals;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.valuetypes.TorqueNewtonMeters;

/**
 * Signal for transmission of motor torque on a wheel
 * 
 * @author ModelerOne
 *
 */
public class MotorTorqueSignal extends SysMLSignal
{
	@Attribute
	public TorqueNewtonMeters torque;

	public MotorTorqueSignal(TorqueNewtonMeters torque)
	{
		super();
		this.torque = torque;
	}

	@Override
	public String stackNamesString()
	{
		return "MotorTorque";
	}
}
