package hflink.common.ports;

import hflink.common.objects.DataLinkFrame;
import hflink.common.objects.IPPacket;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.ports.SysMLFullPort;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.requirements.RequirementSpecificationLink;

/**
 * Port that simulates the protocol to transmit datalink frames
 * 
 * @author ModelerOne
 *
 */
public class DataLinkTransmitProtocol extends SysMLFullPort
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
	 * @param contextBlock block in whose context the port resides
	 * @param id           unique ID
	 */
	public DataLinkTransmitProtocol(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Override
	protected SysMLClass serverObjectFor(SysMLClass object)
	{
		SysMLClass result = null;
		if (object instanceof IPPacket)
			result = new DataLinkFrame((IPPacket)object);
		else
			logger.warning("unexpected object type: " + object.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createHyperlinks()
	{
		protocolStandard = new SysMLHyperlink("IRS for Data Link Transmit Protocol", "file://IRS for Data Link Transmit Protocol");
	}
}
