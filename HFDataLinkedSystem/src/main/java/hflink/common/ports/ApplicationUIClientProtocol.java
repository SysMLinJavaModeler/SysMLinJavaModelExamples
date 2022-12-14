package hflink.common.ports;

import java.util.Optional;
import hflink.common.events.ApplicationUIViewEvent;
import hflink.common.objects.ApplicationUIControl;
import hflink.common.objects.ApplicationUIControlString;
import hflink.common.objects.ApplicationUIView;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.requirements.RequirementSpecificationLink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port that simulates the protocol for input and output of application
 * information from and to the operator
 * 
 * @author ModelerOne
 *
 */
public class ApplicationUIClientProtocol extends SysMLFullPort
{
	/**
	 * Standard spec for the protocol
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;

	/**
	 * Constructor
	 * 
	 * @param contextBlock      block in whose context the port resides
	 * @param eventContextBlock block to receive signal events generated by the port
	 * @param id                unique ID
	 */
	public ApplicationUIClientProtocol(SysMLBlock contextBlock, SysMLBlock eventContextBlock, Long id)
	{
		super(contextBlock, Optional.of(eventContextBlock), id);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLClass object)
	{
		SysMLSignalEvent result = null;
		if (object instanceof ApplicationUIView)
			result = new ApplicationUIViewEvent((ApplicationUIView)object);
		else
			logger.warning("unexpected object type: " + object.getClass().getSimpleName());
		return result;
	}

	@Override
	protected SysMLClass serverObjectFor(SysMLClass clientObject)
	{
		SysMLClass result = null;
		if (clientObject instanceof ApplicationUIControlString)
			result = new ApplicationUIControl((ApplicationUIControlString)clientObject);
		else
			logger.warning("unexpected client object type: " + clientObject.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createHyperlinks()
	{
		protocolStandard = new SysMLHyperlink("AppUI Client Protocol", "file://IRS for Application UI Client Protocol");
	}
}