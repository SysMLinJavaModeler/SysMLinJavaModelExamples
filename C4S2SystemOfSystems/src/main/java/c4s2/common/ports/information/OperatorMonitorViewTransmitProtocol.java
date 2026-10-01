package c4s2.common.ports.information;

import java.util.Optional;

import c4s2.common.items.information.OperatorRadarMonitorView;
import c4s2.common.items.information.OperatorStrikeMonitorView;
import c4s2.common.items.information.OperatorSystemMonitorView;
import c4s2.common.items.information.OperatorTargetMonitorView;
import c4s2.common.signals.OperatorRadarMonitorViewSignal;
import c4s2.common.signals.OperatorStrikeMonitorViewSignal;
import c4s2.common.signals.OperatorSystemMonitorViewSignal;
import c4s2.common.signals.OperatorTargetMonitorViewSignal;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

public class OperatorMonitorViewTransmitProtocol extends SysMLPort
{
	public OperatorMonitorViewTransmitProtocol(SysMLPart parent, Long id)
	{
		super(parent, Optional.of(parent), id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof OperatorSystemMonitorView monitorView)
			result = new OperatorSystemMonitorViewSignal(monitorView);
		else if (object instanceof OperatorRadarMonitorView monitorView)
			result = new OperatorRadarMonitorViewSignal(monitorView);
		else if (object instanceof OperatorStrikeMonitorView monitorView)
			result = new OperatorStrikeMonitorViewSignal(monitorView);
		else if (object instanceof OperatorTargetMonitorView monitorView)
			result = new OperatorTargetMonitorViewSignal(monitorView);
		else
			logger.severe("unexpected object type: " + object.getClass().getSimpleName());
		return result;
	}
}
