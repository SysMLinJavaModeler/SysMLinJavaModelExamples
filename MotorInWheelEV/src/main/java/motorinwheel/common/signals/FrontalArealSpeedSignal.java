package motorinwheel.common.signals;

import sysmlinjava.attributetypes.FrontalArealSpeed;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal for transmission of a moving frontal area
 * 
 * @author ModelerOne
 *
 */
public class FrontalArealSpeedSignal extends SysMLSignal
{
	@Attribute
	public FrontalArealSpeed frontalArealSpeed;

	public FrontalArealSpeedSignal(FrontalArealSpeed frontalArealSpeed)
	{
		super();
		this.frontalArealSpeed = frontalArealSpeed;
	}

	@Override
	public String stackNamesString()
	{
		return "FrontalArealSpeed";
	}
}
