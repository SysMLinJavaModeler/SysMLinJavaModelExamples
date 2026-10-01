package c4s2.common.ports.information;


import java.util.Optional;
import c4s2.common.signals.RadarSignalTransmissionSignal;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.requirements.RequirementReference;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

/**
 * Port for the reception of the radar signal by the target
 * 
 * @author ModelerOne
 *
 */
public class RadarSignalReceiver extends SysMLPort
{

	public RadarSignalReceiver(SysMLPart contextBlock, Long id)
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
			result = new SysMLSignalEvent(signal, "RadarSignalTransmissionEvent", 0L);
		else
			logger.warning("unexpected signal type: " + signal.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		protocolStandard = new SysMLHyperlink("", "file://IRS for Protocol");
	}
}
