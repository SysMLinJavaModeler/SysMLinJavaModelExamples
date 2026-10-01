package hflink.common.items;

import java.util.Optional;

import sysmlinjava.items.SysMLItem;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.common.StackedProtocolObject;

/**
 * Representation of the phase-shift keyed signal that carries the information
 * of a slot in the TDMA protocol
 * 
 * @author ModelerOne
 *
 */
public class PSKSignal extends SysMLItem implements StackedProtocolObject
{
	/**
	 * Whether or not this signal is "corrupted", i.e. for simulation of
	 * transmission interference, jamming, etc.
	 */
	public boolean isCorrupted;
	/**
	 * TDMA slot's current data content
	 */
	@Attribute
	public TDMASlot data;

	/**
	 * Constructor for the specified TDMA slot data
	 * 
	 * @param data TDMA slot's value
	 */
	public PSKSignal(TDMASlot data)
	{
		super();
		this.isCorrupted = false;
		this.data = data;
	}

	/**
	 * Constructor of copy of specified PSK signal
	 * 
	 * @param pskSignal PSK signal value to be copy of
	 */
	public PSKSignal(PSKSignal pskSignal)
	{
		super();
		this.isCorrupted = pskSignal.isCorrupted;
		this.data = new TDMASlot(pskSignal.data);
	}

	/**
	 * Constructor for unspecified TDMA slot
	 */
	public PSKSignal()
	{
		super();
		this.isCorrupted = false;
		this.data = new TDMASlot();
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.of(data));
	}

	@Override
	public String toString()
	{
		return String.format("PSKSignal [isCorrupted=%s, data=%s]", isCorrupted, data);
	}

}
