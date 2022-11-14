package c4s2.common.ports.information;

import java.util.Optional;
import c4s2.common.events.RadarSignalTransmissionEvent;
import c4s2.common.objects.information.RadarSignalTransmission;
import c4s2.common.signals.RadarSignalTransmissionSignal;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.requirements.RequirementReference;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port for the reception of the radar signal by the target
 * 
 * @author ModelerOne
 *
 */
public class RadarSignalReceiver extends SysMLFullPort
{

	public RadarSignalReceiver(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, Optional.of(contextBlock), id);
	}

	@RequirementReference
	@Hyperlink
	public SysMLHyperlink protocolStandard;

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof RadarSignalTransmissionSignal)
		{
			RadarSignalTransmission transmission = ((RadarSignalTransmissionSignal)signal).transmission;
			result = new RadarSignalTransmissionEvent(transmission);
		}
		else
			logger.warning("unexpected signal type: " + signal.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createHyperlinks()
	{
		protocolStandard = new SysMLHyperlink("", "file://IRS for Protocol");
	}
}
