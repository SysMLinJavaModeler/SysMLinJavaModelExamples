package electriccircuit;

import sysmlinjava.units.SysMLinJavaUnits;
import sysmlinjava.valuetypes.RReal;

/**
 * Value type for electical voltage with units volts. This value type is
 * redundant to the {@code PotentialElectricVolts} value type in the SysMLinJava
 * API, but was implemented to be an identical model element to the current
 * value type used in the electrcal circuit model described in "SysML Extension
 * for Physical Interaction and Signal Flow Simulation", Object Management
 * Group, Inc., 2018.
 * 
 * @author ModelerOne
 *
 */
public class Voltage extends RReal
{
	private static final long serialVersionUID = -1329599791502360545L;

	/**
	 * Constructor
	 * 
	 * @param value initial value, in volts, of the voltage
	 */
	public Voltage(double value)
	{
		super(value);
	}

	/**
	 * Copy constructor
	 * 
	 * @param copiedFrom initial value to be copied from
	 */
	public Voltage(Voltage copiedFrom)
	{
		super(copiedFrom.value);
	}

	@Override
	protected void createUnits()
	{
		units = SysMLinJavaUnits.Volt;
	}
}
