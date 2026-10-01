package roboticmower.signals;

import roboticmower.info.WheelsControl;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal for transmission of a {@code WheelsControl} instance between ports
 * 
 * @author ModelerOne
 *
 */
public class WheelsControlSignal extends SysMLSignal
{
	/**
	 * Attribute for the {@code WheelsControl} instance carried by the signal
	 */
	@Attribute
	public WheelsControl control;

	/**
	 * Constructor
	 * 
	 * @param control {@code WheelsControl} instance to be carried by the signal
	 */
	public WheelsControlSignal(WheelsControl control)
	{
		super();
		this.control = control;
	}

	@Override
	public String stackNamesString()
	{
		return String.format("WheelsControl: %2.2f rpm, %2.2f deg", control.rpm.value, control.direction.value);
	}
}
