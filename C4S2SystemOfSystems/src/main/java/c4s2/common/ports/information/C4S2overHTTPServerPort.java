package c4s2.common.ports.information;

import c4s2.common.signals.OperatorRadarControlViewSignal;
import c4s2.common.signals.OperatorStrikeControlViewSignal;
import c4s2.common.signals.OperatorSystemControlViewSignal;
import c4s2.common.signals.OperatorTargetControlViewSignal;
import c4s2.components.computer.services.C4S2ServicesComputer;
import c4s2.components.services.operator.OperatorServices;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.ports.SysMLPort;

/**
 * Port for the C4S2 protocol that receives operator views of systems control
 * data as HTML. The port uses the HTTP server to receive the HTML control
 * views.
 * 
 * @author ModelerOne
 *
 */
public class C4S2overHTTPServerPort extends SysMLPort
{
	public C4S2overHTTPServerPort(C4S2ServicesComputer c4isrServicesComputer, Long id)
	{
		super(c4isrServicesComputer, id);
	}

	@Override
	public void receive(SysMLSignal signal)
	{
		if (signal instanceof OperatorRadarControlViewSignal)
			((OperatorServices)context.get()).acceptEvent(new SysMLSignalEvent((OperatorRadarControlViewSignal)signal, "OperatorRadarControlViewEvent", 0L));
		else if (signal instanceof OperatorStrikeControlViewSignal)
			((OperatorServices)context.get()).acceptEvent(new SysMLSignalEvent((OperatorStrikeControlViewSignal)signal, "OperatorStrikeControlViewEvent", 0L));
		else if (signal instanceof OperatorSystemControlViewSignal)
			((OperatorServices)context.get()).acceptEvent(new SysMLSignalEvent((OperatorSystemControlViewSignal)signal, "OperatorSystemControlViewEvent", 0L));
		else if (signal instanceof OperatorTargetControlViewSignal)
			((OperatorServices)context.get()).acceptEvent(new SysMLSignalEvent((OperatorTargetControlViewSignal)signal, "OperatorTargetControlViewEvent", 0L));
	}
}
