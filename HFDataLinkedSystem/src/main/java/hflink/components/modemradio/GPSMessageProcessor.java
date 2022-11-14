package hflink.components.modemradio;

import java.util.Optional;
import hflink.common.objects.GPSMessage;
import sysmlinjava.annotations.ProxyPort;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.requirements.RequirementCapability;
import sysmlinjava.annotations.requirements.RequirementInterfaceInterface;
import sysmlinjava.annotations.requirements.RequirementSpecificationLink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;

/**
 * Processor of GPS time messages received via the GPS messaging protocol. It
 * demonstrates the use of the proxy port for an implementation-independent
 * model of this particular component
 * 
 * @author ModelerOne
 *
 */
public class GPSMessageProcessor extends SysMLBlock implements GPSMessageProcessorInterface
{
	/**
	 * Port for the proxy of the GPS message processor
	 */
	@RequirementInterfaceInterface
	@ProxyPort
	public GPSMessageProcessorProxy proxy;

	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink componentSpecification;

	/**
	 * Constructor
	 * 
	 * @param contextBlock block in whose context the processor resides
	 * @param id           unique ID
	 */
	public GPSMessageProcessor(SysMLBlock contextBlock, long id)
	{
		super(contextBlock, "GPSMessageProcessor", (long)id);
	}

	/**
	 * Reception to process the specified GPS time message. Processing is to provide
	 * the time message to the TDMA transmit protocol which will proceed with the
	 * transmission of a data-link frame if the current time is for the TDMA time
	 * slot assigned to this modem-radio.
	 * 
	 * @param gpsMessage message to be processes
	 */
	@RequirementCapability
	@Override
	public void processGPSMessage(GPSMessage gpsMessage)
	{
		((ModemRadio)contextBlock.get()).tdmaTransmit.onGPSMessage(gpsMessage);
	}

	@Override
	protected void createHyperlinks()
	{
		componentSpecification = new SysMLHyperlink("Component Specification", "file://Software Requirements Specification For The GPS Message Processor.html");
	}

	@Override
	protected void createProxyPorts()
	{
		proxy = new GPSMessageProcessorProxy(this, Optional.of(this), 0L);
	}
}
