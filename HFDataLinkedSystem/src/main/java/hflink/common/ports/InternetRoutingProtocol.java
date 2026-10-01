package hflink.common.ports;

import hflink.common.items.IPPacket;
import hflink.components.switchrouter.EthernetSwitchIPRouter;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.requirements.RequirementSpecificationLink;
import sysmlinjava.ports.SysMLPort;

/**
 * Port to simulate the protocol to route IP packets between ethernet ports
 * 
 * @author ModelerOne
 *
 */
public class InternetRoutingProtocol extends SysMLPort
{
	/**
	 * Standard that specifies the protocol
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;

	/**
	 * Constructor
	 * 
	 * @param context block (web server) in whose context the port resides
	 * @param id           unique ID
	 */
	public InternetRoutingProtocol(EthernetSwitchIPRouter context, Long id)
	{
		super(context, id);
	}

	public void transmit(SysMLAnything object)
	{
		if (object instanceof IPPacket)
		{
			IPPacket packet = (IPPacket)object;
			EthernetSwitchIPRouter switchRouter = (EthernetSwitchIPRouter)context.get();
			Integer ethernetPortIndex = switchRouter.ipToEthernetMap.ethernetPortFor(packet.destinationAddress);
			EthernetProtocol ethernetPort = (EthernetProtocol)connectedPortsServers.get(ethernetPortIndex);
			ethernetPort.transmit(packet);
		}
		else
			logger.severe("unrecognized object type: " + object.getClass().getName());
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		protocolStandard = new SysMLHyperlink("IETF RFC-1723 Routing Information Protocol", "https://tools.ietf.org/html/rfc1723");
	}
}
