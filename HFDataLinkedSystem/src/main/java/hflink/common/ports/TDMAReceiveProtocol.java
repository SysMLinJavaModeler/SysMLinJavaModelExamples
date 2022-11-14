package hflink.common.ports;

import hflink.common.objects.TDMASlot;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.requirements.RequirementSpecificationLink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Full port for the TDMA receive protocol. The protocol models/simulates common
 * implementations of the TDMA reception, i.e. a TDNA slot is received, data is
 * decapsulated and transfered to an upper level (client) protocol.
 * 
 * @author ModelerOne
 *
 */
public class TDMAReceiveProtocol extends SysMLFullPort
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
	public TDMAReceiveProtocol(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Override
	protected SysMLClass clientObjectFor(SysMLClass serverObject)
	{
		SysMLClass result = null;
		if (serverObject instanceof TDMASlot)
			result = ((TDMASlot)serverObject).data;
		return result;
	}

	@Override
	protected void createHyperlinks()
	{
		protocolStandard = new SysMLHyperlink("IRS for Time-Division-Multiple-Access Receive Protocol", "file://IRS for Time-Division-Multiple-Access Receive Protocol");
	}
}