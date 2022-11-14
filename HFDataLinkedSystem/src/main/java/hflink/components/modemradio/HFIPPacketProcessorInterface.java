package hflink.components.modemradio;

import hflink.common.objects.DataLinkFrame;
import sysmlinjava.blocks.SysMLInterfaceBlock;

/**
 * Interface block for the proxy port of the {@code HFIPPacketProcessor}
 * 
 * @author ModelerOne
 *
 */
public interface HFIPPacketProcessorInterface extends SysMLInterfaceBlock
{
	/**
	 * Processes the specified data-link frame
	 * 
	 * @param frame data-link frame to be processed
	 */
	public void processDataLinkFrame(DataLinkFrame frame);
}
