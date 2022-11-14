package hflink.common.signals;

import java.util.Optional;
import hflink.common.objects.ApplicationUIView;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

public class ApplicationUIViewSignal extends SysMLSignal
{
	@Attribute
	public ApplicationUIView view;

	public ApplicationUIViewSignal(ApplicationUIView view)
	{
		super("ApplicationUIView", 0L);
		this.view = view;
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
