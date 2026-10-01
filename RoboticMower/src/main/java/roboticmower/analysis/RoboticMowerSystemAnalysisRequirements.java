package roboticmower.analysis;

import java.util.List;
import java.util.Optional;

import roboticmower.RoboticMowerDomain;
import sysmlinjava.javaannotations.requirements.Requirement;
import sysmlinjava.requirements.RequirementCategoryEnum;
import sysmlinjava.requirements.SysMLRequirement;
import sysmlinjava.requirements.SysMLRequirementsCollection;
import sysmlinjava.requirements.SysMLVerificationMethodKind;

/**
 * Collection of requirements for the Robotic Mower system analysis.
 */
public class RoboticMowerSystemAnalysisRequirements extends SysMLRequirementsCollection
{
	/**
	 * Requirement for objective of the animation analysis, i.e. graphical animation
	 * of the robotic mower domain in executable model
	 */
	@Requirement
	public static SysMLRequirement animationAnalysisObjective = new SysMLRequirement(
	"1",
	"Robotic Mower Domain Animation",
	"Analysis shall provide a graphical animation of the robotic mower domain executable model",
	Optional.empty(),
	Optional.of(RoboticMowerDomain.class),
	List.of(),
	List.of(),
	List.of(),
	false,
	List.of(),
	RequirementCategoryEnum.Functional,
	List.of(),
	List.of(SysMLVerificationMethodKind.Analysis),
	List.of(),
	List.of());


	/**
	 * Requirement for objective of the waypoints analysis, i.e. graphical display
	 * of the waypoint error frequency distribution of the robotic mower's
	 * executable model
	 */
	@Requirement
	public static SysMLRequirement waypointsAnalysisObjective = new SysMLRequirement(
	"2",
	"Robotic Mower Waypoint Error Frequency Analysis",
	"Analysis shall provide a graphical display of the waypoint error frequency distribution of the robotic mower's executable model",
	Optional.empty(),
	Optional.of(RoboticMowerDomain.class),
	List.of(),
	List.of(),
	List.of(),
	false,
	List.of(),
	RequirementCategoryEnum.Capability,
	List.of(),
	List.of(SysMLVerificationMethodKind.Analysis),
	List.of(),
	List.of());
	
	static
	{
		validate(RoboticMowerSystemAnalysisRequirements.class);
		
		setComposedOfs();
	}

	protected static void setComposedOfs()
	{
		animationAnalysisObjective.composedOf = List.of();
		waypointsAnalysisObjective.composedOf = List.of();
	}
}
