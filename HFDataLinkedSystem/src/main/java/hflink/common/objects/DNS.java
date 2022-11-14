package hflink.common.objects;

import java.util.HashMap;
import java.util.Map;
import sysmlinjava.annotations.Operation;
import sysmlinjava.common.SysMLClass;

/**
 * Domain Name Service (DNS) simulation for host-to-IP address mapping of HF
 * Datalinked System network nodes.
 * 
 * @author ModelerOne
 *
 */
public class DNS extends SysMLClass
{
	/**
	 * Map of host names to IP addresses
	 */
	public static Map<String, Integer> hostNameToIPAddressMap = new HashMap<>();
	static
	{
		hostNameToIPAddressMap.put("www.hflinkedc2.com", 0);
		hostNameToIPAddressMap.put("www.hflinkeddep1.com", 1);
		hostNameToIPAddressMap.put("www.hflinkeddep2.com", 2);
		hostNameToIPAddressMap.put("www.hflinkeddep3.com", 3);
	}

	/**
	 * Returns (simplified) IP address for the specified host name
	 * 
	 * @param hostName name of host to be mapped
	 * @return mapped IP address
	 */
	@Operation
	public static Integer ipAddressFor(String hostName)
	{
		int beginIndex = hostName.indexOf("www.");
		int endIndex = hostName.indexOf(".com") + 4;
		String host = hostName.substring(beginIndex, endIndex);
		return hostNameToIPAddressMap.get(host);
	}
}
