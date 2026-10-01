package hflink.components.modemradio;

import hflink.common.items.DataLinkFrame;
import sysmlinjava.javaannotations.actions.Action;

/**
 * Interface block for the proxy port of the {@code HFIPPacketProcessor}
 * 
 * @author ModelerOne
 *
 */
public interface HFIPPacketProcessorInterface
{
	/**
	 * Operation to process the specified data-link frame
	 * 
	 * @param frame data-link frame to be processed
	 */
	@Action
	public void processDataLinkFrame(DataLinkFrame frame);
}
