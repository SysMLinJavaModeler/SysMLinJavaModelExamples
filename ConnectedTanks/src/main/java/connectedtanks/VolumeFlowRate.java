package connectedtanks;

import sysmlinjava.units.SysMLinJavaUnits;
import sysmlinjava.valuetypes.RReal;

/**
 * Value type for the rate of flow of a volume of fluid
 * 
 * @author ModelerOne
 *
 */
public class VolumeFlowRate extends RReal
{
	private static final long serialVersionUID = -5734213520114579329L;

	/**
	 * Constructor
	 * 
	 * @param value initial value
	 */
	public VolumeFlowRate(double value)
	{
		super(value);
	}

	/**
	 * Copy constructor
	 * 
	 * @param copiedFrom value of which this value is to be copied from
	 */
	public VolumeFlowRate(VolumeFlowRate copiedFrom)
	{
		super(copiedFrom.value);
	}

	@Override
	protected void createUnits()
	{
		units = SysMLinJavaUnits.MetersCubicPerSecond;
	}
}
