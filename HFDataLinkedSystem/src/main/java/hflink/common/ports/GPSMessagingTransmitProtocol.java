package hflink.common.ports;

import hflink.common.objects.GPSMessage;
import hflink.common.signals.GPSMessageSignal;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.requirements.RequirementSpecificationLink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;

public class GPSMessagingTransmitProtocol extends SysMLFullPort
{
	public GPSMessagingTransmitProtocol(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
	{
		SysMLSignal result = null;
		if (object instanceof GPSMessage)
			result = new GPSMessageSignal((GPSMessage)object);
		else
			logger.info("unexpected object type :" + object.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createHyperlinks()
	{
		protocolStandard = new SysMLHyperlink("", "file://IRS for Protocol");
	}
}
