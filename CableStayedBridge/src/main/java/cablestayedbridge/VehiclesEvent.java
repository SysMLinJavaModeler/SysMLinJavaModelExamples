package cablestayedbridge;

import sysmlinjava.events.SysMLChangeEvent;

/**
 * Event for a change in a vehicle's state, i.e. its location after a move
 * 
 * @author ModelerOne
 *
 */
public class VehiclesEvent extends SysMLChangeEvent
{
	/**
	 * Constructor
	 * 
	 * @param name unique name
	 */
	public VehiclesEvent(String name)
	{
		super(name);
	}

	@Override
	protected void createChangeExpression()
	{
		changeExpression = "Move";
	}

}
