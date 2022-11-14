package hflink.common.objects;

import java.util.Optional;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;

/**
 * System control information provided by the operator via the application
 * user-interface
 * 
 * @author ModelerOne
 *
 */
public class ApplicationUIControl extends SysMLClass implements StackedProtocolObject
{
	/**
	 * String representation of the control information
	 */
	@Attribute
	public ApplicationUIControlString control;

	/**
	 * Constructur
	 * 
	 * @param control string representation of the control information
	 */
	public ApplicationUIControl(ApplicationUIControlString control)
	{
		super();
		this.control = control;
	}

	public ApplicationUIControl()
	{
		this.control = new ApplicationUIControlString();
	}

	public ApplicationUIControl(ApplicationUIControl control)
	{
		this.control = new ApplicationUIControlString(control.control);
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.empty());
	}

	@Override
	public String toString()
	{
		return String.format("ApplicationUIControl [control=%s]", control);
	}
}
