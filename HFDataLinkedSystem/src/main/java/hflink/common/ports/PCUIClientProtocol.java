package hflink.common.ports;

import hflink.common.objects.DesktopUIControl;
import hflink.common.objects.PCUIControl;
import hflink.common.objects.PCUIView;
import hflink.common.signals.PCUIControlSignal;
import hflink.common.signals.PCUIViewSignal;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.requirements.RequirementSpecificationLink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port for the physical inputs (Keyboard, mouse, etc) to and outputs (monitor)
 * from the PC
 * 
 * @author ModelerOne
 *
 */
public class PCUIClientProtocol extends SysMLFullPort
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
	public PCUIClientProtocol(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
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
	protected SysMLClass clientObjectFor(SysMLSignal signal)
	{
		SysMLClass result = null;
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
	protected void createHyperlinks()
	{
		protocolStandard = new SysMLHyperlink("", "file://IRS for Protocol");
	}
}
