package connectedtanks;

import sysmlinjava.attributetypes.PressureNewtonsPerMeterSquare;
import sysmlinjava.attributetypes.SysMLAttributeType;
import sysmlinjava.attributetypes.VolumeFlowMetersCubicPerSecond;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.units.SysMLinJavaUnits;

/**
 * Value type for a flowing volume of fluid as characterized by the rate of flow
 * and pressure of the volume
 * 
 * @author ModelerOne
 *
 */
public class FlowingVolume extends SysMLAttributeType
{
	/**
	 * Rate of flow of the fluid
	 */
	@Attribute
	VolumeFlowMetersCubicPerSecond q;
	/**
	 * Pressure of the volume of fluid
	 */
	@Attribute
	PressureNewtonsPerMeterSquare p;

	/**
	 * Constructor
	 * 
	 * @param q rate of flow of the fluid
	 * @param p pressure of the volume of fluid
	 */
	public FlowingVolume(VolumeFlowMetersCubicPerSecond q, PressureNewtonsPerMeterSquare p)
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
