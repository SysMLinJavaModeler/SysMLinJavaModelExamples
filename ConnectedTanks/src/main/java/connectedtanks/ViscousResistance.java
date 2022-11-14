package connectedtanks;

import sysmlinjava.valuetypes.ForceNewtons;

/**
 * Value type for viscous resistance (drag) for a fluid, which is simply a type
 * of force.
 * 
 * @author ModelerOne
 *
 */
public class ViscousResistance extends ForceNewtons
{
	private static final long serialVersionUID = -556839566475312102L;

	/**
	 * Constructor
	 * 
	 * @param value initial value
	 */
	public ViscousResistance(double value)
	{
		super(value);
	}

	/**
	 * Copy constructor
	 * 
	 * @param copyFrom value from which this value is to be a copy of
	 */
	public ViscousResistance(ViscousResistance copyFrom)
	{
		super(copyFrom.value);
	}
}
