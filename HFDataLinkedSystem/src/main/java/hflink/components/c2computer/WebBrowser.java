package hflink.components.c2computer;

import java.util.Optional;

import hflink.common.items.ApplicationUIControl;
import hflink.common.items.ApplicationUIViewString;
import hflink.common.items.DNS;
import hflink.common.items.HTTPRequestString;
import hflink.common.items.HTTPResponse;
import hflink.common.ports.ApplicationUIServerProtocol;
import hflink.common.ports.HTTPClient;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.javaannotations.requirements.RequirementInterfaceProtocol;
import sysmlinjava.javaannotations.requirements.RequirementSpecificationLink;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;
import sysmlinjava.states.FinalEvent;

/**
 * Port for simulated web browser that hosts the C2 application and communicates
 * the monitor and control data between the {@code CommandControlComputer} and
 * the {@code DeployedComputer}s
 * 
 * @author ModelerOne
 *
 */
public class WebBrowser extends SysMLPort
{
	/**
	 * Port (nested) for performing the HTTP with remote web servers on
	 * {@code DeployedComputer}s
	 */
	@RequirementInterfaceProtocol
	@Port
	public HTTPClient WebBrowserHTTP;
	@Override
	public void connectToServerPort(int index, SysMLPort server)
	{
		// TODO Auto-generated method stub
		super.connectToServerPort(index, server);
	}

	/**
	 * Port (nested) for performing the application user-interface protocol with the
	 * system operator
	 */
	@RequirementInterfaceProtocol
	@Port
	public ApplicationUIServerProtocol application;
	
	/**
	 * Standard that specifies the web browser
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;

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
	public WebBrowser(SysMLPart contextBlock, Long id)
	{
		super(contextBlock, id, "");
	}

	@Override
	public void receive(SysMLAnything object)
	{
		WebBrowserHTTP.receive(object);
	}

	/**
	 * Reception for reacting to HTTP response from remote {@code DeployedComputer}s
	 * 
	 * @param response HTTP response received by the web browser
	 */
	@Action
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
	@Action
	public void onApplicationUIControl(ApplicationUIControl control)
	{
		String c2HostName = ((CommandControlComputer)context.get()).hostName.value;
		if (!control.control.text.contains(c2HostName))
		{
			HTTPRequestString requestString = new HTTPRequestString(control.control.text);
			requestString.ipSource = DNS.ipAddressFor(c2HostName);
			requestString.ipDestination = DNS.ipAddressFor(requestString.text);
			WebBrowserHTTP.transmit(requestString);
		} else if (control.control.text.contains("control=shutdown"))
		{
			String responseString = String.format(shutdownResponseFormatString, ((CommandControlComputer)context.get()).hostName.value);
			ApplicationUIViewString viewString = new ApplicationUIViewString(responseString);
			application.transmit(viewString);
			acceptEvent(new FinalEvent());
		}
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new WebBrowserStateMachine(this));
	}

	@Override
	protected void createPorts()
	{
		WebBrowserHTTP = new HTTPClient(this, this, 0L);
		application = new ApplicationUIServerProtocol(this, this, 0L);
	}

	@Override
	public void connectToServerPort(SysMLPort server)
	{
		WebBrowserHTTP.connectToServerPort(server);
	}
	
	@Override
	protected void createSupportingInformationLinks()
	{
		protocolStandard = new SysMLHyperlink("W3C Standards", "https://www.w3.org/TR/");
	}
}
