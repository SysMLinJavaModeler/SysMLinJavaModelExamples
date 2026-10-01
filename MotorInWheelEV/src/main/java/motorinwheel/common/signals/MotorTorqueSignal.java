package motorinwheel.common.signals;

import sysmlinjava.attributetypes.TorqueNewtonMeters;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

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
