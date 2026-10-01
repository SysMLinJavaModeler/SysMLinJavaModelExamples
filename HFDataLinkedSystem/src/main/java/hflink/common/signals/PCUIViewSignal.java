package hflink.common.signals;

import hflink.common.items.PCUIView;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal containing Operator's view information output from the PC's physical
 * user-interface (monitor) which encapsulates the operator's view information
 * output from the desktop user interface (windows).
 * 
 * @author ModelerOne
 *
 */
public class PCUIViewSignal extends SysMLSignal
{
	/**
	 * PC UI view value
	 */
	@Attribute
	public PCUIView view;

	/**
	 * Constructor for specified PC UI view value
	 * 
	 * @param view PC UI view value
	 */
	public PCUIViewSignal(PCUIView view)
	{
		super("PCUIView", 0L);
		this.view = view;
	}

	@Override
	public String stackNamesString()
	{
		return view.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("PCUIViewSignal [view=%s]", view);
	}

}
