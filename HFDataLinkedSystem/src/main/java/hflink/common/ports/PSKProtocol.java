package hflink.common.ports;

import hflink.common.items.PSKSignal;
import hflink.common.items.TDMASlot;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.requirements.RequirementSpecificationLink;
import sysmlinjava.ports.SysMLPort;
import sysmlinjava.states.StateBehaviorContext;

/**
 * Port for the Phase-shift Keyed (PSK) protocol
 * 
 * @author ModelerOne
 *
 */
public class PSKProtocol extends SysMLPort
{
	/**
	 * Standard that specifies the protocol
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;

	/**
	 * Constructor
	 * @param context block in whose context the port resides
	 * @param id unique ID
	 */
	public PSKProtocol(StateBehaviorContext context, Long id)
	{
		super(context, id);
	}

	@Override
	protected SysMLAnything serverObjectFor(SysMLAnything clientObject)
	{
		SysMLAnything result = null;
		if(clientObject instanceof TDMASlot)
			result = new PSKSignal((TDMASlot)clientObject);
		else
			logger.warning("unexpected clientObject type: " + clientObject.getClass().getSimpleName());
		return result;
	}

	@Override
	protected SysMLAnything clientObjectFor(SysMLAnything serverObject)
	{
		SysMLAnything result = null;
		if (serverObject instanceof PSKSignal)
			result = ((PSKSignal)serverObject).data;
		else
			logger.warning("unexpected serverObject type: " + serverObject.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		protocolStandard = new SysMLHyperlink("IRS for Phase-Shift-Keyed Receive Protocol", "file://IRS for Phase-Shift-Keyed Receive Protocol");
	}

}
