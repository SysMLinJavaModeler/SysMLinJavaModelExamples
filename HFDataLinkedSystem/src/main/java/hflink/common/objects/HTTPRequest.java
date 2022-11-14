package hflink.common.objects;

import java.util.Optional;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;

/**
 * Representation of the HTTP request
 * 
 * @author ModelerOne
 *
 */
public class HTTPRequest extends SysMLClass implements StackedProtocolObject
{
	/**
	 * String representation of the request, i.e. HTTP Get, Put text
	 */
	@Attribute
	public HTTPRequestString requestText;
	/**
	 * IP address of the source of the request (web browser)
	 */
	@Attribute
	public Integer ipSource;
	/**
	 * IP address of the destination of the request (web server)
	 */
	@Attribute
	public Integer ipDestination;
	/**
	 * UDP port of the web server
	 */
	@Attribute
	public Integer udpPort;

	/**
	 * Constructor
	 * 
	 * @param requestText string containing the request
	 */
	public HTTPRequest(HTTPRequestString requestText)
	{
		super();
		this.requestText = requestText;
		this.ipSource = 0;
		this.ipDestination = 0;
		this.udpPort = 0;
	}

	public HTTPRequest(HTTPRequest httpRequest)
	{
		super();
		this.requestText = new HTTPRequestString(httpRequest.requestText);
		this.ipSource = httpRequest.ipSource;
		this.ipDestination = httpRequest.ipDestination;
		this.udpPort = httpRequest.udpPort;
	}

	public HTTPRequest()
	{
		super();
		this.requestText = new HTTPRequestString();
		this.ipSource = 0;
		this.ipDestination = 0;
		this.udpPort = 0;
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.empty());
	}

	@Override
	public String toString()
	{
		return String.format("HTTPRequest [request=%s]", requestText);
	}
}
