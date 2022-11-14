package hflink.components.c2computer;

import java.util.Optional;
import hflink.common.objects.ApplicationUIControl;
import hflink.common.objects.ApplicationUIViewString;
import hflink.common.objects.DNS;
import hflink.common.objects.HTTPRequestString;
import hflink.common.objects.HTTPResponse;
import hflink.common.ports.ApplicationUIServerProtocol;
import hflink.common.ports.HTTPClient;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Reception;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.ports.SysMLFullPort;
import sysmlinjava.statemachine.FinalEvent;

/**
 * Port for simulated web browser that hosts the C2 application and communicates
 * the monitor and control data between the {@code CommandControlComputer} and
 * the {@code DeployedComputer}s
 * 
 * @author ModelerOne
 *
 */
public class WebBrowser extends SysMLFullPort
{
	/**
	 * Port (nested) for performing the HTTP with remote web servers on
	 * {@code DeployedComputer}s
	 */
	@FullPort
	public HTTPClient WebBrowserHTTP;
	/**
	 * Port (nested) for performing the application user-interface protocol with the
	 * system operator
	 */
	@FullPort
	public ApplicationUIServerProtocol application;

	/**
	 * Format strings for the HTML to be displayed via the application
	 * user-interface
	 */
	private static final String shutdownResponseFormatString = 
		"<!DOCTYPE html>%n"+
		"<html>%n"+
		"<head>%n"+
			"<meta charset=\"utf-8\">%n"+
			"<title>C2 system shutting down</title>%n"+
		"</head>%n"+
		"<body>%n"+
			"<h3>Command/Control system status:</h3>%n"+
			"<h3>System %s is shutting down</h3>%n"+
		"</body>%n"+
		"</html>%n";
	/**
	 * Constructor
	 * 
	 * @param contextBlock block in whose context the web browser exists
	 * @param id           unique ID
	 */
	public WebBrowser(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id, "");
	}

	/**
	 * Reception for reacting to HTTP response from remote {@code DeployedComputer}s
	 * 
	 * @param response HTTP response received by the web browser
	 */
	@Reception
	public void onHTTPResponse(HTTPResponse response)
	{
		ApplicationUIViewString viewString = new ApplicationUIViewString(response.response.text);
		application.transmit(viewString);
	}

	/**
	 * Reception for reacting to control data from operator via application
	 * user-interface
	 * 
	 * @param control control data from operator
	 */
	@Reception
	public void onApplicationUIControl(ApplicationUIControl control)
	{
		String c2HostName = ((CommandControlComputer)contextBlock.get()).hostName.value;
		if (!control.control.text.contains(c2HostName))
		{
			HTTPRequestString requestString = new HTTPRequestString(control.control.text);
			requestString.ipSource = DNS.ipAddressFor(c2HostName);
			requestString.ipDestination = DNS.ipAddressFor(requestString.text);
			WebBrowserHTTP.transmit(requestString);
		}
		else
		{
			if (control.control.text.contains("control=shutdown"))
			{
				String responseString = String.format(shutdownResponseFormatString, ((CommandControlComputer)contextBlock.get()).hostName.value);
				ApplicationUIViewString viewString = new ApplicationUIViewString(responseString);
				application.transmit(viewString);
				acceptEvent(new FinalEvent());
			}
		}
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new WebBrowserStateMachine(this));
	}

	@Override
	protected void createFullPorts()
	{
		WebBrowserHTTP = new HTTPClient(this, this, 0L);
		application = new ApplicationUIServerProtocol(this, this, 0L);
	}
}
