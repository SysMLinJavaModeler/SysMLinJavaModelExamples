package hflink.common.events;

import hflink.common.objects.HTTPRequest;
import hflink.common.signals.HTTPRequestSignal;
import sysmlinjava.events.SysMLSignalEvent;

/**
 * Event for the receipt of an HTTP request
 * 
 * @author ModelerOne
 *
 */
public class HTTPRequestEvent extends SysMLSignalEvent
{
	public HTTPRequestEvent(HTTPRequest httpRequest)
	{
		super("HTTPRequest");
		((HTTPRequestSignal)signal).request = new HTTPRequest(httpRequest);
	}

	public HTTPRequestEvent(HTTPRequestSignal signal)
	{
		super("HTTPRequest");
		((HTTPRequestSignal)signal).request = new HTTPRequest(signal.request);
	}

	public HTTPRequest getRequest()
	{
		return ((HTTPRequestSignal)signal).request;
	}

	@Override
	public String toString()
	{
		return String.format("HTTPRequestEvent [signal=%s]", signal);
	}

	@Override
	public void createSignal()
	{
		signal = new HTTPRequestSignal(new HTTPRequest());
	}
}
