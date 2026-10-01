package hflink.common.ports;

import hflink.common.items.TDMASlot;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.requirements.RequirementSpecificationLink;
import sysmlinjava.ports.SysMLPort;
import sysmlinjava.states.StateBehaviorContext;

/**
 * Full port for the TDMA receive protocol. The protocol models/simulates common
 * implementations of the TDMA reception, i.e. a TDMA slot is received, data is
 * decapsulated and transfered to an upper level (client) protocol.
 * 
 * @author ModelerOne
 *
 */
public class TDMAReceiveProtocol extends SysMLPort
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
	public TDMAReceiveProtocol(StateBehaviorContext context, Long id)
	{
		super(context, id);
	}

	@Override
	protected SysMLAnything clientObjectFor(SysMLAnything serverObject)
	{
		SysMLAnything result = null;
		if (serverObject instanceof TDMASlot)
			result = ((TDMASlot)serverObject).data;
		return result;
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		protocolStandard = new SysMLHyperlink("IRS for Time-Division-Multiple-Access Receive Protocol", "file://IRS for Time-Division-Multiple-Access Receive Protocol");
	}
}