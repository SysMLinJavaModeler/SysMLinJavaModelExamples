package motorinwheel.common;

import java.util.Optional;
import sysmlinjava.annotations.Unit;
import sysmlinjava.quantitykinds.SysMLinJavaQuantityKinds;
import sysmlinjava.units.SysMLUnit;
import sysmlinjava.units.SysMLUnits;

/**
 * Units needed by the MotorInWheel model beyond those available in
 * {@code SysMLinJavaUnits}
 * 
 * @author ModelerOne
 *
 */
public class MotorInWheelUnits extends SysMLUnits
{
	@Unit
	public static final SysMLUnit NewtonMetersPerKilowatt = new SysMLUnit("newton-meters/kilowatt", "nm/kw", "", "Newton-Meters per Kilowatt", Optional.of(SysMLinJavaQuantityKinds.Energy));

	static
	{
		instances.add(NewtonMetersPerKilowatt);
	}
}
