package electriccircuit;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.units.SysMLinJavaUnits;
import sysmlinjava.valuetypes.SysMLValueType;

/**
 * Value type for the flow of electric charge in terms of a current and voltage.
 * 
 * @author ModelerOne
 *
 */
public class FlowingCharge extends SysMLValueType
{
	/**
	 * Current of the flowing charge
	 */
	@Attribute
	Current i;
	/**
	 * Voltage of the flowing charge
	 */
	@Attribute
	Voltage v;

	/**
	 * Constructor
	 * 
	 * @param i current of the flowing charge
	 * @param v voltage of the flowing charge
	 */
	public FlowingCharge(Current i, Voltage v)
	{
		super();
		this.i = i;
		this.v = v;
	}

	/**
	 * Constructor
	 * 
	 * @param i current (amperes) of the flowing charge
	 * @param v voltage (volts) of the flowing charge
	 */
	public FlowingCharge(double i, double v)
	{
		this.i = new Current(i);
		this.v = new Voltage(i);
	}

	@Override
	protected void createUnits()
	{
		units = SysMLinJavaUnits.VoltAmperes;
	}
}
