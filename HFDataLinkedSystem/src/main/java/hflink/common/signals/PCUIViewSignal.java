package hflink.common.signals;

import hflink.common.objects.PCUIView;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

/**
 * Signal for view (displayed) information provided by the PC user-interface to
 * the operator
 * 
 * @author ModelerOne
 *
 */
public class PCUIViewSignal extends SysMLSignal
{
	@Attribute
	public PCUIView view;

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
