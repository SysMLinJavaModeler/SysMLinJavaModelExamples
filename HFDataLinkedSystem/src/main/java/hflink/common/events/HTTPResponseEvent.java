package hflink.common.events;

import hflink.common.objects.HTTPResponse;
import hflink.common.signals.HTTPResponseSignal;
import sysmlinjava.events.SysMLSignalEvent;

/**
 * Event for the receipt of an HTTP response
 * 
 * @author ModelerOne
 *
 */
public class HTTPResponseEvent extends SysMLSignalEvent
{
	public HTTPResponseEvent(HTTPResponse httpResponse)
	{
		super("httpResponse");
		((HTTPResponseSignal)signal).response = new HTTPResponse(httpResponse);
	}

	public HTTPResponseEvent(HTTPResponseSignal signal)
	{
		super("HTTPResponse");
		((HTTPResponseSignal)signal).response = new HTTPResponse(signal.response);
	}

	public HTTPResponse getResponse()
	{
		return ((HTTPResponseSignal)signal).response;
	}

	@Override
	public String toString()
	{
		return String.format("HTTPResponseEvent [signal=%s]", signal);
	}

	@Override
	public void createSignal()
	{
		signal = new HTTPResponseSignal(new HTTPResponse());
	}
}
