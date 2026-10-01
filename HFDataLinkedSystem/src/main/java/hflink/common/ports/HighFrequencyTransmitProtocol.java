package hflink.common.ports;

import hflink.common.items.HFPulse;
import hflink.common.items.PSKSignal;
import hflink.common.signals.HFPulseSignal;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.requirements.RequirementSpecificationLink;
import sysmlinjava.ports.SysMLPort;
import sysmlinjava.states.StateBehaviorContext;

/**
 * Port/protocol to simulate transmitting pulses of HF radio waves carrying data
 * link information
 * 
 * @author ModelerOne
 */
public class HighFrequencyTransmitProtocol extends SysMLPort
{
	/**
	 * Standard that specifies the protocol
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;

	/**
	 * Constructor with context
	 * 
	 * @param context state behavior context (thread) in which this protocol
	 *                operates
	 * @param id      unique identifier
	 */
	public HighFrequencyTransmitProtocol(StateBehaviorContext context, Long id)
	{
		super(context, id);
	}

	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof PSKSignal)
		{
			PSKSignal pskSignal = (PSKSignal) object;
			HFPulse hfPulse = new HFPulse(pskSignal);
			result = new HFPulseSignal(hfPulse);
		}
		else
			logger.info("unexpected object type :" + object.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		protocolStandard = new SysMLHyperlink("IRS for High-Frequency Transmit Protocol", "file://IRS for High-Frequency Transmit Protocol");
	}
}
