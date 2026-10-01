package roboticmower.signals;

import sysmlinjava.attributetypes.Vector2D;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal that carries a {@code Vector2D} instance for transmission between
 * ports
 * 
 * @author ModelerOne
 *
 */
public class VectorSignal extends SysMLSignal
{
	/**
	 * Attribute for the {@code Vector2D} instance that is carried by the signal
	 */
	@Attribute
	public Vector2D vector;

	/**
	 * Constructor
	 * 
	 * @param vector {@code Vector2D} instance to be carried by the signal
	 */
	public VectorSignal(Vector2D vector)
	{
		super("Vector", 0L);
		this.vector = vector;
	}

	@Override
	public String stackNamesString()
	{
		return String.format("Vector2D: %2.2f m, %2.2f deg", vector.value, vector.direction.toDegrees().value);
	}
}
