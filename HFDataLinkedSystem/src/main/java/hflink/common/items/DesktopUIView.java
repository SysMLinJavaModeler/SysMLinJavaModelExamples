package hflink.common.items;

import java.util.Optional;

import sysmlinjava.items.SysMLItem;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.common.StackedProtocolObject;

/**
 * Operator's view information output from the PC desktop user-interface which
 * encapsulates the operator's view information output from the application user
 * interface.
 * 
 * @author ModelerOne
 *
 */
public class DesktopUIView extends SysMLItem implements StackedProtocolObject
{
	/**
	 * Application user-interface view information
	 */
	@Attribute
	public ApplicationUIView view;

	/**
	 * Constructor
	 * 
	 * @param view application user-interface view information
	 */
	public DesktopUIView(ApplicationUIView view)
	{
		super();
		this.view = view;
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.of(view));
	}

	@Override
	public String toString()
	{
		return String.format("DesktopUIView [view=%s]", view);
	}
}
