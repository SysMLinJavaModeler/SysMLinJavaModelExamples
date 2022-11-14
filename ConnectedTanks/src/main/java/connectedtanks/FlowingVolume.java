package connectedtanks;

import sysmlinjava.annotations.Value;
import sysmlinjava.units.SysMLinJavaUnits;
import sysmlinjava.valuetypes.SysMLValueType;

/**
 * Value type for a flowing volume of fluid as characterized by the rate of flow
 * and pressure of the volume
 * 
 * @author ModelerOne
 *
 */
public class FlowingVolume extends SysMLValueType
{
	/**
	 * Rate of flow of the fluid
	 */
	@Value
	VolumeFlowRate q;
	/**
	 * Pressure of the volume of fluid
	 */
	@Value
	Pressure p;

	/**
	 * Constructor
	 * 
	 * @param q rate of flow of the fluid
	 * @param p pressure of the volume of fluid
	 */
	public FlowingVolume(VolumeFlowRate q, Pressure p)
	{
		super();
		this.q = q;
		this.p = p;
	}

	@Override
	protected void createUnits()
	{
		units = SysMLinJavaUnits.MetersCubicPerSecond;
	}
}
