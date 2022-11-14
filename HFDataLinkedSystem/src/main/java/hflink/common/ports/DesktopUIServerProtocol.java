package hflink.common.ports;

import hflink.common.objects.ApplicationUIView;
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
 * information from and to the PC
 * 
 * @author ModelerOne
 *
 */
public class DesktopUIServerProtocol extends SysMLFullPort
{
	public DesktopUIServerProtocol(SysMLBlock parent, Long id)
	{
		super(parent, id);
	}

	/**
	 * Standard that specifies the protocol
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;

	@Override
	protected void createHyperlinks()
	{
		protocolStandard = new SysMLHyperlink("", "file://IRS for Protocol");
	}

	@Override
	protected SysMLClass serverObjectFor(SysMLClass clientObject)
	{
		SysMLClass result = null;
		if (clientObject instanceof ApplicationUIView)
			result = new DesktopUIView((ApplicationUIView)clientObject);
		else
			logger.warning("unexpected client object type: " + clientObject.getClass().getSimpleName());
		return result;
	}

	@Override
	protected SysMLClass clientObjectFor(SysMLClass serverObject)
	{
		SysMLClass result = null;
		if (serverObject instanceof DesktopUIControl)
			result = ((DesktopUIControl)serverObject).control;
		else
			logger.warning("unexpected server object type: " + serverObject.getClass().getSimpleName());
		return result;
	}
}
