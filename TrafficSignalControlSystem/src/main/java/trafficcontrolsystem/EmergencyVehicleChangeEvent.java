package trafficcontrolsystem;

import java.util.Optional;
import sysmlinjava.events.SysMLChangeEvent;
import sysmlinjava.valuetypes.DirectionDegrees;

/**
 * Event for the direction of approach of an emergency vehicle when present
 * 
 * @author ModelerOne
 *
 */
public class EmergencyVehicleChangeEvent extends SysMLChangeEvent
{
	/**
	 * Optional direction of approach (cardinal direction) of the emergency vehicle,
	 * when it is present
	 */
	public Optional<DirectionDegrees> approachDirection;

	/**
	 * Constructor
	 * 
	 * @param approachDirection Optional direction of approach (cardinal direction)
	 *                          of the emergency vehicle when it is present
	 */
	public EmergencyVehicleChangeEvent(Optional<DirectionDegrees> approachDirection)
	{
		super("EmergencyVehicle");
		this.approachDirection = approachDirection;
	}

	@Override
	protected void createChangeExpression()
	{
		changeExpression = "approachDirection"; 
	}
}
