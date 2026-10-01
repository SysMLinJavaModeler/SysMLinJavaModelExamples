package hflink.common.signals;

import java.util.Optional;

import hflink.common.items.GPSMessage;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal containing the GPS message sent (broadcast) by GPS to specify the
 * current time. Also specifies the TDMA slot time which, when used in
 * {@code slotID()} operation, specifies which TDMA slot the current time is
 * associated with for TDMA signaling.
 * 
 * @author ModelerOne
 *
 */
public class GPSMessageSignal extends SysMLSignal
{
	/**
	 * GPS message value
	 */
	@Attribute
	public GPSMessage message;

	/**
	 * Constructor for specified GPS message
	 * 
	 * @param message GPS message value
	 */
	public GPSMessageSignal(GPSMessage message)
	{
		super("GPSMessage", 0L);
		this.message = message;
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.empty());
	}

	@Override
	public String toString()
	{
		return String.format("GPSMessageSignal [name=%s, id=%s, message=%s]", name, id, message);
	}
}
