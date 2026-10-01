package c4s2.common.ports.information;

import java.util.Optional;
import c4s2.common.signals.OperatorRadarMonitorViewSignal;
import c4s2.common.signals.OperatorStrikeMonitorViewSignal;
import c4s2.common.signals.OperatorSystemMonitorViewSignal;
import c4s2.common.signals.OperatorTargetMonitorViewSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

public class OperatorMonitorViewReceiveProtocol extends SysMLPort
{
	public OperatorMonitorViewReceiveProtocol(SysMLPart parent, Long id)
	{
		super(parent, Optional.of(parent), id);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof OperatorRadarMonitorViewSignal)
			result = new SysMLSignalEvent((OperatorRadarMonitorViewSignal)signal, "OperatorRadarMonitorView", 0L);
		else if (signal instanceof OperatorStrikeMonitorViewSignal)
			result = new SysMLSignalEvent((OperatorStrikeMonitorViewSignal)signal, "OperatorStrikeMonitorView", 0L);
		else if (signal instanceof OperatorSystemMonitorViewSignal)
			result = new SysMLSignalEvent((OperatorSystemMonitorViewSignal)signal, "OperatorSystemMonitorView", 0L);
		else if (signal instanceof OperatorTargetMonitorViewSignal)
			result = new SysMLSignalEvent((OperatorTargetMonitorViewSignal)signal, "OperatorTargetMonitorView", 0L);
		else
			logger.severe("unexpected signal type: " + signal.getClass().getSimpleName());
		return result;
	}
}
