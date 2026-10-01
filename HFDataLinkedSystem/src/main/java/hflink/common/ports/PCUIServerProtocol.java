package hflink.common.ports;

import hflink.common.items.DesktopUIView;
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
 * Port for the physical inputs (Keyboard, mouse, etc) from and outputs (monitor)
 * to the operator
 * 
 * @author ModelerOne
 *
 */
public class PCUIServerProtocol extends SysMLPort
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
	public PCUIServerProtocol(StateBehaviorContext context, Long id)
	{
		super(context, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof DesktopUIView)
		{
			PCUIView control = new PCUIView((DesktopUIView)object);
			result = new PCUIViewSignal(control);
		}
		else
			logger.warning("unexpected object type: " + object.getClass().getSimpleName());
		return result;
	}

	@Override
	protected SysMLAnything clientObjectFor(SysMLSignal signal)
	{
		SysMLAnything result = null;
		if (signal instanceof PCUIControlSignal)
		{
			PCUIControl pcControl = ((PCUIControlSignal)signal).control;
			result = pcControl.control;
		}
		else
			logger.warning("unexpected signal type: " + signal.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		protocolStandard = new SysMLHyperlink("IRS for PC UI Server", "file://IRS for Protocol");
	}
}
