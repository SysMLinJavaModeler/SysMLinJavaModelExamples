package hflink.common.items;

import java.util.Optional;

import sysmlinjava.items.SysMLItem;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.common.StackedProtocolObject;

/**
 * Representation of the data in a "slot" of transmission via the TDMA protocol
 * 
 * @author ModelerOne
 *
 */
public class TDMASlot extends SysMLItem implements StackedProtocolObject
{
	/**
	 * ID of the slot in the TDMA time slots in which this slot is carried
	 */
	@Attribute
	public Integer slotID;
	/**
	 * Data frame carried by the slot
	 */
	@Attribute
	public DataLinkFrame data;
	/**
	 * Flag indicating the slot is corrupted for simulating transmission failures
	 * due to interference, jamming, etc.
	 */
	public boolean isCorrupted;

	/**
	 * Constructor for specified data link frame
	 * 
	 * @param slotID    ID of the slot of this TDMA Slot
	 * @param dataFrame data frame to be contained in the slot
	 */
	public TDMASlot(Integer slotID, DataLinkFrame dataFrame)
	{
		super();
		this.isCorrupted = false;
		this.slotID = slotID;
		this.data = dataFrame;
	}

	/**
	 * Constructor of copy of specified TDMA slot data
	 * 
	 * @param data data to be copy of
	 */
	public TDMASlot(TDMASlot data)
	{
		super();
		this.isCorrupted = false;
		this.slotID = data.slotID;
		this.data = new DataLinkFrame(data.data);
	}

	/**
	 * Constructor for unspecified data frame
	 */
	public TDMASlot()
	{
		super();
		this.isCorrupted = false;
		this.slotID = 0;
		this.data = new DataLinkFrame();
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.of(data));
	}

	@Override
	public String toString()
	{
		return String.format("TDMASlot [isCorrupted=%s, slotID=%s, data=%s]", isCorrupted, slotID, data);
	}
}
