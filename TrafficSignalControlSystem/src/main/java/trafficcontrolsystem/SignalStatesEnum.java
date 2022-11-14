package trafficcontrolsystem;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.valuetypes.SysMLEnumeration;

/**
 * The SignalStatesEnum is a SysMLinJava Enumerated ValueType of the standard
 * traffic signal states, i.e. red, yellow, and green.
 * 
 * @author ModelerOne
 *
 */
public class SignalStatesEnum extends SysMLEnumeration<SignalStatesEnum>
{
	/**
	 * Enumeration of red signal state
	 */
	@Attribute
	public static final SignalStatesEnum Red = new SignalStatesEnum("Red", 0);
	/**
	 * Enumeration of yellow signal state
	 */
	@Attribute
	public static final SignalStatesEnum Yellow = new SignalStatesEnum("Yellow", 1);
	/**
	 * Enumeration of green signal state
	 */
	@Attribute
	public static final SignalStatesEnum Green = new SignalStatesEnum("Green", 2);
	/**
	 * Array of values of signal state for assignment to
	 * {@code SysMLEnumeration.values}
	 */
	private static final SignalStatesEnum[] values = {Red, Yellow, Green};

	/**
	 * Constructor
	 * 
	 * @param name    name of the state
	 * @param ordinal ordinal of the state
	 */
	public SignalStatesEnum(String name, int ordinal)
	{
		super(name, ordinal);
	}

	/**
	 * Constructor for copy
	 * 
	 * @param copyOf enumeration value to copy
	 */
	public SignalStatesEnum(SignalStatesEnum copyOf)
	{
		this(copyOf.name, copyOf.ordinal);
	}

	/**
	 * Returns enumerated value with the specified name
	 * 
	 * @param name name of the value
	 * @return enumerated value
	 */
	public static SignalStatesEnum valueOf(String name)
	{
		return SysMLEnumeration.valueOf(name, values);
	}

	/**
	 * Returns enumerated value with the specified ordinal
	 * 
	 * @param ordinal ordinal of the enumerated value
	 * @return enumerated value
	 */
	public static SignalStatesEnum valueOf(int ordinal)
	{
		return SysMLEnumeration.valueOf(ordinal, values);
	}

	/**
	 * Returns the enumerated values as an array
	 * 
	 * @return array of enumerated values
	 */
	public static SignalStatesEnum[] values()
	{
		return values;
	}
}
