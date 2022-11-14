package motorinwheel.common.signals;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.valuetypes.TorqueNewtonMeters;

/**
 * Signal for transmission of brake torque on the wheel
 * 
 * @author ModelerOne
 *
 */
public class BrakeTorqueSignal extends SysMLSignal
{
	@Attribute
	public TorqueNewtonMeters torque;

	public BrakeTorqueSignal(TorqueNewtonMeters torque)
	{
		super();
		this.torque = torque;
	}

	@Override
	public String stackNamesString()
	{
		return "BrakeTorque";
	}
}
