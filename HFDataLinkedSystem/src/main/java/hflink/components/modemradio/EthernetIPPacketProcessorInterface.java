package hflink.components.modemradio;

import hflink.common.objects.IPPacket;
import sysmlinjava.blocks.SysMLInterfaceBlock;

/**
 * Interface block for the proxy port of the {@code EthernetIPPacketProcessor}
 * 
 * @author ModelerOne
 *
 */
@FunctionalInterface
public interface EthernetIPPacketProcessorInterface extends SysMLInterfaceBlock
{
	/**
	 * Processes the specified IP packet
	 * 
	 * @param packet IP packet to process
	 */
	void processIPPacket(IPPacket packet);
}
