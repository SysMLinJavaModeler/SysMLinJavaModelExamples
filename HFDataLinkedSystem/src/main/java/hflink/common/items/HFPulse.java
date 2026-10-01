package hflink.common.items;

import java.util.Optional;

import sysmlinjava.items.SysMLItem;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.common.StackedProtocolObject;

/**
 * Represents a pulse of HF radio wave transmitted by modem-radio. Simulates a
 * "burst" of information in the form of a phase-shift keyed signal carried over
 * an HF radio wave of short duration
 * 
 * @author ModelerOne
 *
 */
public class HFPulse extends SysMLItem implements StackedProtocolObject
{
	/**
	 * Data object encapsulated by the pulse
	 */
	@Attribute
	public PSKSignal data;
	/**
	 * Flag to indicate pulse is corrupted to simulate transmission failure
	 */
	public boolean isCorrupted;

	/**
	 * Constructor
	 * 
	 * @param data data object to be encapsulated by the pulse
	 */
	public HFPulse(PSKSignal data)
	{
		super();
		this.data = data;
	}

	public HFPulse(HFPulse hfPulse)
	{
		super();
		this.data = new PSKSignal(hfPulse.data);
	}

	public HFPulse()
	{
		super();
		this.data = new PSKSignal();
	}

	@Override
	public String stackNamesString()
	{
		if (data instanceof PSKSignal signal)
			return stackNamesString(this, Optional.of(signal.data));
		else
			return "<none>";
	}

	@Override
	public String toString()
	{
		return String.format("HFPulse [data=%s, isCorrupted=%s]", data, isCorrupted);
	}
}
