package hflink.common.signals;

import java.util.Optional;

import hflink.common.items.ApplicationUIControl;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * /** Signal containing system control information provided by the operator via
 * the application user-interface
 * 
 * @author ModelerOne
 *
 */
public class ApplicationUIControlSignal extends SysMLSignal
{
	/**
	 * UI control data
	 */
	@Attribute
	public ApplicationUIControl control;

	/**
	 * Constructor for specified UI control
	 * 
	 * @param uiControl UI control value
	 */
	public ApplicationUIControlSignal(ApplicationUIControl uiControl)
	{
		super("ApplicationUIControl", 0L);
		this.control = uiControl;
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.empty());
	}

	@Override
	public String toString()
	{
		return String.format("ApplicationUIControlSignal [control=%s]", control);
	}
}
