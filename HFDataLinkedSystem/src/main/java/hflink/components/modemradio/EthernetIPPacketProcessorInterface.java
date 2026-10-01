package hflink.components.modemradio;

import hflink.common.items.IPPacket;

/**
 * Interface for the proxy port of the {@code EthernetIPPacketProcessor}
 * 
 * @author ModelerOne
 *
 */
@FunctionalInterface
public interface EthernetIPPacketProcessorInterface
{
	/**
	 * Processes the specified IP packet
	 * 
	 * @param packet IP packet to process
	 */
	void processIPPacket(IPPacket packet);
}
