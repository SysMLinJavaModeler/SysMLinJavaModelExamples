package connectedtanks;

import sysmlinjava.units.SysMLinJavaUnits;

/**
 * Viscosity as an extenstion of pressure, for a second
 * 
 * @author ModelerOne
 *
 */
public class Viscosity extends Pressure
{
	private static final long serialVersionUID = -7187408417061035537L;

	/**
	 * Constructor
	 * @param value initial value
	 */
	public Viscosity(double value)
	{
		super(value);
	}

	/**
	 * Copy constructor
	 * @param copyFrom value from which this value is to be a copy of
	 */
	public Viscosity(Viscosity copyFrom)
	{
		super(copyFrom.value);
	}

	@Override
	protected void createUnits()
	{
		units = SysMLinJavaUnits.PascalSecond;
	}
}
