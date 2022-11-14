package c4s2.common.ports.information;

import c4s2.common.objects.information.OperatorRadarControlView;
import c4s2.common.objects.information.OperatorStrikeControlView;
import c4s2.common.objects.information.OperatorSystemControlView;
import c4s2.common.objects.information.OperatorTargetControlView;
import c4s2.common.signals.OperatorRadarControlViewSignal;
import c4s2.common.signals.OperatorStrikeControlViewSignal;
import c4s2.common.signals.OperatorSystemControlViewSignal;
import c4s2.common.signals.OperatorTargetControlViewSignal;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port for the transmission of the operator's views of control information.
 * Control views include radar, strike, and target system views as well as a
 * view of the C4S2 system itself.
 * 
 * @author ModelerOne
 *
 */
public class OperatorControlTransmitProtocol extends SysMLFullPort
{
	public OperatorControlTransmitProtocol(SysMLBlock parent, Long id)
	{
		super(parent, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
	{
		SysMLSignal result = null;
		if (object instanceof OperatorSystemControlView controlView)
			result = new OperatorSystemControlViewSignal(controlView);
		else if (object instanceof OperatorRadarControlView controlView)
			result = new OperatorRadarControlViewSignal(controlView);
		else if (object instanceof OperatorStrikeControlView controlView)
			result = new OperatorStrikeControlViewSignal(controlView);
		else if (object instanceof OperatorTargetControlView controlView)
			result = new OperatorTargetControlViewSignal(controlView);
		else
			logger.severe("unexpected object type: " + object.getClass().getSimpleName());
		return result;
	}
}
