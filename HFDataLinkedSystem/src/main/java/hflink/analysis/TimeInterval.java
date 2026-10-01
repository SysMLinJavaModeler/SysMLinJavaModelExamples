package hflink.analysis;

import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.attributetypes.RReal;

/**
 * Represents a time interval that corresponds to a category of wait times
 * 
 * @author ModelerOne Kunce;
 *
 */
public class TimeInterval
{
	/**
	 * Name of category
	 */
	public String category;
	/**
	 * Intervals start time
	 */
	public DurationSeconds start;
	/**
	 * Interval's end time
	 */
	public DurationSeconds end;

	/**
	 * Constructor
	 * 
	 * @param category name of category
	 * @param start    interval start seconds
	 * @param end      interval end seconds
	 */
	public TimeInterval(String category, DurationSeconds start, DurationSeconds end)
	{
		super();
		this.category = category;
		this.start = start;
		this.end = end;
	}

	/**
	 * Return whether this time interval includes the specified seconds of wait time
	 * 
	 * @param waitTimeSeconds wait time to be checked
	 * @return True if waitTimeSeconds is in the interval, false othewise
	 */
	public boolean includes(RReal waitTimeSeconds)
	{
		return waitTimeSeconds.value >= start.value && waitTimeSeconds.value < end.value;
	}
}