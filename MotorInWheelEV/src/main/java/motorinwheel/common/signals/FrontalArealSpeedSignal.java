package motorinwheel.common.signals;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.valuetypes.FrontalArealSpeed;

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
