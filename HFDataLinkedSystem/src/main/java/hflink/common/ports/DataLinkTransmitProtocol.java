package hflink.common.ports;

import hflink.common.items.DataLinkFrame;
import hflink.common.items.IPPacket;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.requirements.RequirementSpecificationLink;
import sysmlinjava.ports.SysMLPort;
import sysmlinjava.states.StateBehaviorContext;

/**
 * Port that simulates the protocol to transmit datalink frames
 * 
 * @author ModelerOne
 *
 */
public class DataLinkTransmitProtocol extends SysMLPort
{
	/**
	 * Standard that specifies the protocol
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;

	/**
	 * Constructor
	 * 
	 * @param context block in whose context the port resides
	 * @param id           unique ID
	 */
	public DataLinkTransmitProtocol(StateBehaviorContext context, Long id)
	{
		super(context, id);
	}

	@Override
	protected SysMLAnything serverObjectFor(SysMLAnything object)
	{
		SysMLAnything result = null;
		if (object instanceof IPPacket)
			result = new DataLinkFrame((IPPacket)object);
		else
			logger.warning("unexpected object type: " + object.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		protocolStandard = new SysMLHyperlink("IRS for Data Link Transmit Protocol", "file://IRS for Data Link Transmit Protocol");
	}
}
