package hflink.components.modemradio;

import hflink.common.objects.GPSMessage;
import sysmlinjava.blocks.SysMLInterfaceBlock;

public interface GPSMessageProcessorInterface extends SysMLInterfaceBlock
{
	void processGPSMessage(GPSMessage message);
}
