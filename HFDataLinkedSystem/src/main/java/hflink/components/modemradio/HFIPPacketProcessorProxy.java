package hflink.components.modemradio;

import java.time.Instant;
import java.util.Optional;
import hflink.common.objects.DataLinkFrame;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.ports.SysMLProxyPort;

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
	 * @param contextBlock             block in whose context the proxy port resides
	 * @param implementingContextBlock block that implements the interface of this
	 *                                 proxy port if the port is a conjugate port
	 * @param id                    unique index associated with this port
	 */
	public HFIPPacketProcessorProxy(SysMLBlock contextBlock, Optional<? extends SysMLBlock> implementingContextBlock, Long id)
	{
		super(contextBlock, implementingContextBlock, id);
	}

	/**
	 * Processes the specified data-link frame. If this is a conjugate port (port on
	 * the processor), it invokes the operation of the implementing block. Otherwise
	 * if not a conjugate (port on the client part or port), it simply uses the
	 * connection with the processor's proxy port to invoke the processing
	 * operation.
	 *
	 *@param frame data-link frame to process
	 */
	@Override
	public void processDataLinkFrame(DataLinkFrame frame)
	{
		if (implementingContextBlock.isPresent())
			((HFIPPacketProcessor)implementingContextBlock.get()).processDataLinkFrame(frame);
		else if(!connectedPortsPeers.isEmpty())
			for (SysMLProxyPort peer : connectedPortsPeers)
			{
				try
				{
					String message = "processDataLinkFrame(frame)";
					if (messageUtility.isPresent())
						messageUtility.get().perform(Instant.now(), contextBlock, message, peer, logger);
					((HFIPPacketProcessorProxy)peer).processDataLinkFrame(frame);
				} catch (NullPointerException e)
				{
					e.printStackTrace();
				}
			}
	}
}
