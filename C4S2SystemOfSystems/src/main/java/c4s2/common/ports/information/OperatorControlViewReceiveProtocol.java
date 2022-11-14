package c4s2.common.ports.information;

import java.util.Optional;
import c4s2.common.events.OperatorRadarControlViewEvent;
import c4s2.common.events.OperatorStrikeControlViewEvent;
import c4s2.common.events.OperatorSystemControlViewEvent;
import c4s2.common.events.OperatorTargetControlViewEvent;
import c4s2.common.signals.OperatorRadarControlViewSignal;
import c4s2.common.signals.OperatorStrikeControlViewSignal;
import c4s2.common.signals.OperatorSystemControlViewSignal;
import c4s2.common.signals.OperatorTargetControlViewSignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port for the reception of the operator's views of control information.
 * Control views include radar, strike, and target system views as well as a
 * view of the C4S2 system itself.
 * 
 * @author ModelerOne
 *
 */
public class OperatorControlViewReceiveProtocol extends SysMLFullPort
{
	public OperatorControlViewReceiveProtocol(SysMLBlock parent, Long id)
	{
		super(parent, Optional.of(parent), id);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof OperatorSystemControlViewSignal viewSignal)
			result = new OperatorSystemControlViewEvent(viewSignal.controlView);
		else if (signal instanceof OperatorRadarControlViewSignal viewSignal)
			result = new OperatorRadarControlViewEvent(viewSignal.controlView);
		else if (signal instanceof OperatorStrikeControlViewSignal viewSignal)
			result = new OperatorStrikeControlViewEvent(viewSignal.controlView);
		else if (signal instanceof OperatorTargetControlViewSignal viewSignal)
			result = new OperatorTargetControlViewEvent(viewSignal.controlView);
		else
			logger.severe("unexpected signal type: " + signal.getClass().getSimpleName());
		return result;
	}
}
