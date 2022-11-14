/**
 * Proxy port for the GPS message processor via which the processor's service is
 * invoked
 * 
 * @author ModelerOne
 *
 */
package hflink.components.modemradio;

import java.time.Instant;
import java.util.Optional;
import hflink.common.objects.GPSMessage;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.ports.SysMLProxyPort;

public class GPSMessageProcessorProxy extends SysMLProxyPort implements GPSMessageProcessorInterface
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock             block in whose context the proxy port resides
	 * @param implementingContextBlock block that implements the interface of this
	 *                                 proxy port if the port is a conjugate port
	 * @param id                       unique index associated with this port
	 */
	public GPSMessageProcessorProxy(SysMLBlock contextBlock, Optional<? extends SysMLBlock> implementingContextBlock, Long id)
	{
		super(contextBlock, implementingContextBlock, id);
	}

	/**
	 * Processes the specified GPS time message. If this is a conjugate port (port
	 * on the processor), it invokes the operation of the implementing block.
	 * Otherwise if not a conjugate (port on the client part or port), it simply
	 * uses the connection with the processor's proxy port to invoke the processing
	 * operation.
	 *
	 * @param gpsMessage message to be processed
	 */
	@Override
	public void processGPSMessage(GPSMessage gpsMessage)
	{
		if (implementingContextBlock.isPresent())
			((GPSMessageProcessor)implementingContextBlock.get()).processGPSMessage(gpsMessage);
		else if (!connectedPortsPeers.isEmpty())
			for (SysMLProxyPort peer : connectedPortsPeers)
			{
				try
				{
					String message = "processGPSMessage(gpsMessage)";
					if (messageUtility.isPresent())
						messageUtility.get().perform(Instant.now(), contextBlock, message, peer, logger);
					((GPSMessageProcessorProxy)peer).processGPSMessage(gpsMessage);
				} catch (NullPointerException e)
				{
					e.printStackTrace();
				}
			}
	}
}
