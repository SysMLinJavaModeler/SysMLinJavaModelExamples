package hflink.components.modemradio;

import hflink.common.items.GPSMessage;
import sysmlinjava.actions.SysMLActionFunction;

/**
 * Interface for the GPS message processor. Specifies single operation to
 * process specified GPS message.
 * 
 * @author ModelerOne
 *
 */
@FunctionalInterface
public interface GPSMessageProcessorInterface extends SysMLActionFunction
{
	/**
	 * Operation to process specified GPS message
	 * 
	 * @param message GPS message to process
	 */
	void processGPSMessage(GPSMessage message);
}
