package hflink.common.signals;

import hflink.common.items.PCUIControl;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal containing Operator's control information input via the PC's physical
 * user-interface which encapsulates the operator's control information input
 * via the desktop user interface.
 * 
 * @author ModelerOne
 *
 */
public class PCUIControlSignal extends SysMLSignal
{
	/**
	 * PC UI control value
	 */
	@Attribute
	public PCUIControl control;

	/**
	 * Constructor for specified PC UI control value
	 * 
	 * @param control PC UI control value
	 */
	public PCUIControlSignal(PCUIControl control)
	{
		super("PCUIControl", 0L);
		this.control = control;
	}

	@Override
	public String stackNamesString()
	{
		return control.stackNamesString();
	}

	@Override
	public String toString()
	{
		return String.format("PCUIControlSignal [control=%s]", control);
	}
}
