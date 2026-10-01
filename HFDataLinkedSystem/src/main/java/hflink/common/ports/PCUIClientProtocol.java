package hflink.common.ports;

import hflink.common.items.DesktopUIControl;
import hflink.common.items.PCUIControl;
import hflink.common.items.PCUIView;
import hflink.common.signals.PCUIControlSignal;
import hflink.common.signals.PCUIViewSignal;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.requirements.RequirementSpecificationLink;
import sysmlinjava.ports.SysMLPort;
import sysmlinjava.states.StateBehaviorContext;

/**
 * Port for the physical inputs (Keyboard, mouse, etc) to and outputs (monitor)
 * from the PC
 * 
 * @author ModelerOne
 *
 */
public class PCUIClientProtocol extends SysMLPort
{
	/**
	 * Standard that specifies the protocol
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;

	/**
	 * Conxtructor
	 * 
	 * @param context block in whose context the port resides
	 * @param id           unique ID
	 */
	public PCUIClientProtocol(StateBehaviorContext context, Long id)
	{
		super(context, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof DesktopUIControl)
		{
			PCUIControl control = new PCUIControl((DesktopUIControl)object);
			result = new PCUIControlSignal(control);
		}
		else
			logger.warning("unexpected object type: " + object.getClass().getSimpleName());
		return result;
	}

	@Override
	protected SysMLAnything clientObjectFor(SysMLSignal signal)
	{
		SysMLAnything result = null;
		if (signal instanceof PCUIViewSignal)
		{
			PCUIView pcView = ((PCUIViewSignal)signal).view;
			result = pcView.view;
		}
		else
			logger.warning("unexpected signal type: " + signal.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		protocolStandard = new SysMLHyperlink("IRS for PC UI Client", "file://IRS for Protocol");
	}
}
