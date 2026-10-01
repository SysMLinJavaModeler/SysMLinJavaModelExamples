package motorinwheel.common;

import java.util.Optional;
import sysmlinjava.javaannotations.attributes.Unit;
import sysmlinjava.quantitykinds.SysMLinJavaQuantityKinds;
import sysmlinjava.units.SysMLUnit;
import sysmlinjava.units.SysMLUnitsCollection;

/**
 * Units needed by the MotorInWheel model beyond those available in
 * {@code SysMLinJavaUnits}
 * 
 * @author ModelerOne
 *
 */
public class MotorInWheelUnits extends SysMLUnitsCollection
{
	@Unit
	public static final SysMLUnit NewtonMetersPerKilowatt = new SysMLUnit("newton-meters/kilowatt", "nm/kw", "", "Newton-Meters per Kilowatt", Optional.of(SysMLinJavaQuantityKinds.Energy));
}
