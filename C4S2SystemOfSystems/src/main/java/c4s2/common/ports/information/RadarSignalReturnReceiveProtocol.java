package c4s2.common.ports.information;

import java.util.Optional;
import c4s2.common.events.RadarSignalReturnEvent;
import c4s2.common.objects.information.RadarSignalReturn;
import c4s2.common.signals.RadarSignalReturnSignal;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port for the reception of radar signal returns, i.e. for receipt of the
 * reflection of radar signals from the target.
 * 
 * @author ModelerOne
 *
 */
public class RadarSignalReturnReceiveProtocol extends SysMLFullPort
{
	public RadarSignalReturnReceiveProtocol(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, Optional.of(contextBlock), id);
	}

	@Hyperlink
	public SysMLHyperlink protocolStandard;

	@Override
	protected void createHyperlinks()
	{
		protocolStandard = new SysMLHyperlink("Interface Requirements Specification for the Radar Signal Protocol", "file://IRS for Radar Signal Protocol.pdf");
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof RadarSignalReturnSignal)
		{
			RadarSignalReturn radarReturn = ((RadarSignalReturnSignal)signal).radarReturn;
			result = new RadarSignalReturnEvent(radarReturn);
		}
		else
			logger.warning("unexpected signal type: " + signal.getClass().getSimpleName());
		return result;
	}
}
