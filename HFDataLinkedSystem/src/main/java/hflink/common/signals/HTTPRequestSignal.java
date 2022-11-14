package hflink.common.signals;

import java.util.Optional;
import hflink.common.objects.HTTPRequest;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

public class HTTPRequestSignal extends SysMLSignal
{
	@Attribute
	public HTTPRequest request;

	public HTTPRequestSignal(HTTPRequest request)
	{
		super("HTTPRequest", 0L);
		this.request = request;
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.empty());
	}

	@Override
	public String toString()
	{
		return String.format("HTTPRequestSignal [request=%s]", request);
	}
}
