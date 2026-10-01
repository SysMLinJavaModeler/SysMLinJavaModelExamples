package hflink.common.ports;

import hflink.common.items.GPSMessage;
import hflink.common.signals.GPSMessageSignal;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.requirements.RequirementSpecificationLink;
import sysmlinjava.ports.SysMLPort;
import sysmlinjava.states.StateBehaviorContext;

/**
 * Port/protocol for transmitting GPS time messages
 * 
 * @author ModelerOne
 *
 */
public class GPSMessagingTransmitProtocol extends SysMLPort
{
	/**
	 * Constructor
	 * @param context state behavior context in which protocol operates
	 * @param id unique identifier
	 */
	public GPSMessagingTransmitProtocol(StateBehaviorContext context, Long id)
	{
		super(context, id);
	}

	/**
	 * Hyperlink to standard document for this protocol
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;

	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof GPSMessage)
			result = new GPSMessageSignal((GPSMessage)object);
		else
			logger.info("unexpected object type :" + object.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		protocolStandard = new SysMLHyperlink("IRS for GPS Message Transmit", "file://IRS for Protocol");
	}
}
