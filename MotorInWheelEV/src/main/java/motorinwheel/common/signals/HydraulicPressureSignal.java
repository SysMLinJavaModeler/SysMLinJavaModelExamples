package motorinwheel.common.signals;

import sysmlinjava.attributetypes.ForceNewtonsPerMeterSquare;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal for transmission of pressure from a hydraulic line
 * 
 * @author ModelerOne
 *
 */
public class HydraulicPressureSignal extends SysMLSignal
{
	@Attribute
	public ForceNewtonsPerMeterSquare force;
	@Attribute
	public Long id;

	public HydraulicPressureSignal(ForceNewtonsPerMeterSquare force, Long id)
	{
		super();
		this.force = force;
		this.id = id;
	}

	@Override
	public String stackNamesString()
	{
		return "HydraulicPressure";
	}
}
