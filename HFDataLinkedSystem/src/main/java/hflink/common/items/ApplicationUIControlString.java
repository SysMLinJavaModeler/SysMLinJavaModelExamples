package hflink.common.items;

import sysmlinjava.items.SysMLItem;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Representation of control information as displayed in the application
 * user-interface. Display is represented as text, which may be HTML, plain
 * text, or other.
 * 
 * @author ModelerOne
 *
 */
public class ApplicationUIControlString extends SysMLItem
{
	/**
	 * String for the control as text
	 */
	@Attribute
	public String text;

	/**
	 * Conxtructor for specified control text
	 * 
	 * @param text text of control
	 */
	public ApplicationUIControlString(String text)
	{
		super();
		this.text = text;
	}

	/**
	 * Constructor for unspecified control text
	 */
	public ApplicationUIControlString()
	{
		this.text = "not specified";
	}

	/**
	 * Constructor for copy of specified control string
	 * 
	 * @param control control string value
	 */
	public ApplicationUIControlString(ApplicationUIControlString control)
	{
		this.text = control.text;
	}

	@Override
	public String toString()
	{
		return String.format("ApplicationUIControlString [text=%s]", text);
	}
}