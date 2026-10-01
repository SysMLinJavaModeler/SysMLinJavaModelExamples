package c4s2.common.ports.information;

import c4s2.common.items.information.RadarSignalTransmission;
import c4s2.common.signals.RadarSignalTransmissionSignal;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.ports.SysMLPort;

/**
 * Port for the transmission of the radar signal to the target
 * 
 * @author ModelerOne
 *
 */
public class RadarSignalTransmitProtocol extends SysMLPort
{

	public RadarSignalTransmitProtocol(SysMLPart contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Hyperlink
	public SysMLHyperlink protocolStandard;

	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		RadarSignalTransmissionSignal result = null;
		if (object instanceof RadarSignalTransmission)
			result = new RadarSignalTransmissionSignal((RadarSignalTransmission)object);
		else
			logger.severe("unrecognized object type: " + object.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		protocolStandard = new SysMLHyperlink("Interface Requirements Specification for the Radar Signal Protocol", "file://IRS for Radar Signal Protocol.pdf");
	}
}
