package c4s2.common.ports.information;

import java.util.Optional;

import c4s2.common.signals.OperatorRadarControlViewSignal;
import c4s2.common.signals.OperatorStrikeControlViewSignal;
import c4s2.common.signals.OperatorSystemControlViewSignal;
import c4s2.common.signals.OperatorTargetControlViewSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

/**
 * Port for the reception of the operator's views of control information.
 * Control views include radar, strike, and target system views as well as a
 * view of the C4S2 system itself.
 * 
 * @author ModelerOne
 *
 */
public class OperatorControlViewReceiveProtocol extends SysMLPort
{
	public OperatorControlViewReceiveProtocol(SysMLPart parent, Long id)
	{
		super(parent, Optional.of(parent), id);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		logger.info(signal.getClass().getSimpleName());
		SysMLSignalEvent result = null;
		if (signal instanceof OperatorRadarControlViewSignal)
			result = new SysMLSignalEvent(signal, "OperatorRadarControlView", 0L);
		else if (signal instanceof OperatorStrikeControlViewSignal)
			result = new SysMLSignalEvent(signal, "OperatorStrikeControlView", 0L);
		else if (signal instanceof OperatorSystemControlViewSignal)
			result = new SysMLSignalEvent(signal, "OperatorSystemControlView", 0L);
		else if (signal instanceof OperatorTargetControlViewSignal)
			result = new SysMLSignalEvent(signal, "OperatorTargetControlView", 0L);
		else
			logger.severe("unexpected signal type: " + signal.getClass().getSimpleName());
		return result;
	}
}
