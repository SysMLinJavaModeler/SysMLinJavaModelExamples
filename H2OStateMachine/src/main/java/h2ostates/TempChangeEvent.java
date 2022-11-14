package h2ostates;

import sysmlinjava.events.SysMLChangeEvent;

/**
 * Event for the change of temperature.
 * 
 * @author ModelerOne
 *
 */
public class TempChangeEvent extends SysMLChangeEvent
{
	/**
	 * Constructor
	 */
	public TempChangeEvent()
	{
		super("Temp change");
	}

	@Override
	protected void createChangeExpression()
	{
		changeExpression = "TempChange";
	}
}
