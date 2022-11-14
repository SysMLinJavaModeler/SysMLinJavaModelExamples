package hflink.common.objects;

import java.util.Optional;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;

/**
 * Representation of the data in a "slot" of transmission via the TDMA protocol
 * 
 * @author ModelerOne
 *
 */
public class TDMASlot extends SysMLClass implements StackedProtocolObject
{
	/**
	 * ID of the slot in the TDMA time slots in which this slot is carried
	 */
	@Attribute
	public Integer slotID;
	/**
	 * Data carried by the slot
	 */
	@Attribute
	public DataLinkFrame data;
	/**
	 * Flag indicating the slot is corrupted for simulating transmission failures
	 */
	public boolean isCorrupted;

	/**
	 * Constructor for transmit of data link frame
	 * 
	 * @param slotID ID of the slot of this TDMA Slot
	 * @param data   data to be carried by the slot
	 */
	public TDMASlot(Integer slotID, DataLinkFrame data)
	{
		super();
		this.isCorrupted = false;
		this.slotID = slotID;
		this.data = data;
	}

	/**
	 * Copy constructor
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
	 * Constructor for default initialization
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
