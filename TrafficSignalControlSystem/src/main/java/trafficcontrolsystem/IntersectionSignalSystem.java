package trafficcontrolsystem;

import java.util.Optional;
import sysmlinjava.attributetypes.BBoolean;
import sysmlinjava.attributetypes.DirectionDegrees;
import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.statemachines.StateMachine;
import sysmlinjava.parts.SysMLPart;

/**
 * SysMLinJava executable model of an individual intersection's traffic signal
 * system that controls signal phases for four approaches to the intersection.
 * The signal phases correspond to the system states. The modeled system
 * consists of values for the signal colors for each of the four approaches for
 * each of the signal phases/states, and a state machine that determines the
 * timing of and transitioning between each of the phases/states. The
 * intersection signal system operates as a sub-state machine to two of the
 * states of the {@code TrafficControlSystem}, i.e. one sub-state machine for
 * normal operations and another sub-state machine for emergency operations.
 * Normal operations provide each approach with a standard red-green-yellow-red
 * cycle, while emergency operations find the intersection providing green to
 * the approaching emergency vehicle and red to all other approaches.
 * <p>
 * The {@code IntersectionSignalSystem} model demonstrates how SysMLinJava can
 * be used to model systems that operate in accordance with more complex state
 * machines, i.e. state machines with pseudo-states for transition "choices" and
 * "junctions" and state machines that operate within states as sub-state
 * machines.
 * 
 * @author ModelerOne
 *
 * @see trafficcontrolsystem.TrafficSignalControlSystem
 * @see trafficcontrolsystem.IntersectionSignalSystemNormalStateMachine
 * @see trafficcontrolsystem.IntersectionSignalSystemEmergencyStateMachine
 */
public class IntersectionSignalSystem extends SysMLPart
{
	/**
	 * Value for signal state (red, green, yellow) of the east-bound approach
	 */
	@Attribute
	public SignalStatesEnum eastBound;
	/**
	 * Value for signal state (red, green, yellow) of the west-bound approach
	 */
	@Attribute
	public SignalStatesEnum westBound;
	/**
	 * Value for signal state (red, green, yellow) of the north-bound approach
	 */
	@Attribute
	public SignalStatesEnum northBound;
	/**
	 * Value for signal state (red, green, yellow) of the south-bound approach
	 */
	@Attribute
	public SignalStatesEnum southBound;

	/**
	 * Value for the duration of the signal phase east/west red, north-south green
	 */
	@Attribute
	public DurationSeconds EWRedNSGrnDuration;
	/**
	 * Value for the duration of the signal phase east/west yellow or north/south
	 * yellow
	 */
	@Attribute
	public DurationSeconds YellowDuration;
	/**
	 * Value for the duration of the signal phase east/west green, north-south red
	 */
	@Attribute
	public DurationSeconds EWGrnNSRedDuration;

	/**
	 * Value that indicates the presence of an emergency vehicle on an approach
	 */
	@Attribute
	public BBoolean emergencyVehiclePresent;
	/**
	 * Value for the approach direction of the emergency vehicle, if present
	 */
	@Attribute
	public DirectionDegrees emergencyVehicleDirection;

	/**
	 * State machine for the signal system during normal operations
	 */
	@StateMachine
	public IntersectionSignalSystemNormalStateMachine stateMachineNormalOps;
	/**
	 * State machine for the signal system during emergency operations
	 */
	@StateMachine
	public IntersectionSignalSystemEmergencyStateMachine stateMachineEmergencyOps;

	/**
	 * Constructor
	 * 
	 * @param contextPart part in whose context the system resides
	 * @param name        unique name
	 * @param id          unique ID
	 */
	public IntersectionSignalSystem(SysMLPart contextPart, String name, Long id)
	{
		super(Optional.of(contextPart), name, id);
	}

	/**
	 * Sets the east-west signals to green, north-south to red
	 */
	@Action
	public void setEWGrnNSRed()
	{
		eastBound.setValue(SignalStatesEnum.Green);
		westBound.setValue(SignalStatesEnum.Green);
		northBound.setValue(SignalStatesEnum.Red);
		southBound.setValue(SignalStatesEnum.Red);
	}

	/**
	 * Sets the east-west signals to yellow, north-south to red
	 */
	@Action
	public void setEWYelNSRed()
	{
		eastBound.setValue(SignalStatesEnum.Yellow);
		westBound.setValue(SignalStatesEnum.Yellow);
		northBound.setValue(SignalStatesEnum.Red);
		southBound.setValue(SignalStatesEnum.Red);
	}

	/**
	 * Sets the east signal yellow, west signal to green, north-south to red
	 */
	@Action
	public void setEYelWGrnNSRed()
	{
		eastBound.setValue(SignalStatesEnum.Yellow);
		westBound.setValue(SignalStatesEnum.Green);
		northBound.setValue(SignalStatesEnum.Red);
		southBound.setValue(SignalStatesEnum.Red);
	}

	/**
	 * Sets the east signal to green, west signal to yellow, north-south to red
	 */
	@Action
	public void setEGrnWYelNSRed()
	{
		eastBound.setValue(SignalStatesEnum.Green);
		westBound.setValue(SignalStatesEnum.Yellow);
		northBound.setValue(SignalStatesEnum.Red);
		southBound.setValue(SignalStatesEnum.Red);
	}

	/**
	 * Sets the east-west signals to red, north-south to green
	 */
	@Action
	public void setEWRedNSGreen()
	{
		eastBound.setValue(SignalStatesEnum.Red);
		westBound.setValue(SignalStatesEnum.Red);
		northBound.setValue(SignalStatesEnum.Green);
		southBound.setValue(SignalStatesEnum.Green);
	}

	/**
	 * Sets the east-west signals to red, north-south to yellow
	 */
	@Action
	public void setEWRedNSYel()
	{
		eastBound.setValue(SignalStatesEnum.Red);
		westBound.setValue(SignalStatesEnum.Red);
		northBound.setValue(SignalStatesEnum.Yellow);
		southBound.setValue(SignalStatesEnum.Yellow);
	}

	/**
	 * Sets the east signal to green, west signal to red, north-south to red
	 */
	@Action
	public void setEGrnWRedNSRed()
	{
		eastBound.setValue(SignalStatesEnum.Green);
		westBound.setValue(SignalStatesEnum.Red);
		northBound.setValue(SignalStatesEnum.Red);
		southBound.setValue(SignalStatesEnum.Red);
	}

	/**
	 * Sets the east signal to red, west signals to green, north-south to red
	 */
	@Action
	public void setERedWGrnNSRed()
	{
		eastBound.setValue(SignalStatesEnum.Red);
		westBound.setValue(SignalStatesEnum.Green);
		northBound.setValue(SignalStatesEnum.Red);
		southBound.setValue(SignalStatesEnum.Red);
	}

	@Override
	protected void createAttributes()
	{
		super.createAttributes();
		eastBound = new SignalStatesEnum(SignalStatesEnum.Green);
		westBound = new SignalStatesEnum(SignalStatesEnum.Green);
		northBound = new SignalStatesEnum(SignalStatesEnum.Red);
		southBound = new SignalStatesEnum(SignalStatesEnum.Red);
		EWGrnNSRedDuration = new DurationSeconds(20);
		EWRedNSGrnDuration = new DurationSeconds(10);
		YellowDuration = new DurationSeconds(5);
		emergencyVehiclePresent = new BBoolean(false);
		emergencyVehicleDirection = new DirectionDegrees(DirectionDegrees.east);
	}
}
