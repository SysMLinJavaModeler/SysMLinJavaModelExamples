package c4s2.common.ports.information;

import java.util.Optional;
import c4s2.common.events.OperatorRadarMonitorViewEvent;
import c4s2.common.events.OperatorStrikeMonitorViewEvent;
import c4s2.common.events.OperatorSystemMonitorViewEvent;
import c4s2.common.events.OperatorTargetMonitorViewEvent;
import c4s2.common.signals.OperatorRadarMonitorViewSignal;
import c4s2.common.signals.OperatorStrikeMonitorViewSignal;
import c4s2.common.signals.OperatorSystemMonitorViewSignal;
import c4s2.common.signals.OperatorTargetMonitorViewSignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.ports.SysMLFullPort;

public class OperatorMonitorViewProtocol extends SysMLFullPort
{
	public OperatorMonitorViewProtocol(SysMLBlock parent, Long id)
	{
		super(parent, Optional.of(parent), id);
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof OperatorSystemMonitorViewSignal viewSignal)
			result = new OperatorSystemMonitorViewEvent(viewSignal.monitorView);
		else if (signal instanceof OperatorRadarMonitorViewSignal viewSignal)
			result = new OperatorRadarMonitorViewEvent(viewSignal.monitorView);
		else if (signal instanceof OperatorStrikeMonitorViewSignal viewSignal)
			result = new OperatorStrikeMonitorViewEvent(viewSignal.monitorView);
		else if (signal instanceof OperatorTargetMonitorViewSignal viewSignal)
			result = new OperatorTargetMonitorViewEvent(viewSignal.monitorView);
		else
			logger.severe("unexpected signal type: " + signal.getClass().getSimpleName());
		return result;
	}
}
