package hflink.components.deployedcomputer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import hflink.common.items.DNS;
import hflink.common.items.HTTPRequest;
import hflink.common.items.HTTPResponseString;
import hflink.common.ports.HTTPServer;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.javaannotations.requirements.RequirementInterfaceProtocol;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;
import sysmlinjava.states.FinalEvent;

/**
 * Port for simulated web server that hosts the C2 server application and
 * communicates the monitor and control data between the
 * {@code DeployedComputer}s and the {@code CommandControlComputer}
 * 
 * @author ModelerOne
 *
 */
public class WebServer extends SysMLPort
{
	/**
	 * Port (nested) for performing the HTTP with remote web browser on the
	 * {@code CommandControlComputer}
	 */
	@RequirementInterfaceProtocol
	@Port
	public HTTPServer WebServerHTTP;

	/**
	 * Constructor
	 * 
	 * @param context part in whose context this web server exists
	 * @param id      unique ID
	 */
	public WebServer(SysMLPart context, Long id)
	{
		super(context, id);
	}

	/**
	 * Format string for HTML for HTTP response to unrecognized HTTP request
	 */
	private static final String unrecognizedRequestFormatString = "<!DOCTYPE html>" + "<html>" + "<head>" + "<meta charset=\"utf-8\">" + "<title>Unrecognized request</title>" + "</head>" + "<body>"
		+ "<h3>System %s received unrecognized request</h3>" + "<h3>Unrecognized request at %s</h3>" + "<p><pre>%s</pre></p>" + "</body>" + "</html>";
	/**
	 * Format string for HTML for HTTP response to status request
	 */
	private static final String statusResponseFormatString = "<!DOCTYPE html>" + "<html>" + "<head>" + "<meta charset=\"utf-8\">" + "<title>Deployed system status</title>" + "</head>" + "<body>" + "<h3>System %s is A-OK!</h3>"
		+ "<h3>Deployed system status as of %s</h3>" + "<ul>" + "<li>Available Capacity: 30%%</li>" + "<li>Power: 250 watts</li>" + "<li>Temperature: 35C</li>" + "<li>Faults: 0</li>" + "</ul>" + "</body>" + "</html>";

	/**
	 * Format string for HTML for HTTP response to shutdown request
	 */
	private static final String shutdownResponseFormatString = "<!DOCTYPE html>" + "<html>" + "<head>" + "<meta charset=\"utf-8\">" + "<title>Deployed system shutting down</title>" + "</head>" + "<body>"
		+ "<h3>Deployed system status at %s</h3>" + "<h3>System %s is shutting down</h3>" + "</body>" + "</html>";

	@Override
	public void receive(SysMLAnything object)
	{
		WebServerHTTP.receive(object);
	}

	/**
	 * Reception for reaction to an HTTP request
	 * 
	 * @param request HTTP request
	 */
	@Action
	public void onHTTPRequest(HTTPRequest request)
	{
		HTTPResponseString responseString = new HTTPResponseString();
		responseString.ipSource = DNS.ipAddressFor(((DeployedComputer)context.get()).hostName.value);
		responseString.ipDestination = request.ipSource;
		responseString.udpPort = 0;

		int beginIndex = request.requestText.text.indexOf(".com/") + 5;
		String requestText = request.requestText.text.substring(beginIndex);
		if (requestText.contains("control=sendstatus"))
		{
			responseString.text = String.format(statusResponseFormatString, ((DeployedComputer)context.get()).hostName.value, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")));
			WebServerHTTP.transmit(responseString);
		}
		else if (requestText.contains("control=shutdown"))
		{
			responseString.text = String.format(shutdownResponseFormatString, ((DeployedComputer)context.get()).hostName.value, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")));
			WebServerHTTP.transmit(responseString);
			acceptEvent(new FinalEvent());
		}
		else
		{
			responseString.text = String.format(unrecognizedRequestFormatString, ((DeployedComputer)context.get()).hostName.value, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")), requestText);
			WebServerHTTP.transmit(responseString);
		}
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new WebServerStateMachine(this));
	}

	@Override
	protected void createPorts()
	{
		WebServerHTTP = new HTTPServer(this, this, 0L);
	}

	@Override
	public void connectToServerPort(SysMLPort server)
	{
		WebServerHTTP.connectToServerPort(server);
	}

}
