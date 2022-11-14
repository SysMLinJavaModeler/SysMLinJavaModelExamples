package hflink.components.modemradio;

import java.time.Instant;
import java.util.Optional;
import hflink.common.objects.IPPacket;
import sysmlinjava.annotations.requirements.RequirementCapability;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.ports.SysMLProxyPort;

/**
 * Proxy port for the Ethernet IP Packet Processor via which the processor's
 * service is invoked
 * 
 * @author ModelerOne
 *
 */
public class EthernetIPPacketProcessorProxy extends SysMLProxyPort implements EthernetIPPacketProcessorInterface
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock             block in whose context the proxy port resides
	 * @param implementingContextBlock block that implements the interface of this
	 *                                 proxy port if the port is a conjugate port
	 * @param id                       unique index associated with this port
	 */
	public EthernetIPPacketProcessorProxy(SysMLBlock contextBlock, Optional<? extends SysMLBlock> implementingContextBlock, Long id)
	{
		super(contextBlock, implementingContextBlock, id);
	}

	/**
	 * Processes the specified IP packet. If this is a conjugate port (port on the
	 * processor), it invokes the operation of the implementing block. Otherwise if
	 * not a conjugate (port on the client part or port), it simply uses the
	 * connection with the processor's proxy port to invoke the processing
	 * operation.
	 *
	 * @param packet IP packet to process
	 */
	@RequirementCapability
	@Override
	public void processIPPacket(IPPacket packet)
	{
		if (implementingContextBlock.isPresent())
			((EthernetIPPacketProcessor)implementingContextBlock.get()).processIPPacket(packet);
		else if (!connectedPortsPeers.isEmpty())
			for (SysMLProxyPort peer : connectedPortsPeers)
			{
				try
				{
					String message = "processEthernetIPPacket(packet)";
					if (messageUtility.isPresent())
						messageUtility.get().perform(Instant.now(), contextBlock, message, peer, logger);
					((EthernetIPPacketProcessorProxy)peer).processIPPacket(packet);
				} catch (NullPointerException e)
				{
					e.printStackTrace();
				}
			}
	}
}
