package motorinwheel.components.motor;

import sysmlinjava.valuetypes.RReal;
import motorinwheel.common.MotorInWheelUnits;

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
