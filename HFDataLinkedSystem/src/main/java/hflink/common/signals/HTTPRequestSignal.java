package hflink.common.signals;

import java.util.Optional;

import hflink.common.items.HTTPRequest;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal containing an HTTP request
 * 
 * @author ModelerOne
 *
 */
public class HTTPRequestSignal extends SysMLSignal
{
	/**
	 * HTTP request value
	 */
	@Attribute
	public HTTPRequest request;

	/**
	 * Constructor for specified HTTP request value
	 * 
	 * @param request HTTP request value
	 */
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
