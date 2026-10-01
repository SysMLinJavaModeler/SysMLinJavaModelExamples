package motorinwheel.common.signals;

import sysmlinjava.attributetypes.ForceNewtons;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal for transmission of force on the brake pedal
 * 
 * @author ModelerOne
 *
 */
public class BrakePedalForceSignal extends SysMLSignal
{
	@Attribute
	public ForceNewtons force;
	@Attribute
	public Long id;

	public BrakePedalForceSignal(ForceNewtons force, Long id)
	{
		super();
		this.force = force;
		this.id = id;
	}

	@Override
	public String stackNamesString()
	{
		return "BrakePedalForce";
	}
}
