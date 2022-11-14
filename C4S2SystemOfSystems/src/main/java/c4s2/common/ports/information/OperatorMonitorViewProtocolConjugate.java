package c4s2.common.ports.information;

import java.util.Optional;
import c4s2.common.objects.information.OperatorRadarMonitorView;
import c4s2.common.objects.information.OperatorStrikeMonitorView;
import c4s2.common.objects.information.OperatorSystemMonitorView;
import c4s2.common.objects.information.OperatorTargetMonitorView;
import c4s2.common.signals.OperatorRadarMonitorViewSignal;
import c4s2.common.signals.OperatorStrikeMonitorViewSignal;
import c4s2.common.signals.OperatorSystemMonitorViewSignal;
import c4s2.common.signals.OperatorTargetMonitorViewSignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;

public class OperatorMonitorViewProtocolConjugate extends SysMLFullPort
{
	public OperatorMonitorViewProtocolConjugate(SysMLBlock parent, Long id)
	{
		super(parent, Optional.of(parent), id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
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
