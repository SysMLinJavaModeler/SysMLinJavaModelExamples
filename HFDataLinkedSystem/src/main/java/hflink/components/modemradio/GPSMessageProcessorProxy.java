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

import hflink.common.items.GPSMessage;
import sysmlinjava.ports.SysMLProxyPort;
import sysmlinjava.states.StateBehaviorContext;

/**
 * Proxy port for GPS message processing
 */
public class GPSMessageProcessorProxy extends SysMLProxyPort implements GPSMessageProcessorInterface
{
	/**
	 * Constructor
	 * 
	 * @param context             part or port in whose context the proxy port
	 *                            resides
	 * @param implementingContext part or port that implements the interface of this
	 *                            proxy port if the port is a conjugate port
	 * @param name                unique name for this proxy port
	 * @param id                  unique index associated with this port
	 */
	public GPSMessageProcessorProxy(StateBehaviorContext context, Optional<StateBehaviorContext> implementingContext, String name, Long id)
	{
		super(context, implementingContext, name, id);
	}

	/**
	 * Processes the specified GPS time message. If this is a conjugate port (port
	 * on the processor), it invokes the operation of the implementing part or port.
	 * Otherwise if not a conjugate (port on the client part or port), it simply
	 * uses the connection with the processor's proxy port to invoke the processing
	 * operation.
	 *
	 * @param gpsMessage message to be processed
	 */
	@Override
	public void processGPSMessage(GPSMessage gpsMessage)
	{
		if (implementingContext.isPresent())
		{
			String message = "processGPSMessage(gpsMessage)";
			if (messageUtility.isPresent())
				messageUtility.get().perform(Instant.now(), this, message, implementingContext.get(), logger);
			((GPSMessageProcessor)implementingContext.get()).processGPSMessage(gpsMessage);
		}
		else if (!connectedPortsPeers.isEmpty())
			for (SysMLProxyPort peer : connectedPortsPeers)
			{
				try
				{
					String message = "processGPSMessage(gpsMessage)";
					if (messageUtility.isPresent())
						messageUtility.get().perform(Instant.now(), context, message, peer, logger);
					((GPSMessageProcessorProxy)peer).processGPSMessage(gpsMessage);
				} catch (NullPointerException e)
				{
					e.printStackTrace();
				}
			}
	}
}
