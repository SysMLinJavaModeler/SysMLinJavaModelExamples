package hflink.common.items;

import java.util.Optional;

import sysmlinjava.items.SysMLItem;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.common.StackedProtocolObject;

/**
 * Data frame for the data link protocol. The frame simulates a data structure
 * in a typical data-link protocol which transmits data between two devices such
 * as two modem-radios serving as a data link for transfering IP packets between
 * IP routers.
 * 
 * @author ModelerOne
 */
public class DataLinkFrame extends SysMLItem implements StackedProtocolObject
{
	/**
	 * Flag for indicating a corrupted frame to simulated transmission failures
	 */
	public boolean isCorrupted;
	/**
	 * IP packet data encapsulated by the data line frame
	 */
	@Attribute
	public IPPacket data;

	/**
	 * Constructor
	 * 
	 * @param data data to be encapsulated by the frame
	 */
	public DataLinkFrame(IPPacket data)
	{
		super();
		this.isCorrupted = false;
		this.data = data;
	}

	/**
	 * Constructor
	 * 
	 * @param data data frame with encapsulated data
	 */
	public DataLinkFrame(DataLinkFrame frame)
	{
		this.isCorrupted = frame.isCorrupted;
		this.data = new IPPacket(frame.data);
	}

	/**
	 * Constructor
	 */
	public DataLinkFrame()
	{
		super();
		this.isCorrupted = false;
		this.data = new IPPacket();
	}

	/**
	 * Implementation of the {@code StackedProtocolObject} to return a "stack" of
	 * names of the protocol objects in this protocol stack
	 */
	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.of(data));
	}

	@Override
	public String toString()
	{
		return String.format("DataLinkFrame [isCorrupted=%s, data=%s]", isCorrupted, data);
	}

	/**
	 * Returns the size of the frame in bytes
	 * 
	 * @return frame size in bytes
	 */
	public int sizeBytes()
	{
		return 24 * 1024;
	}
}
