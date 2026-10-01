package hflink.common.ports;

import hflink.common.items.ApplicationUIControl;
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
 * from and to the operator
 * 
 * @author ModelerOne
 */
public class DesktopUIClientProtocol extends SysMLPort
{
	/**
	 * Hyperlink to standard document for this protocol
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;

	/**
	 * Constructor
	 * 
	 * @param context block in whose context the port resides
	 * @param id      unique ID
	 */
	public DesktopUIClientProtocol(StateBehaviorContext context, Long id)
	{
		super(context, id);
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		protocolStandard = new SysMLHyperlink("IRS for Desktop UI Client", "file://IRS for Protocol");
	}

	@Override
	protected SysMLAnything serverObjectFor(SysMLAnything clientObject)
	{
		SysMLAnything result = null;
		if (clientObject instanceof ApplicationUIControl)
			result = new DesktopUIControl((ApplicationUIControl) clientObject);
		else
			logger.warning("unexpected client object type: " + clientObject.getClass().getSimpleName());
		return result;
	}

	@Override
	protected SysMLAnything clientObjectFor(SysMLAnything serverObject)
	{
		SysMLAnything result = null;
		if (serverObject instanceof DesktopUIView)
			result = ((DesktopUIView) serverObject).view;
		else
			logger.warning("unexpected client object type: " + serverObject.getClass().getSimpleName());
		return result;
	}
}
