package hflink.common.items;

import java.util.Optional;

import sysmlinjava.items.SysMLItem;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.common.StackedProtocolObject;

/**
 * System control information provided by the operator via the application
 * user-interface
 * 
 * @author ModelerOne
 */
public class ApplicationUIControl extends SysMLItem implements StackedProtocolObject
{
	/**
	 * String representation of the control information
	 */
	@Attribute
	public ApplicationUIControlString control;

	/**
	 * Constructor for specified control string
	 * 
	 * @param control string representation of the control information
	 */
	public ApplicationUIControl(ApplicationUIControlString control)
	{
		super();
		this.control = control;
	}

	/**
	 * Constructor for empty control string
	 */
	public ApplicationUIControl()
	{
		this.control = new ApplicationUIControlString();
	}

	/**
	 * Constructor for copy of specified control
	 * 
	 * @param control control to be copy of
	 */
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
