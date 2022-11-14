package hflink.common.ports;

import hflink.common.objects.PSKSignal;
import hflink.common.objects.TDMASlot;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.requirements.RequirementSpecificationLink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port for the Phase-shift Keyed (PSK) protocol
 * 
 * @author ModelerOne
 *
 */
public class PSKProtocol extends SysMLFullPort
{
	/**
	 * Standard that specifies the protocol
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;

	/**
	 * Constructor
	 * @param contextBlock block in whose context the port resides
	 * @param id unique ID
	 */
	public PSKProtocol(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Override
	protected SysMLClass serverObjectFor(SysMLClass clientObject)
	{
		SysMLClass result = null;
		if(clientObject instanceof TDMASlot)
			result = new PSKSignal((TDMASlot)clientObject);
		else
			logger.warning("unexpected clientObject type: " + clientObject.getClass().getSimpleName());
		return result;
	}

	@Override
	protected SysMLClass clientObjectFor(SysMLClass serverObject)
	{
		SysMLClass result = null;
		if (serverObject instanceof PSKSignal)
			result = ((PSKSignal)serverObject).data;
		else
			logger.warning("unexpected serverObject type: " + serverObject.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createHyperlinks()
	{
		protocolStandard = new SysMLHyperlink("IRS for Phase-Shift-Keyed Receive Protocol", "file://IRS for Phase-Shift-Keyed Receive Protocol");
	}

}
