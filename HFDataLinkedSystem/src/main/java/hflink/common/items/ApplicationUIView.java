package hflink.common.items;

import java.util.Optional;

import sysmlinjava.items.SysMLItem;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.common.StackedProtocolObject;

/**
 * System view information provided to the operator by the application
 * user-interface
 * 
 * @author ModelerOne
 */
public class ApplicationUIView extends SysMLItem implements StackedProtocolObject
{
	/**
	 * String representation of the system view information
	 */
	@Attribute
	public ApplicationUIViewString view;

	/**
	 * Constructor
	 * 
	 * @param view string representation of the system view information
	 */
	public ApplicationUIView(ApplicationUIViewString view)
	{
		super();
		this.view = view;
	}

	/**
	 * Constructor
	 * 
	 * @param system view information
	 */
	public ApplicationUIView(ApplicationUIView view)
	{
		this.view = new ApplicationUIViewString(view.view);
	}

	/**
	 * Constructor
	 */
	public ApplicationUIView()
	{
		this.view = new ApplicationUIViewString();
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.empty());
	}

	@Override
	public String toString()
	{
		return String.format("ApplicationUIView [view=%s]", view);
	}
}
