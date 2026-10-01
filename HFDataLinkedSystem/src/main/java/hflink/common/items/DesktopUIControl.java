package hflink.common.items;

import java.util.Optional;

import sysmlinjava.items.SysMLItem;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.common.StackedProtocolObject;

/**
 * Operator's control information input via the PC desktop user-interface which
 * encapsulates the operator's control information input via the application
 * user interface.
 * 
 * @author ModelerOne
 *
 */
public class DesktopUIControl extends SysMLItem implements StackedProtocolObject
{
	/**
	 * Application user-interface control information
	 */
	@Attribute
	public ApplicationUIControl control;

	/**
	 * Constructor
	 * 
	 * @param control appliation user-interface control information
	 */
	public DesktopUIControl(ApplicationUIControl control)
	{
		super();
		this.control = control;
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.of(control));
	}

	@Override
	public String toString()
	{
		return String.format("DesktopUIControl [control=%s]", control);
	}
}
