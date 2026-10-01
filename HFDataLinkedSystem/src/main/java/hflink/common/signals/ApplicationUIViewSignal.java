package hflink.common.signals;

import java.util.Optional;

import hflink.common.items.ApplicationUIView;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal containing the system view information provided to the operator by the
 * application user-interface
 * 
 * @author ModelerOne
 *
 */
public class ApplicationUIViewSignal extends SysMLSignal
{
	/**
	 * UI view data
	 */
	@Attribute
	public ApplicationUIView view;

	/**
	 * Constructor for the specified UI view
	 * 
	 * @param uiView UI view value
	 */
	public ApplicationUIViewSignal(ApplicationUIView uiView)
	{
		super("ApplicationUIView", 0L);
		this.view = uiView;
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.empty());
	}

	@Override
	public String toString()
	{
		return String.format("ApplicationUIViewSignal [view=%s]", view);
	}
}
