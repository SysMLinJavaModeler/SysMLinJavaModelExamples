package hflink.common.items;

import sysmlinjava.items.SysMLItem;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * String representation of the system view information provided to the operator
 * by the application user-interface. View is represented as text which may be
 * HTML, plain text, or other.
 * 
 * @author ModelerOne
 *
 */
public class ApplicationUIViewString extends SysMLItem
{
	/**
	 * String for the text of the view
	 */
	@Attribute
	public String text;

	/**
	 * Constructor for the specified text of the view
	 * 
	 * @param text text value of the view
	 */
	public ApplicationUIViewString(String text)
	{
		super();
		this.text = text;
	}

	/**
	 * Constructor for copy of the specified view string
	 * 
	 * @param view view string value to be copy of
	 */
	public ApplicationUIViewString(ApplicationUIViewString view)
	{
		this.text = view.text;
	}

	/**
	 * Constructor for unspecified view string
	 */
	public ApplicationUIViewString()
	{
		this.text = "not specified";
	}

	@Override
	public String toString()
	{
		return String.format("ApplicationUIViewString [text=%n%s]", text);
	}
}