package hflink.common.ports;

import hflink.common.objects.DataLinkFrame;
import hflink.common.objects.HFPulse;
import hflink.common.objects.PSKSignal;
import hflink.common.objects.TDMASlot;
import hflink.common.signals.HFPulseSignal;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.requirements.RequirementSpecificationLink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.common.SysMLSignal;
import sysmlinjava.ports.SysMLFullPort;
import sysmlinjava.probability.SysMLUniformProbabilityDistribution;

/**
 * Port to simulated the protocol to receive pulses of HF radio waves carrying
 * data link information
 * 
 * @author ModelerOne
 *
 */
public class HighFrequencyReceiveProtocol extends SysMLFullPort
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
	@Value
	public SysMLUniformProbabilityDistribution corruptedFrameProbability;

	/**
	 * Constructor
	 * 
	 * @param contextBlock block in whose context the port resides
	 * @param id           unique ID
	 */
	public HighFrequencyReceiveProtocol(SysMLBlock contextBlock, Long id)
	{
		super(contextBlock, id);
	}

	/**
	 * Specializes full port's receive operation to randomly set datalink frame to
	 * be corrupted also setting encapsulating protocol objects to be corrupted as
	 * well.
	 */
	@Override
	@Operation
	public void receive(SysMLSignal signal)
	{
		if (signal instanceof HFPulseSignal)
		{
			if (corruptedFrameProbability.nextRandom() < 0.05)
			{
				HFPulseSignal pulseSignal = (HFPulseSignal)signal;
				HFPulse pulse = pulseSignal.pulse;
				pulse.isCorrupted = true;
				PSKSignal pskSignal = (PSKSignal)pulse.data;
				pskSignal.isCorrupted = true;
				TDMASlot tdmaSlot = (TDMASlot)pskSignal.data;
				tdmaSlot.isCorrupted = true;
				DataLinkFrame dataLinkFrame = (DataLinkFrame)tdmaSlot.data;
				dataLinkFrame.isCorrupted = true;
			}
			super.receive(signal);
		}
		else
			logger.warning("unexpected signal type: " + signal.getClass().getSimpleName());
	}

	@Override
	protected SysMLClass clientObjectFor(SysMLSignal signal)
	{
		SysMLClass result = null;
		if (signal instanceof HFPulseSignal)
		{
			HFPulseSignal pulseSignal = (HFPulseSignal)signal;
			HFPulse pulse = pulseSignal.pulse;
			result = pulse.data;
		}
		else
			logger.warning("unexpected signal type: " + signal.getClass().getSimpleName());
		return result;
	}

	@Override
	protected void createValues()
	{
		corruptedFrameProbability = new SysMLUniformProbabilityDistribution(0.0, 1.0);
	}

	@Override
	protected void createHyperlinks()
	{
		protocolStandard = new SysMLHyperlink("IRS for High-Frequency Receive Protocol", "file://IRS for High-Frequency Receive Protocol");
	}
}
