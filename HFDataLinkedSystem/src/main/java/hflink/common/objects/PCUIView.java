package hflink.common.objects;

import java.util.Optional;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;

/**
 * Operator's view information output from the PC's physical user-interface
 * (monitor) which encapsulates the operator's view information output from the
 * desktop user interface (windows).
 * 
 * @author ModelerOne
 *
 */
public class PCUIView extends SysMLClass implements StackedProtocolObject
{
	/**
	 * Desktop user-interface view information
	 */
	@Attribute
	public DesktopUIView view;

	/**
	 * Constructor
	 * 
	 * @param view desktop user-interface view information
	 */
	public PCUIView(DesktopUIView view)
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
		return String.format("PCUIView [view=%s]", view);
	}
}
