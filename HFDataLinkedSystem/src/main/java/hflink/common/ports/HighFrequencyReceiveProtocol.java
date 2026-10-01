package hflink.common.ports;

import hflink.common.items.DataLinkFrame;
import hflink.common.items.HFPulse;
import hflink.common.items.PSKSignal;
import hflink.common.items.TDMASlot;
import hflink.common.signals.HFPulseSignal;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.attributetypes.RReal;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.requirements.RequirementSpecificationLink;
import sysmlinjava.ports.SysMLPort;
import sysmlinjava.probability.SysMLUniformProbabilityDistribution;
import sysmlinjava.states.StateBehaviorContext;

/**
 * Port/protocol to simulate receiving pulses of HF radio waves carrying data
 * link information
 * 
 * @author ModelerOne
 */
public class HighFrequencyReceiveProtocol extends SysMLPort
{
	/**
	 * Standard that specifies the protocol
	 */
	@RequirementSpecificationLink
	@Hyperlink
	public SysMLHyperlink protocolStandard;
	/**
	 * Probability distribution used to randomly corrupt data frames transmitted
	 * over the HF link as a means of simulating communications failures.
	 */
	@Attribute
	public SysMLUniformProbabilityDistribution corruptedFrameProbability;

	/**
	 * Constructor
	 * 
	 * @param context block in whose context the port resides
	 * @param id      unique ID
	 */
	public HighFrequencyReceiveProtocol(StateBehaviorContext context, Long id)
	{
		super(context, id);
	}

	/**
	 * Specializes full port's receive operation to randomly set datalink frame to
	 * be corrupted also setting encapsulating protocol objects to be corrupted as
	 * well.
	 */
	@Override
	@Action
	public void receive(SysMLSignal signal)
	{
		if (signal instanceof HFPulseSignal)
		{
			if (corruptedFrameProbability.nextRandom().value < 0.05)
			{
				HFPulseSignal pulseSignal = (HFPulseSignal) signal;
				HFPulse pulse = pulseSignal.pulse;
				pulse.isCorrupted = true;
				PSKSignal pskSignal = pulse.data;
				pskSignal.isCorrupted = true;
				TDMASlot tdmaSlot = pskSignal.data;
				tdmaSlot.isCorrupted = true;
				DataLinkFrame dataLinkFrame = tdmaSlot.data;
				dataLinkFrame.isCorrupted = true;
			}
			super.receive(signal);
		}
		else
			logger.warning("unexpected signal type: " + signal.getClass().getSimpleName());
	}

	@Override
	protected SysMLAnything clientObjectFor(SysMLSignal signal)
	{
		SysMLAnything result = null;
		if (signal instanceof HFPulseSignal)
		{
			HFPulseSignal pulseSignal = (HFPulseSignal) signal;
			HFPulse pulse = pulseSignal.pulse;
			result = pulse.data;
		}
		else
			logger.warning("unexpected signal type: " + signal.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createAttributes()
	{
		corruptedFrameProbability = new SysMLUniformProbabilityDistribution(new RReal(0.0), new RReal(1.0));
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		protocolStandard = new SysMLHyperlink("IRS for High-Frequency Receive Protocol", "file://IRS for High-Frequency Receive Protocol");
	}
}
