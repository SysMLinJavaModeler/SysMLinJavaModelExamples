package connectedtanks;

import sysmlinjava.valuetypes.ForceNewtonsPerMeterSquare;

/**
 * Value type for pressure which is simply an extension of a force value type.
 * @author ModelerOne
 *
 */
public class Pressure extends ForceNewtonsPerMeterSquare
{
	private static final long serialVersionUID = -1538795321322592505L;

	/**
	 * Constructor
	 * @param value initial value
	 */
	public Pressure(double value)
	{
		super(value);
	}

	/**
	 * Copy constructor
	 * @param copyFrom pressure from which this value is to be a copy
	 */
	public Pressure(Pressure copyFrom)
	{
		super(copyFrom.value);
	}
}
