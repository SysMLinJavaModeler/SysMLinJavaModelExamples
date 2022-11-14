package hflink.common.objects;

import java.util.Optional;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;

/**
 * System view information provided to the operator by the application
 * user-interface
 * 
 * @author ModelerOne
 *
 */
public class ApplicationUIView extends SysMLClass implements StackedProtocolObject
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

	public ApplicationUIView(ApplicationUIView view)
	{
		this.view = new ApplicationUIViewString(view.view);
	}

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
