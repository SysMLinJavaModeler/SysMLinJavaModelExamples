package c4s2.common.ports.information;

import c4s2.common.objects.information.RadarSignalTransmission;
import c4s2.common.signals.RadarSignalTransmissionSignal;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port for the transmission of the radar signal to the target
 * 
 * @author ModelerOne
 *
 */
public class RadarSignalTransmitProtocol extends SysMLFullPort
{

	public RadarSignalTransmitProtocol(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Hyperlink
	public SysMLHyperlink protocolStandard;

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
	{
		RadarSignalTransmissionSignal result = null;
		if (object instanceof RadarSignalTransmission)
			result = new RadarSignalTransmissionSignal((RadarSignalTransmission)object);
		else
			logger.severe("unrecognized object type: " + object.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createHyperlinks()
	{
		protocolStandard = new SysMLHyperlink("Interface Requirements Specification for the Radar Signal Protocol", "file://IRS for Radar Signal Protocol.pdf");
	}
}
