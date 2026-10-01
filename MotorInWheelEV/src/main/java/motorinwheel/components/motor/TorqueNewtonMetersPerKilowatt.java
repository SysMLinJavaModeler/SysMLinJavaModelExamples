package motorinwheel.components.motor;

import motorinwheel.common.MotorInWheelUnits;
import sysmlinjava.attributetypes.RReal;

public class TorqueNewtonMetersPerKilowatt extends RReal
{
	private static final long serialVersionUID = 9159924672262578011L;

	public TorqueNewtonMetersPerKilowatt(double value)
	{
		super(value);
	}

	@Override
	public void createUnits()
	{
		units = MotorInWheelUnits.NewtonMetersPerKilowatt;
	}
}
