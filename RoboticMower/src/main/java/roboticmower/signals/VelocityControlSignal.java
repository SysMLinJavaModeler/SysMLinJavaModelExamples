package roboticmower.signals;

import sysmlinjava.attributetypes.VelocityMetersPerSecondRadians;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal for transmission of a {@code VelocityMetersPerSecondRadians} instance
 * between ports
 * 
 * @author ModelerOne
 *
 */
public class VelocityControlSignal extends SysMLSignal
{
	/**
	 * Attribute for the {@code VelocityMetersPerSecondRadians} instance carried by
	 * the signal
	 */
	@Attribute
	public VelocityMetersPerSecondRadians velocity;

	/**
	 * Constructor
	 * 
	 * @param velocity {@code VelocityMetersPerSecondRadians} instance to be carried
	 *                 by the signal
	 */
	public VelocityControlSignal(VelocityMetersPerSecondRadians velocity)
	{
		super("Velocity", 0L);
		this.velocity = velocity;
	}

	@Override
	public String stackNamesString()
	{
		return String.format("Velocity control: %2.2f m/s, %3.2f deg", velocity.value, velocity.heading.toDegrees().value);
	}
}
