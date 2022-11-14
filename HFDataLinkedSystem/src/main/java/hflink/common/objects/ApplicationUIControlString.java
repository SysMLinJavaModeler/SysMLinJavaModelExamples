package hflink.common.objects;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;

/**
 * Representation of control information as displayed in the application
 * user-interface.
 * 
 * @author ModelerOne
 *
 */
public class ApplicationUIControlString extends SysMLClass
{
	@Attribute
	public String text;

	public ApplicationUIControlString(String text)
	{
		super();
		this.text = text;
	}

	public ApplicationUIControlString()
	{
		this.text = "not specified";
	}

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