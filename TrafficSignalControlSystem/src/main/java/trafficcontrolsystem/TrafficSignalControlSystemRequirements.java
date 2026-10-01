package trafficcontrolsystem;

import java.util.List;
import java.util.Optional;

import sysmlinjava.javaannotations.requirements.Requirement;
import sysmlinjava.requirements.RequirementCategoryEnum;
import sysmlinjava.requirements.SysMLRequirement;
import sysmlinjava.requirements.SysMLRequirementsCollection;

/**
 * Collection of requirements for the {@code TrafficSignalControlSystem}. Full
 * system reqirements are TBD with only one - testing objective - provided this
 * far.
 */
public class TrafficSignalControlSystemRequirements extends SysMLRequirementsCollection
{
	/**
	 * Requirement for basic system
	 */
	@Requirement
	public static SysMLRequirement trafficControl = new SysMLRequirement("1", "Traffic Control",
	"System shall control traffic for specified intersections",
	Optional.empty(),
	Optional.of(TrafficSignalControlSystem.class),
	List.of(),
	List.of(),
	List.of(),
	false,
	List.of(),
	RequirementCategoryEnum.Functional,
	List.of(),
	List.of(),
	List.of(),
	List.of());
	/**
	 * Requirement for basic system normal operations
	 */
	@Requirement
	public static SysMLRequirement normalTrafficControl = new SysMLRequirement("1.1", "Traffic Control - Normal",
	"System shall control normal traffic",
	Optional.empty(),
	Optional.of(TrafficSignalControlSystem.class),
	List.of(),
	List.of(),
	List.of(),
	false,
	List.of(),
	RequirementCategoryEnum.Functional,
	List.of(),
	List.of(),
	List.of(),
	List.of());

	/**
	 * Requirement for basic system emergency operations
	 */
	@Requirement
	public static SysMLRequirement emergencyTrafficControl = new SysMLRequirement("1.2", "Traffic Control - Emergency",
	"System shall control emergency traffic",
	Optional.empty(),
	Optional.of(TrafficSignalControlSystem.class),
	List.of(),
	List.of(),
	List.of(),
	false,
	List.of(),
	RequirementCategoryEnum.Functional,
	List.of(),
	List.of(),
	List.of(),
	List.of());

	/**
	 * Requirement/objective of verification testing
	 */
	@Requirement
	public static SysMLRequirement testObjective = new SysMLRequirement("2", "Traffic Control System Test Objective",
	"Test shall verify behavior of system for random emergency vehicle is correct",
	Optional.empty(),
	Optional.of(TrafficSignalControlSystem.class),
	List.of(),
	List.of(),
	List.of(),
	false,
	List.of(),
	RequirementCategoryEnum.Verification,
	List.of(),
	List.of(),
	List.of(),
	List.of());

	static
	{
		validate(TrafficSignalControlSystemRequirements.class);
		
		setComposedOfs();
	}

	protected static void setComposedOfs()
	{
		trafficControl.composedOf = List.of(normalTrafficControl, emergencyTrafficControl);
	}
}
