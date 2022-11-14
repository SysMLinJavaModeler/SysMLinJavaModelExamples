package hflink.common.signals;

import java.util.Optional;
import hflink.common.objects.HTTPResponse;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

public class HTTPResponseSignal extends SysMLSignal
{
	@Attribute
	public HTTPResponse response;

	public HTTPResponseSignal(HTTPResponse response)
	{
		super("HTTPResponse", 0L);
		this.response = response;
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.empty());
	}

	@Override
	public String toString()
	{
		return String.format("HTTPResponseSignal [response=%s]", response);
	}
}
