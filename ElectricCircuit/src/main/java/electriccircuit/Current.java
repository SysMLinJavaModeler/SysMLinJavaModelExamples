package electriccircuit;

import sysmlinjava.units.SysMLinJavaUnits;
import sysmlinjava.valuetypes.RReal;

/**
 * Value type for electical current with units ampere. This value type is
 * redundant to the {@code CurrentAmperes} value type in the SysMLinJava API,
 * but was implemented to be an identical model element to the current value
 * type used in the electrcal circuit model described in "SysML Extension for
 * Physical Interaction and Signal Flow Simulation", Object Management Group,
 * Inc., 2018.
 * 
 * @author ModelerOne
 *
 */
public class Current extends RReal
{
	private static final long serialVersionUID = -1329599791502360535L;

	/**
	 * Constructor
	 * 
	 * @param value initial value in ohms for the current
	 */
	public Current(double value)
	{
		super(value);
	}

	/**
	 * Copy Constructor
	 * 
	 * @param copiedFrom initial value to be copied from
	 */
	public Current(Current copiedFrom)
	{
		super(copiedFrom.value);
	}

	@Override
	protected void createUnits()
	{
		units = SysMLinJavaUnits.Ampere;
	}

}
