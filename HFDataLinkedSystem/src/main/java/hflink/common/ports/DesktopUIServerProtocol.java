package hflink.common.ports;

import hflink.common.items.ApplicationUIView;
import hflink.common.items.DesktopUIControl;
import hflink.common.items.DesktopUIView;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.requirements.RequirementSpecificationLink;
import sysmlinjava.ports.SysMLPort;
import sysmlinjava.states.StateBehaviorContext;

/**
 * Port/protocol that simulates the input and output of PC desktop information
 * from and to the PC
 * 
 * @author ModelerOne
 */
public class DesktopUIServerProtocol extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param context state behavior context in which protocol operates
	 * @param id      unique identifier
	 */
	public DesktopUIServerProtocol(StateBehaviorContext context, Long id)
	{
		super(context, id);
	}

	/**
	 * Standard that specifies the protocol
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;

	@Override
	protected void createSupportingInformationLinks()
	{
		protocolStandard = new SysMLHyperlink("IRS for Desktop UI Server", "file://IRS for Protocol");
	}

	@Override
	protected SysMLAnything serverObjectFor(SysMLAnything clientObject)
	{
		SysMLAnything result = null;
		if (clientObject instanceof ApplicationUIView)
			result = new DesktopUIView((ApplicationUIView) clientObject);
		else
			logger.warning("unexpected client object type: " + clientObject.getClass().getSimpleName());
		return result;
	}

	@Override
	protected SysMLAnything clientObjectFor(SysMLAnything serverObject)
	{
		SysMLAnything result = null;
		if (serverObject instanceof DesktopUIControl)
			result = ((DesktopUIControl) serverObject).control;
		else
			logger.warning("unexpected server object type: " + serverObject.getClass().getSimpleName());
		return result;
	}
}
