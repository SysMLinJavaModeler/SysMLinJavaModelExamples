package motorinwheel.components.wheel;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.valuetypes.SysMLEnumeration;

/**
 * Enumeration of the four wheel locations
 * 
 * @author ModelerOne
 *
 */
public class WheelLocationEnum extends SysMLEnumeration<WheelLocationEnum>
{
	/**
	 * Constant instance for left-front wheel location
	 */
	@Attribute
	public static final WheelLocationEnum leftFront = new WheelLocationEnum("LeftFront", 0);
	/**
	 * Constant instance for right-front wheel location
	 */
	@Attribute
	public static final WheelLocationEnum rightFront = new WheelLocationEnum("RightFront", 1);
	/**
	 * Constant instance for left-rear wheel location
	 */
	@Attribute
	public static final WheelLocationEnum leftRear = new WheelLocationEnum("LeftRear", 2);
	/**
	 * Constant instance for right-rear wheel location
	 */
	@Attribute
	public static final WheelLocationEnum rightRear = new WheelLocationEnum("RightRear", 3);
	/**
	 * Array of instances
	 */
	public static final WheelLocationEnum[] values = {leftFront, rightFront, leftRear, rightRear};

	/**
	 * Constructor - name and ordinal
	 * 
	 * @param name    - name of enumeration instance
	 * @param ordinal - ordinal value of enumeration instance
	 */
	private WheelLocationEnum(String name, int ordinal)
	{
		super(name, ordinal);
	}

	/**
	 * Returns the instance of enumeration with specified name
	 * 
	 * @param name name of enumeration value
	 * @return instance of enumeration with specified name
	 */
	public static WheelLocationEnum valueOf(String name)
	{
		return SysMLEnumeration.valueOf(name, values);
	}

	/**
	 * Returns the instance of enumeration with specified ordinal
	 * 
	 * @param ordinal ordinal of enumeration value
	 * @return instance of enumeration with specified ordinal
	 */
	public static WheelLocationEnum valueOf(int ordinal)
	{
		return SysMLEnumeration.valueOf(ordinal, values);
	}

	/**
	 * Returns array of enumeration instance values
	 * 
	 * @return array of the enumeration instance values
	 */
	public static WheelLocationEnum[] values()
	{
		return values;
	}
}
