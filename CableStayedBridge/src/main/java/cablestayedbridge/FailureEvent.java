package cablestayedbridge;

import sysmlinjava.events.SysMLEvent;

/**
 * Event for the failure of a component
 * 
 * @author ModelerOne
 *
 */
public class FailureEvent extends SysMLEvent
{
	/**
	 * Constructor
	 */
	public FailureEvent()
	{
		super("failure", 0L);
	}
}
