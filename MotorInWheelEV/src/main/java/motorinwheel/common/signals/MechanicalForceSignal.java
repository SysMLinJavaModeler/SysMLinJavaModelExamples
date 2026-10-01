package motorinwheel.common.signals;

import sysmlinjava.attributetypes.ForceNewtons;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal for transmission of a mechanical force on an identified object
 * 
 * @author ModelerOne
 *
 */
public class MechanicalForceSignal extends SysMLSignal
{
	@Attribute
	public ForceNewtons force;
	@Attribute
	public Long id;

	public MechanicalForceSignal(ForceNewtons force, Long id)
	{
		super();
		this.force = force;
		this.id = id;
	}

	@Override
	public String stackNamesString()
	{
		return "MechanicalForce";
	}
}
