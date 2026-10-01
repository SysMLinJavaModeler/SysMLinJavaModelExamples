package hflink.common.ports;

import hflink.common.items.HTTPRequest;
import hflink.common.items.HTTPResponse;
import hflink.common.items.UDPDatagram;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.requirements.RequirementSpecificationLink;
import sysmlinjava.ports.SysMLPort;
import sysmlinjava.states.StateBehaviorContext;

/**
 * Port that simulates the UDP protocol as a component of the protocol stack
 * that communicates control and monitor data between the command/control and
 * deployed systems.
 * 
 * @author ModelerOne
 *
 */
public class UDP extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param context part or port in whose context this port exists
	 * @param id      unique ID of the port
	 */
	public UDP(StateBehaviorContext context, Long id)
	{
		super(context, id);
	}

	/**
	 * Standard spec for the protocol
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;

	@Override
	protected SysMLAnything serverObjectFor(SysMLAnything clientObject)
	{
		SysMLAnything result = null;
		if (clientObject instanceof HTTPRequest)
			result = new UDPDatagram(0, 0, ((HTTPRequest)clientObject).ipSource, ((HTTPRequest)clientObject).ipDestination, (HTTPRequest)clientObject);
		else if (clientObject instanceof HTTPResponse)
			result = new UDPDatagram(0, 0, ((HTTPResponse)clientObject).ipSource, ((HTTPResponse)clientObject).ipDestination, (HTTPResponse)clientObject);
		else
			logger.warning("unexpected client object type: " + clientObject.getClass().getSimpleName());
		return result;
	}

	@Override
	protected SysMLAnything clientObjectFor(SysMLAnything serverObject)
	{
		SysMLAnything result = null;
		if (serverObject instanceof UDPDatagram)
		{
			if (((UDPDatagram)serverObject).request != null)
				result = ((UDPDatagram)serverObject).request;
			else if (((UDPDatagram)serverObject).response != null)
				result = ((UDPDatagram)serverObject).response;
		}
		else
			logger.warning("unexpected serverObject type: " + serverObject.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		protocolStandard = new SysMLHyperlink("IETF RFC-768 User Datagram Protocol", "https://tools.ietf.org/html/rfc768");
	}
}
