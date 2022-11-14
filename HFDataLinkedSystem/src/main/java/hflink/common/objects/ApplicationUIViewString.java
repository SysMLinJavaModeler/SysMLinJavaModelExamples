package hflink.common.objects;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;

/**
 * String representation of the system view information provided to the operator
 * by the application user-interface
 * 
 * @author ModelerOne
 *
 */
public class ApplicationUIViewString extends SysMLClass
{
	@Attribute
	public String text;

	public ApplicationUIViewString(String text)
	{
		super();
		this.text = text;
	}

	public ApplicationUIViewString(ApplicationUIViewString view)
	{
		this.text = view.text;
	}

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