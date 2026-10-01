package hflink.common.items;

import sysmlinjava.items.SysMLItem;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.metadata.Rationale;
import sysmlinjava.metadata.SysMLRationale;

/**
 * Representation of the information contained in the HTTP response
 * 
 * @author ModelerOne
 *
 */
public class HTTPResponseString extends SysMLItem
{
	/**
	 * Text represention of the response
	 */
	@Attribute
	public String text;
	/**
	 * Simulated address of the source of the response
	 */
	@Attribute
	public Integer ipSource;
	/**
	 * Simulated address of the destination of the response
	 */
	@Attribute
	public Integer ipDestination;
	/**
	 * Simulated port of the UDP
	 */
	@Attribute
	public Integer udpPort;

	/**
	 * Rationale for using a simulated IP address
	 */
	@Rationale
	public SysMLRationale simulatedIPAddress;

	/**
	 * Constructor
	 * 
	 * @param text string representaion, i.e. HTML
	 */
	public HTTPResponseString(String text)
	{
		super();
		this.text = text;
	}

	/**
	 * Constructor
	 */
	public HTTPResponseString()
	{
		super();
		this.text = "not specified";
	}

	public HTTPResponseString(HTTPResponseString response)
	{
		this.text = response.text;
	}

	@Override
	protected void createRationales()
	{
		simulatedIPAddress = new SysMLRationale("Simple integer used for simulated IPAddress as use of real IPAddress provides no significant value to model while adding unnecessary complexity model", "simpleIPAddressesOK", 0L);
	}

	@Override
	public String toString()
	{
		return String.format("HTTPResponseString [text=\n%s, ipSource=%s, ipDestination=%s, udpPort=%s]", text, ipSource, ipDestination, udpPort);
	}
}