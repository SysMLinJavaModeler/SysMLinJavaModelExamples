package hflink.common.signals;

import java.util.Optional;

import hflink.common.items.DataLinkFrame;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal containing the data frame for the data link protocol. The frame
 * simulates a data structure in a typical data-link protocol which transmits
 * data between two devices such as two modem-radios serving as a data link for
 * transfering IP packets between IP routers.
 * 
 * @author ModelerOne
 *
 */
public class DataLinkFrameSignal extends SysMLSignal
{
	/**
	 * Data link frame value
	 */
	@Attribute
	public DataLinkFrame frame;

	/**
	 * Constructor for the specified data link frams
	 * 
	 * @param dataFrame data link frame value
	 */
	public DataLinkFrameSignal(DataLinkFrame dataFrame)
	{
		super();
		this.frame = dataFrame;
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.empty());
	}

	@Override
	public String toString()
	{
		return String.format("DataLinkFrameSignal [frame=%s]", frame);
	}
}
