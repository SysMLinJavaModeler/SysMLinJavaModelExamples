package hflink.common.signals;

import hflink.common.objects.PCUIControl;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

/**
 * Signal for control information submitted by the operator via the PC
 * user-interface
 * 
 * @author ModelerOne
 *
 */
public class PCUIControlSignal extends SysMLSignal
{
	@Attribute
	public PCUIControl control;

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
