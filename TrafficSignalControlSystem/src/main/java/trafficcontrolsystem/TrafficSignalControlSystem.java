package trafficcontrolsystem;

import java.util.Optional;

import sysmlinjava.attributetypes.BBoolean;
import sysmlinjava.attributetypes.DirectionDegrees;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.metadata.Issue;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.javaannotations.requirements.Requirement;
import sysmlinjava.metadata.SysMLIssue;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.requirements.SysMLRequirement;

/**
 * SysMLinJava executable model of a small traffic control system that provides
 * a priority response to the presence of emergency vehicles. The modeled system
 * consists of four intersection signal systems operating in synchrony with the
 * overall traffic control system. The system operates in either normal
 * operations mode or emergency mode. Normal operations consist of each of the
 * four intersections providing each approach with a standard
 * red-green-yellow-red cycle, while emergency operations find each of the
 * intersections providing green to the approaching emergency vehicle and red to
 * all other approaches. Each intersection signal system operates in accordance
 * with either of two dedicated state machines, one for the normal operations,
 * another for emergency operations. These intersection state machines operate
 * as sub-state machines of the normal operations and emergency operations
 * states of the traffic control system.
 * <p>
 * The {@code TrafficControlSystem} model demonstrates how SysMLinJava can be
 * used to model systems that operate in accordance with more complex state
 * machines, i.e. state machines with pseudo-states for transition "choices" and
 * "junctions" and state machines with states that have sub-state machines.
 * 
 * @author ModelerOne
 *
 * @see trafficcontrolsystem.IntersectionSignalSystem
 * @see trafficcontrolsystem.TrafficSignalControlSystemStateMachine
 */
public class TrafficSignalControlSystem extends SysMLPart
{
	/**
	 * Part that represents one of the intersection signal systems (main at 1st
	 * streets) in the traffic control system
	 */
	@Part
	public IntersectionSignalSystem mainAt1st;
	/**
	 * Part that represents one of the intersection signal systems (main at 2nd
	 * streets) in the traffic control system
	 */
	@Part
	public IntersectionSignalSystem mainAt2nd;
	/**
	 * Part that represents one of the intersection signal systems (main at 3rd
	 * streets)in the traffic control system
	 */
	@Part
	public IntersectionSignalSystem mainAt3rd;
	/**
	 * Part that represents one of the intersection signal systems (main at 4th
	 * streets)in the traffic control system
	 */
	@Part
	public IntersectionSignalSystem mainAt4th;

	/**
	 * Attribute that indicates the presence of an emergency vehicle
	 */
	@Attribute
	public BBoolean emergencyVehiclePresent;
	/**
	 * Attribute that indicates the approaching direction of the emergency vehicle, if
	 * present
	 */
	@Attribute
	public Optional<DirectionDegrees> emergencyVehicleDirection;

	/**
	 * Root requirement for system
	 */
	@Requirement
	public SysMLRequirement rootRequirement;
	
	/**
	 * Capabilities issue for system modeling
	 */
	@Issue
	public SysMLIssue capabilitiesIssue;
	
	/**
	 * Constructor
	 */
	public TrafficSignalControlSystem()
	{
		super("TrafficControlSystem", 0L);
	}

	@Override
	protected void createAttributes()
	{
		emergencyVehiclePresent = BBoolean.False;
		emergencyVehicleDirection = Optional.empty();
	}

	@Override
	protected void createParts()
	{
		mainAt1st = new IntersectionSignalSystem(this, "MainAt1st", 1L);
		mainAt2nd = new IntersectionSignalSystem(this, "MainAt2nd", 2L);
		mainAt3rd = new IntersectionSignalSystem(this, "MainAt3rd", 3L);
		mainAt4th = new IntersectionSignalSystem(this, "MainAt4th", 4L);
	}

	@Override
	protected void createRequirements()
	{
//		rootRequirement = SystemRequirements.id1;
	}

	@Override
	protected void createIssues()
	{
//		capabilitiesIssue = SystemIssues.capabilities;
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new TrafficSignalControlSystemStateMachine(this));
	}
}
