package hflink.common.ports;

import java.util.Optional;
import hflink.common.events.ApplicationUIControlEvent;
import hflink.common.objects.ApplicationUIControl;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.ports.SysMLFullPort;
import hflink.common.objects.ApplicationUIView;
import hflink.common.objects.ApplicationUIViewString;

/**
 * Port that simulates the protocol for input and output by the operator 
 * of information from and to the application
 * 
 * @author ModelerOne
 *
 */
public class ApplicationUIServerProtocol extends SysMLFullPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock      block in whose context the port resides
	 * @param eventContextBlock block to receive signal events generated by the port
	 * @param id                unique ID
	 */
	public ApplicationUIServerProtocol(SysMLBlock contextBlock, SysMLBlock eventContextBlock, Long id)
	{
		super(contextBlock, Optional.of(eventContextBlock), id);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLClass object)
	{
		SysMLSignalEvent result = null;
		if (object instanceof ApplicationUIControl)
			result = new ApplicationUIControlEvent((ApplicationUIControl)object);
		else
			logger.warning("unexpected object type: " + object.getClass().getSimpleName());
		return result;
	}

	@Override
	protected SysMLClass serverObjectFor(SysMLClass clientObject)
	{
		SysMLClass result = null;
		if (clientObject instanceof ApplicationUIViewString)
			result = new ApplicationUIView((ApplicationUIViewString)clientObject);
		else
			logger.warning("unexpected client object type: " + clientObject.getClass().getSimpleName());
		return result;
	}
}