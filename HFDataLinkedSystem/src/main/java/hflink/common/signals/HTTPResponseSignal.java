package hflink.common.signals;

import java.util.Optional;

import hflink.common.items.HTTPResponse;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal containing an HTTP response
 * 
 * @author ModelerOne
 *
 */
public class HTTPResponseSignal extends SysMLSignal
{
	/**
	 * HTTP response value
	 */
	@Attribute
	public HTTPResponse response;

	/**
	 * Constructor for specified HTTP response value
	 * 
	 * @param response HTTP response value
	 */
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
