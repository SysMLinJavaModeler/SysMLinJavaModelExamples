package c4s2.common.ports.information;

import c4s2.common.events.OperatorRadarControlViewEvent;
import c4s2.common.events.OperatorStrikeControlViewEvent;
import c4s2.common.events.OperatorSystemControlViewEvent;
import c4s2.common.events.OperatorTargetControlViewEvent;
import c4s2.common.signals.OperatorRadarControlViewSignal;
import c4s2.common.signals.OperatorStrikeControlViewSignal;
import c4s2.common.signals.OperatorSystemControlViewSignal;
import c4s2.common.signals.OperatorTargetControlViewSignal;
import c4s2.components.computer.services.C4S2ServicesComputer;
import c4s2.components.services.operator.OperatorServices;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port for the C4S2 protocol that receives operator views of systems control
 * data as HTML. The port uses the HTTP server to receive the HTML control
 * views.
 * 
 * @author ModelerOne
 *
 */
public class C4S2overHTTPServerPort extends SysMLFullPort
{
	public C4S2overHTTPServerPort(C4S2ServicesComputer c4isrServicesComputer, Long id)
	{
		super(c4isrServicesComputer, id);
	}

	@Override
	public void receive(SysMLSignal signal)
	{
		if (signal instanceof OperatorRadarControlViewSignal viewSignal)
		{
			OperatorRadarControlViewEvent event = new OperatorRadarControlViewEvent(viewSignal.controlView);
			((OperatorServices)contextBlock.get()).acceptEvent(event);
		}
		else if (signal instanceof OperatorStrikeControlViewSignal viewSignal)
		{
			OperatorStrikeControlViewEvent event = new OperatorStrikeControlViewEvent(viewSignal.controlView);
			((OperatorServices)contextBlock.get()).acceptEvent(event);
		}
		else if (signal instanceof OperatorSystemControlViewSignal viewSignal)
		{
			OperatorSystemControlViewEvent event = new OperatorSystemControlViewEvent(viewSignal.controlView);
			((OperatorServices)contextBlock.get()).acceptEvent(event);
		}
		else if (signal instanceof OperatorTargetControlViewSignal viewSignal)
		{
			OperatorTargetControlViewEvent event = new OperatorTargetControlViewEvent(viewSignal.controlView);
			((OperatorServices)contextBlock.get()).acceptEvent(event);
		}
	}
}
