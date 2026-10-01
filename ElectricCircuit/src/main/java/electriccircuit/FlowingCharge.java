package electriccircuit;

import sysmlinjava.attributetypes.CurrentAmps;
import sysmlinjava.attributetypes.SysMLAttributeType;
import sysmlinjava.attributetypes.Voltage;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.units.SysMLinJavaUnits;

/**
 * Value type for the flow of electric charge in terms of a CurrentAmps and
 * voltage.  The model is a SysMLinJava implementation of the model by the same
 * name described in "SysML Extension for Physical Interaction and Signal Flow
 * Simulation", Object Management Group, Inc., 2018.
 * 
 * @see <a href="https://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @author ModelerOne
 *
 */
public class FlowingCharge extends SysMLAttributeType
{
	/**
	 * CurrentAmps of the flowing charge
	 */
	@Attribute
	CurrentAmps i;
	/**
	 * Voltage of the flowing charge
	 */
	@Attribute
	Voltage v;

	/**
	 * Constructor
	 * 
	 * @param i CurrentAmpsAmps of the flowing charge
	 * @param v voltage of the flowing charge
	 */
	public FlowingCharge(CurrentAmps i, Voltage v)
	{
		super();
		this.i = i;
		this.v = v;
	}

	/**
	 * Constructor
	 * 
	 * @param i CurrentAmpsAmps (amperes) of the flowing charge
	 * @param v voltage (volts) of the flowing charge
	 */
	public FlowingCharge(double i, double v)
	{
		this.i = new CurrentAmps(i);
		this.v = new Voltage(i);
	}

	@Override
	protected void createUnits()
	{
		units = SysMLinJavaUnits.VoltAmperes;
	}
}
