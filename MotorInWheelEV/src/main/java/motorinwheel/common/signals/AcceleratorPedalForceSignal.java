package motorinwheel.common.signals;

import sysmlinjava.attributetypes.ForceNewtons;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal for transmission of force on the accelerator pedal
 * 
 * @author ModelerOne
 *
 */
public class AcceleratorPedalForceSignal extends SysMLSignal
{
	@Attribute
	public ForceNewtons force;
	@Attribute
	public Long id;

	public AcceleratorPedalForceSignal(ForceNewtons force, Long id)
	{
		super();
		this.force = force;
		this.id = id;
	}

	@Override
	public String stackNamesString()
	{
		return "AcceleratorPedalForce";
	}
}
