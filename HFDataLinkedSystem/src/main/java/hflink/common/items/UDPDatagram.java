package hflink.common.items;

import java.util.Optional;

import sysmlinjava.items.SysMLItem;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.common.StackedProtocolObject;

/**
 * Representation of the datagram of the UDP protocol
 * 
 * @author ModelerOne
 *
 */
public class UDPDatagram extends SysMLItem implements StackedProtocolObject
{
	/**
	 * Integer port number from which the datagram is sent
	 */
	@Attribute
	public Integer sourcePort;
	/**
	 * Integer port number to which the datagram is sent
	 */
	@Attribute
	public Integer destinationPort;
	/**
	 * IP address from which the datagram is sent
	 */
	@Attribute
	public Integer sourceIPAddress;
	/**
	 * IP address to which the datagram is sent
	 */
	@Attribute
	public Integer destinationIPAddress;
	/**
	 * HTTPRequest carried by the data gram
	 */
	@Attribute
	public HTTPRequest request;
	/**
	 * HTTPResponse carried by the data gram
	 */
	@Attribute
	public HTTPResponse response;

	/**
	 * Constructor for HTTP request content, including HTTP protocol stack data
	 * 
	 * @param sourcePort           Integer port number from which the datagram is
	 *                             sent
	 * @param destinationPort      Integer port number to which the datagram is sent
	 * @param sourceIPAddress      IP address from which the datagram is sent
	 * @param destinationIPAddress IP address to which the datagram is sent
	 * @param request              HTTPRequest to be carried by the datagram
	 */
	public UDPDatagram(Integer sourcePort, Integer destinationPort, Integer sourceIPAddress, Integer destinationIPAddress, HTTPRequest request)
	{
		super();
		this.sourceIPAddress = sourceIPAddress;
		this.sourcePort = sourcePort;
		this.destinationIPAddress = destinationIPAddress;
		this.destinationPort = destinationPort;
		this.request = request;
	}

	/**
	 * Constructor for HTTP response content including HTTP protocol stack data
	 * 
	 * @param sourcePort           Integer port number from which the datagram is
	 *                             sent
	 * @param destinationPort      Integer port number to which the datagram is sent
	 * @param sourceIPAddress      IP address from which the datagram is sent
	 * @param destinationIPAddress IP address to which the datagram is sent
	 * @param response             HTTPResponse to be carried by the datagram
	 */
	public UDPDatagram(Integer sourcePort, Integer destinationPort, Integer sourceIPAddress, Integer destinationIPAddress, HTTPResponse response)
	{
		super();
		this.sourcePort = sourcePort;
		this.destinationPort = destinationPort;
		this.sourceIPAddress = sourceIPAddress;
		this.destinationIPAddress = destinationIPAddress;
		this.response = response;
	}

	/**
	 * Constructor for copy of specified datagram
	 * 
	 * @param datagram UDP datagram to be copy of
	 */
	public UDPDatagram(UDPDatagram datagram)
	{
		super();
		this.sourcePort = datagram.sourcePort;
		this.destinationPort = datagram.destinationPort;
		this.sourceIPAddress = datagram.sourceIPAddress;
		this.destinationIPAddress = datagram.destinationIPAddress;
		if (datagram.request != null)
			this.request = new HTTPRequest(datagram.request);
		if (datagram.response != null)
			this.response = new HTTPResponse(datagram.response);
	}

	/**
	 * Constructor of unspecified datagram content and protocol stack data
	 */
	public UDPDatagram()
	{
		super();
		this.sourcePort = 0;
		this.destinationPort = 0;
		this.sourceIPAddress = 0;
		this.destinationIPAddress = 0;
		this.response = null;
		this.response = null;
	}

	@Override
	public String stackNamesString()
	{
		if (request != null)
			return stackNamesString(this, Optional.of(request));
		else if (response != null)
			return stackNamesString(this, Optional.of(response));
		else
			return "<none>";
	}

	@Override
	public String toString()
	{
		return String.format("UDPDatagram [sourcePort=%s, destinationPort=%s, sourceIPAddress=%s, destinationIPAddress=%s, request=%s, response=%s]", sourcePort, destinationPort, sourceIPAddress, destinationIPAddress, request, response);
	}

}
