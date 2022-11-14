package hflink.common.ports;

import hflink.common.objects.HFPulse;
import hflink.common.objects.PSKSignal;
import hflink.common.signals.HFPulseSignal;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.requirements.RequirementSpecificationLink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;

/**
 * Port to simulate the protocol to transmit pulses of HF radio waves carrying
 * data link information
 * 
 * @author ModelerOne
 *
 */
public class HighFrequencyTransmitProtocol extends SysMLFullPort
{
	/**
	 * Standard that specifies the protocol
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;

	public HighFrequencyTransmitProtocol(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLClass object)
	{
		SysMLSignal result = null;
		if (object instanceof PSKSignal)
		{
			PSKSignal pskSignal = (PSKSignal)object;
			HFPulse hfPulse = new HFPulse(pskSignal);
			result = new HFPulseSignal(hfPulse);
		}
		else
			logger.info("unexpected object type :" + object.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createHyperlinks()
	{
		protocolStandard = new SysMLHyperlink("IRS for High-Frequency Transmit Protocol", "file://IRS for High-Frequency Transmit Protocol");
	}
}
