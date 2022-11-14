package motorinwheel.common.ports.energy;

import java.util.Optional;
import motorinwheel.common.events.BrakeWeightEvent;
import motorinwheel.common.signals.MechanicalForceSignal;
import motorinwheel.components.suspensionchassisbody.SuspensionChassisBody;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port to receive the weight force of the brake on the suspension
 * 
 * @author ModelerOne
 *
 */
public class BrakeSuspensionMountForceReceivePort extends SysMLFullPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock      block in whose context the port resides
	 * @param eventContextBlock block to receive the receive events generated by
	 *                          this port
	 * @param id                unique ID
	 */
	public BrakeSuspensionMountForceReceivePort(SysMLBlock contextBlock, SuspensionChassisBody eventContextBlock, Long id)
	{
		super(contextBlock, Optional.of(eventContextBlock), id);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if(signal instanceof MechanicalForceSignal)
			result = new BrakeWeightEvent(((MechanicalForceSignal)signal).force, id);
		return result;
	}
}
