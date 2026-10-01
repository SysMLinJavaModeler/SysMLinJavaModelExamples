package hflink.common.items;

import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.items.SysMLItem;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Representation of the GPS message sent (broadcast) by GPS to specify the
 * current time. Also specifies the TDMA slot time which, when used in
 * {@code slotID()} operation, specifies which TDMA slot the current time is
 * associated with for TDMA signaling.
 * 
 * @author ModelerOne
 *
 */
public class GPSMessage extends SysMLItem
{
	/**
	 * Integer ID of source of the message
	 */
	@Attribute
	public Integer sourceID;
	/**
	 * Time of message
	 */
	@Attribute
	public InstantMilliseconds time;
	/**
	 * Number cycles of TDMA access by all members in the network
	 */
	public static final double cyclesPerMinute = 2;
	/**
	 * Number TDMA slots per cycle
	 */
	public static final double slotsPerCycle = 4;
	/**
	 * Number seconds per TDMA slot, i.e. time allocated to each member in network
	 * to access a slot in a cycle of access.
	 */
	public static final double secondsPerSlot = 60.0 / cyclesPerMinute / slotsPerCycle;

	/**
	 * Constructor
	 * 
	 * @param sourceID ID of the source of the message
	 * @param time     time of message
	 */
	public GPSMessage(Integer sourceID, InstantMilliseconds time)
	{
		super();
		this.sourceID = sourceID;
		this.time = time;
	}

	public GPSMessage(GPSMessage gpsMessage)
	{
		super();
		this.sourceID = gpsMessage.sourceID;
		this.time = new InstantMilliseconds(gpsMessage.time);
	}

	public GPSMessage()
	{
		super();
		this.sourceID = 0;
		this.time = InstantMilliseconds.now();
	}

	/**
	 * Returns the ID of the TDMA slot associated with this message time
	 * 
	 * @return slotID for this time
	 */
	public int slotID()
	{
		return (int)(((time.seconds() % (60.0 /cyclesPerMinute)) / secondsPerSlot));
	}

	@Override
	public String toString()
	{
		return String.format("GPSMessage [sourceID=%s, time=%s]", sourceID, time);
	}
}
