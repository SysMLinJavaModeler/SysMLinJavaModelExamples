package motorinwheel.common.signals;

import sysmlinjava.attributetypes.TorqueNewtonMeters;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

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
