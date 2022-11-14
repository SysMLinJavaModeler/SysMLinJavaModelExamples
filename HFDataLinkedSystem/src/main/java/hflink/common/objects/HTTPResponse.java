package hflink.common.objects;

import java.util.Optional;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;

/**
 * Representation of the HTTP response
 * 
 * @author ModelerOne
 *
 */
public class HTTPResponse extends SysMLClass implements StackedProtocolObject
{
	/**
	 * String representation of the response, i.e. HTML
	 */
	@Attribute
	public HTTPResponseString response;
	/**
	 * IP address of the packet's source (web server)
	 */
	@Attribute
	public Integer ipSource;
	/**
	 * IP address of the packet's destination (web browser)
	 */
	@Attribute
	public Integer ipDestination;
	/**
	 * UDP port of the browser
	 */
	@Attribute
	public Integer udpPort;

	/**
	 * Constructor
	 * 
	 * @param response string representation of the response
	 */
	public HTTPResponse(HTTPResponseString response)
	{
		super();
		this.response = new HTTPResponseString(response);
		this.ipSource = 0;
		this.ipDestination = 0;
		this.udpPort = 0;
	}

	public HTTPResponse(HTTPResponse response)
	{
		super();
		this.response = new HTTPResponseString(response.response);
		this.ipSource = response.ipSource;
		this.ipDestination = response.ipDestination;
		this.udpPort = response.udpPort;		
	}

	public HTTPResponse()
	{
		this.response = new HTTPResponseString();
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
		return String.format("HTTPResponse [response=%s, ipSource=%s, ipDestination=%s, udpPort=%s]", response, ipSource, ipDestination, udpPort);
	}
}
