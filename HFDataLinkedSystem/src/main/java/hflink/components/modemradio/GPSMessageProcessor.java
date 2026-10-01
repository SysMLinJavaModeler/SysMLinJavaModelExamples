package hflink.components.modemradio;

import java.util.Optional;

import hflink.common.items.GPSMessage;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.ports.ProxyPort;
import sysmlinjava.javaannotations.requirements.RequirementCapability;
import sysmlinjava.javaannotations.requirements.RequirementInterfaceInterface;
import sysmlinjava.javaannotations.requirements.RequirementSpecificationLink;
import sysmlinjava.parts.SysMLPart;

/**
 * Processor of GPS time messages received via the GPS messaging protocol. It
 * demonstrates the use of the proxy port for an implementation-independent
 * model of this particular component
 * 
 * @author ModelerOne
 */
public class GPSMessageProcessor extends SysMLPart implements GPSMessageProcessorInterface
{
	/**
	 * Port for the proxy of the GPS message processor
	 */
	@RequirementInterfaceInterface
	@ProxyPort
	public GPSMessageProcessorProxy proxy;

	/**
	 * Requirements spec for GPS message processor component part
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink componentSpecification;

	/**
	 * Constructor
	 * 
	 * @param contextPart part (ModemRadio) in whose context the processor resides
	 * @param id          unique ID
	 */
	public GPSMessageProcessor(ModemRadio contextPart, long id)
	{
		super(Optional.of(contextPart), "GPSMessageProcessor", id);
	}

	/**
	 * Action to process the specified GPS time message. Processing is to provide
	 * the time message to the TDMA transmit protocol which will proceed with the
	 * transmission of a data-link frame if the current time is for the TDMA time
	 * slot assigned to this modem-radio.
	 * 
	 * @param gpsMessage message to be processes
	 */
	@RequirementCapability
	@Action
	@Override
	public void processGPSMessage(GPSMessage gpsMessage)
	{
		((ModemRadio) context.get()).tdmaTransmit.onGPSMessage(gpsMessage);
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		componentSpecification = new SysMLHyperlink("Component Specification", "file://Software Requirements Specification For The GPS Message Processor.html");
	}

	@Override
	protected void createPorts()
	{
		proxy = new GPSMessageProcessorProxy(this, Optional.of(this), "GPSMessageProcessorProxy", 0L);
	}
}
