package hflink.components.modemradio;

import java.time.Instant;
import java.util.Optional;

import hflink.common.items.IPPacket;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.requirements.RequirementCapability;
import sysmlinjava.ports.SysMLProxyPort;
import sysmlinjava.states.StateBehaviorContext;

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
	 * @param context             part in whose context the proxy port resides
	 * @param implementingContext part that implements the interface of this proxy
	 *                            port if the port is a conjugate port
	 * @param name                unique name for this proxy port
	 * @param id                  unique index associated with this port
	 */
	public EthernetIPPacketProcessorProxy(StateBehaviorContext context, Optional<StateBehaviorContext> implementingContext, String name, Long id)
	{
		super(context, implementingContext, name, id);
	}

	/**
	 * Processes the specified IP packet. If this is a conjugate port (port on the
	 * processor), it invokes the operation of the implementing context part. Otherwise if
	 * not a conjugate (port on the client part or port), it simply uses the
	 * connection with the processor's proxy port to invoke the processing
	 * operation.
	 *
	 * @param packet IP packet to process
	 */
	@RequirementCapability
	@Action
	@Override
	public void processIPPacket(IPPacket packet)
	{
		if (implementingContext.isPresent())
		{
			String message = "processEthernetIPPacket(packet)";
			if (messageUtility.isPresent())
				messageUtility.get().perform(Instant.now(), this, message, implementingContext.get(), logger);
			((EthernetIPPacketProcessor)implementingContext.get()).processIPPacket(packet);
		}
		else if (!connectedPortsPeers.isEmpty())
			for (SysMLProxyPort peer : connectedPortsPeers)
			{
				try
				{
					String message = "processEthernetIPPacket(packet)";
					if (messageUtility.isPresent())
						messageUtility.get().perform(Instant.now(), context, message, peer, logger);
					((EthernetIPPacketProcessorProxy)peer).processIPPacket(packet);
				} catch (NullPointerException e)
				{
					e.printStackTrace();
				}
			}
	}
}
