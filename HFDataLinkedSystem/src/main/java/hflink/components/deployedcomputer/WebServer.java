package hflink.components.deployedcomputer;

import java.time.LocalDateTime;
import java.util.Optional;
import hflink.common.objects.DNS;
import hflink.common.objects.HTTPRequest;
import hflink.common.objects.HTTPResponseString;
import hflink.common.ports.HTTPServer;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.requirements.RequirementInterfaceProtocol;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.ports.SysMLFullPort;
import sysmlinjava.statemachine.FinalEvent;

/**
 * Port for simulated web server that hosts the C2 server application and
 * communicates the monitor and control data between the
 * {@code DeployedComputer}s and the {@code CommandControlComputer}
 * 
 * @author ModelerOne
 *
 */
public class WebServer extends SysMLFullPort
{
	/**
	 * Port (nested) for performing the HTTP with remote web browser on the
	 * {@code CommandControlComputer}
	 */
	@RequirementInterfaceProtocol
	@FullPort
	public HTTPServer WebServerHTTP;

	/**
	 * Constructor
	 * 
	 * @param contextBlock block in whose context this web server exists
	 * @param id           unique ID
	 */
	public WebServer(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	/**
	 * Format string for HTML for HTTP response to unrecognized HTTP request
	 */
	private static final String unrecognizedRequestFormatString =
		"<!DOCTYPE html>"+
		"<html>"+
			"<head>"+
				"<meta charset=\"utf-8\">"+
				"<title>Unrecognized request</title>"+
			"</head>"+
			"<body>"+
				"<h3>System %s received unrecognized request</h3>"+
				"<h3>Unrecognized request at %s</h3>"+
				"<p><pre>%s</pre></p>"+
			"</body>"+
		"</html>";
	/**
	 * Format string for HTML for HTTP response to status request
	 */
	private static final String statusResponseFormatString =
		"<!DOCTYPE html>"+
		"<html>"+
			"<head>"+
				"<meta charset=\"utf-8\">"+
				"<title>Deployed system status</title>"+
			"</head>"+
			"<body>"+
				"<h3>System %s is A-OK!</h3>"+
				"<h3>Deployed system status as of %s</h3>"+
				"<ul>"+
					"<li>Available Capacity: 30%%</li>"+
					"<li>Power: 250 watts</li>"+
					"<li>Temperature: 35C</li>"+
					"<li>Faults: 0</li>"+
				"</ul>"+
			"</body>"+
		"</html>";

	/**
	 * Format string for HTML for HTTP response to shutdown request
	 */
	private static final String shutdownResponseFormatString =
		"<!DOCTYPE html>"+
		"<html>"+
			"<head>"+
				"<meta charset=\"utf-8\">"+
				"<title>Deployed system shutting down</title>"+
			"</head>"+
			"<body>"+
				"<h3>Deployed system status at %s</h3>"+
				"<h3>System %s is shutting down</h3>"+
			"</body>"+
		"</html>";

	/**
	 * Reception for reaction to an HTTP request
	 * 
	 * @param request HTTP request
	 */
	@Reception
	public void onHTTPRequest(HTTPRequest request)
	{
		HTTPResponseString responseString = new HTTPResponseString();
		responseString.ipSource = DNS.ipAddressFor(((DeployedComputer)contextBlock.get()).hostName.value);
		responseString.ipDestination = request.ipSource;
		responseString.udpPort = 0;

		int beginIndex = request.requestText.text.indexOf(".com/") + 5;
		String requestText = request.requestText.text.substring(beginIndex);
		if (requestText.contains("control=sendstatus"))
		{
			responseString.text = String.format(statusResponseFormatString, ((DeployedComputer)contextBlock.get()).hostName.value, LocalDateTime.now().toString());
			WebServerHTTP.transmit(responseString);
		}
		else if (requestText.contains("control=shutdown"))
		{
			responseString.text = String.format(shutdownResponseFormatString, ((DeployedComputer)contextBlock.get()).hostName.value, LocalDateTime.now().toString());
			WebServerHTTP.transmit(responseString);
			acceptEvent(new FinalEvent());
		}
		else
		{
			responseString.text = String.format(unrecognizedRequestFormatString, ((DeployedComputer)contextBlock.get()).hostName.value, LocalDateTime.now().toString(), requestText);
			WebServerHTTP.transmit(responseString);
		}
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new WebServerStateMachine(this));
	}

	@Override
	protected void createFullPorts()
	{
		WebServerHTTP = new HTTPServer(this, this, 0L);
	}
}
