package hflink.common.items;

import java.util.Optional;

import sysmlinjava.items.SysMLItem;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.common.StackedProtocolObject;

/**
 * Operator's control information input via the PC's physical user-interface
 * which encapsulates the operator's control information input via the desktop
 * user interface.
 * 
 * @author ModelerOne
 *
 */
public class PCUIControl extends SysMLItem implements StackedProtocolObject
{
	/**
	 * Desktop user-interface control information
	 */
	@Attribute
	public DesktopUIControl control;

	/**
	 * Constructor
	 * 
	 * @param control desktop user interface control information
	 */
	public PCUIControl(DesktopUIControl control)
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
		return String.format("PCUIControl [control=%s]", control);
	}
}
