package hflink.common.objects;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.annotations.comments.Rationale;
import sysmlinjava.comments.SysMLRationale;
import sysmlinjava.common.SysMLClass;

/**
 * Text representation of the data contained in an HTTP request
 * 
 * @author ModelerOne
 *
 */
public class HTTPRequestString extends SysMLClass
{
	/**
	 * String representing the request
	 */
	@Attribute
	public String text;
	/**
	 * Integer representation of the request source's IP address
	 */
	@Attribute
	public Integer ipSource;
	/**
	 * Integer representation of the request destination's IP address
	 */
	@Attribute
	public Integer ipDestination;
	/**
	 * Integer representation of the request destination's UDP port number 
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
	 * @param text string representing the request
	 */
	public HTTPRequestString(String text)
	{
		super();
		this.text = text;
		this.ipSource = 0;
		this.ipDestination = 0;
		this.udpPort = 0;
	}

	public HTTPRequestString(HTTPRequestString requestText)
	{
		super();
		this.text = requestText.text;
		this.ipSource = 0;
		this.ipDestination = 0;
		this.udpPort = 0;
	}

	public HTTPRequestString()
	{
		super();
		this.text = "not specified";
		this.ipSource = 0;
		this.ipDestination = 0;
		this.udpPort = 0;
	}

	@Override
	protected void createRationales()
	{
		simulatedIPAddress = new SysMLRationale("Simple integer used for simulated IPAddress as use of real IPAddress provides no significant value to model while adding unnecessary complexity model");
	}

	@Override
	public String toString()
	{
		return String.format("HTTPRequestString [text=%s]", text);
	}
}