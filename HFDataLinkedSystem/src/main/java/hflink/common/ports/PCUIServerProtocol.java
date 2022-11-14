package hflink.common.ports;

import hflink.common.signals.PCUIControlSignal;
import hflink.common.signals.PCUIViewSignal;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.requirements.RequirementSpecificationLink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;
import hflink.common.objects.DesktopUIView;
import hflink.common.objects.PCUIControl;
import hflink.common.objects.PCUIView;

/**
 * Port for the physical inputs (Keyboard, mouse, etc) from and outputs (monitor)
 * to the operator
 * 
 * @author ModelerOne
 *
 */
public class PCUIServerProtocol extends SysMLFullPort
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
	 * @param contextBlock block in whose context the port resides
	 * @param id           unique ID
	 */
	public PCUIServerProtocol(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
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
	protected SysMLClass clientObjectFor(SysMLSignal signal)
	{
		SysMLClass result = null;
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
	protected void createHyperlinks()
	{
		protocolStandard = new SysMLHyperlink("", "file://IRS for Protocol");
	}
}
