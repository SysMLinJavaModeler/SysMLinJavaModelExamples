package hflink.common.signals;

import java.util.Optional;
import hflink.common.objects.ApplicationUIControl;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

public class ApplicationUIControlSignal extends SysMLSignal
{
	@Attribute
	public ApplicationUIControl control;

	public ApplicationUIControlSignal(ApplicationUIControl control)
	{
		super("ApplicationUIControl", 0L);
		this.control = control;
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
