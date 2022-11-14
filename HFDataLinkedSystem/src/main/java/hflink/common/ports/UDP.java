package hflink.common.ports;

import hflink.common.objects.HTTPRequest;
import hflink.common.objects.HTTPResponse;
import hflink.common.objects.UDPDatagram;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.requirements.RequirementSpecificationLink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port that simulates the UDP protocol as a component of the protocol stack
 * that communicates control and monitor data between the command/control and
 * deployed systems.
 * 
 * @author ModelerOne
 *
 */
public class UDP extends SysMLFullPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock block in whose context this port exists
	 * @param id           unique ID of the port
	 */
	public UDP(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	/**
	 * Standard spec for the protocol
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;

	@Override
	protected SysMLClass serverObjectFor(SysMLClass clientObject)
	{
		SysMLClass result = null;
		if (clientObject instanceof HTTPRequest)
			result = new UDPDatagram(0, 0, ((HTTPRequest)clientObject).ipSource, ((HTTPRequest)clientObject).ipDestination, (HTTPRequest)clientObject);
		else if (clientObject instanceof HTTPResponse)
			result = new UDPDatagram(0, 0, ((HTTPResponse)clientObject).ipSource, ((HTTPResponse)clientObject).ipDestination, (HTTPResponse)clientObject);
		else
			logger.warning("unexpected client object type: " + clientObject.getClass().getSimpleName());
		return result;
	}

	@Override
	protected SysMLClass clientObjectFor(SysMLClass serverObject)
	{
		SysMLClass result = null;
		if (serverObject instanceof UDPDatagram)
		{
			if(((UDPDatagram)serverObject).request != null)
				result = ((UDPDatagram)serverObject).request;
			else if(((UDPDatagram)serverObject).response != null)
				result = ((UDPDatagram)serverObject).response;
		}
		else
			logger.warning("unexpected serverObject type: " + serverObject.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createHyperlinks()
	{
		protocolStandard = new SysMLHyperlink("IETF RFC-768 User Datagram Protocol", "https://tools.ietf.org/html/rfc768");
	}
}
