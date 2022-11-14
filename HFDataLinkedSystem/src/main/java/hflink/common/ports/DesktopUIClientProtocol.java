package hflink.common.ports;

import hflink.common.objects.ApplicationUIControl;
import hflink.common.objects.DesktopUIControl;
import hflink.common.objects.DesktopUIView;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.requirements.RequirementSpecificationLink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port that simulates the protocol for input and output of PC desktop
 * information from and to the operator
 * 
 * @author ModelerOne
 *
 */
public class DesktopUIClientProtocol extends SysMLFullPort
{
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;

	/**
	 * Constructor
	 * 
	 * @param contextBlock block in whose context the port resides
	 * @param id           unique ID
	 */
	public DesktopUIClientProtocol(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Override
	protected void createHyperlinks()
	{
		protocolStandard = new SysMLHyperlink("", "file://IRS for Protocol");
	}

	@Override
	protected SysMLClass serverObjectFor(SysMLClass clientObject)
	{
		SysMLClass result = null;
		if (clientObject instanceof ApplicationUIControl)
			result = new DesktopUIControl((ApplicationUIControl)clientObject);
		else
			logger.warning("unexpected client object type: " + clientObject.getClass().getSimpleName());
		return result;
	}

	@Override
	protected SysMLClass clientObjectFor(SysMLClass serverObject)
	{
		SysMLClass result = null;
		if (serverObject instanceof DesktopUIView)
			result = ((DesktopUIView)serverObject).view;
		else
			logger.warning("unexpected client object type: " + serverObject.getClass().getSimpleName());
		return result;
	}
}
