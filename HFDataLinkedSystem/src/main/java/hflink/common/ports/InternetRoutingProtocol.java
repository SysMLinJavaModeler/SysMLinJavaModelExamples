package hflink.common.ports;

import hflink.common.objects.IPPacket;
import hflink.components.switchrouter.EthernetSwitchIPRouter;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.requirements.RequirementSpecificationLink;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port to simulate the protocol to route IP packets between ethernet ports
 * 
 * @author ModelerOne
 *
 */
public class InternetRoutingProtocol extends SysMLFullPort
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
	 * @param contextBlock block (web server) in whose context the port resides
	 * @param id           unique ID
	 */
	public InternetRoutingProtocol(EthernetSwitchIPRouter contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	public void transmit(SysMLClass object)
	{
		if (object instanceof IPPacket)
		{
			IPPacket packet = (IPPacket)object;
			EthernetSwitchIPRouter switchRouter = (EthernetSwitchIPRouter)contextBlock.get();
			Integer ethernetPortIndex = switchRouter.ipToEthernetMap.ethernetPortFor(packet.destinationAddress);
			EthernetProtocol ethernetPort = (EthernetProtocol)connectedPortsServers.get(ethernetPortIndex);
			ethernetPort.transmit(packet);
		}
		else
			logger.severe("unrecognized object type: " + object.getClass().getName());
	}

	@Override
	protected void createHyperlinks()
	{
		protocolStandard = new SysMLHyperlink("IETF RFC-1723 Routing Information Protocol", "https://tools.ietf.org/html/rfc1723");
	}
}
