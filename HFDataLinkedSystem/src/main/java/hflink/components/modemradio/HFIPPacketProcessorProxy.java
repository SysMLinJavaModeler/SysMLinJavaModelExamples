package hflink.components.modemradio;

import java.time.Instant;
import java.util.Optional;

import hflink.common.items.DataLinkFrame;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.ports.SysMLProxyPort;
import sysmlinjava.states.StateBehaviorContext;

/**
 * Proxy port for the HF IP Packet Processor via which the processor's service
 * is invoked
 * 
 * @author ModelerOne
 *
 */
public class HFIPPacketProcessorProxy extends SysMLProxyPort implements HFIPPacketProcessorInterface
{
	/**
	 * Constructor
	 * 
	 * @param context             part or port in whose context the proxy port resides
	 * @param implementingContext part or port that implements the interface of this
	 *                                 proxy port if the port is a conjugate port
	 * @param id                    unique index associated with this port
	 */
	public HFIPPacketProcessorProxy(StateBehaviorContext context, Optional<StateBehaviorContext> implementingContext, String name, Long id)
	{
		super(context, implementingContext, name, id);
	}

	/**
	 * Processes the specified data-link frame. If this is a conjugate port (port on
	 * the processor), it invokes the operation of the implementing part or port.
	 * Otherwise if not a conjugate (port on the client part or port), it simply
	 * uses the connection with the processor's proxy port to invoke the processing
	 * operation.
	 *
	 * @param frame data-link frame to process
	 */
	@Action
	@Override
	public void processDataLinkFrame(DataLinkFrame frame)
	{
		if (implementingContext.isPresent())
		{
			((HFIPPacketProcessor)implementingContext.get()).processDataLinkFrame(frame);
		}
		else if(!connectedPortsPeers.isEmpty())
			for (SysMLProxyPort peer : connectedPortsPeers)
			{
				try
				{
					String message = "processDataLinkFrame(frame)";
					if (messageUtility.isPresent())
						messageUtility.get().perform(Instant.now(), context, message, peer, logger);
					((HFIPPacketProcessorProxy)peer).processDataLinkFrame(frame);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
	}
}
