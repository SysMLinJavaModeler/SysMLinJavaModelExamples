package hflink.components.switchrouter;

import java.util.HashMap;

/**
 * Map of IP addresses to ethernet port - for use by ethernet switch / IP router
 * to route IP packets to ethernet port that connects to corresponding IP
 * address. Map entries are added by calls to the {@code put()} method.
 * 
 * @author ModelerOne
 *
 */
public class IPAddressToEthernetPortMap extends HashMap<Integer, Integer>
{
	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = -3171005757832433632L;

	/**
	 * Constructor before addition of mappings
	 */
	public IPAddressToEthernetPortMap()
	{
		super();
	}

	/**
	 * Returns the ethernet port for the specified IP address
	 * 
	 * @param ipAddress IP address whose ethernet port is to be provided
	 * @return number of the ether port for the specified IP address
	 */
	public Integer ethernetPortFor(Integer ipAddress)
	{
		return get(ipAddress);
	}
}