package motorinwheel.analysis;

import java.util.List;
import java.util.Optional;

import motorinwheel.systems.vehicle.Vehicle;
import sysmlinjava.javaannotations.requirements.Requirement;
import sysmlinjava.requirements.RequirementCategoryEnum;
import sysmlinjava.requirements.SysMLRequirement;
import sysmlinjava.requirements.SysMLRequirementsCollection;
import sysmlinjava.requirements.SysMLVerificationMethodKind;

/**
 * Collection of requirements for the Robotic Mower system analysis.
 */
public class MotorInWheelSystemAnalysisRequirements extends SysMLRequirementsCollection
{
	/**
	 * Requirement for objective of the animation analysis, i.e. calculate motor-in-wheels energy over distance traveled
	 */
	@Requirement
	public static SysMLRequirement wheelEnergyAnalysisObjective = new SysMLRequirement(
		"1",
		"Motor-in-Wheel Energy Analysis",
		"Analysis shall calculate motor-in-wheels energy over distance traveled",
		Optional.empty(),
		Optional.of(Vehicle.class),
		List.of(),
		List.of(),
		List.of(),
		false,
		List.of(),
		RequirementCategoryEnum.StatesTransitions,
		List.of(),
		List.of(SysMLVerificationMethodKind.Analysis),
		List.of(),
		List.of());

	/**
	 * Requirement for objective of the animation analysis, i.e. calculate motor-in-wheels energy over distance traveled
	 */
	@Requirement
	public static SysMLRequirement vehicleEnergyAnalysisObjective = new SysMLRequirement(
		"1",
		"Motor-in-Wheel Energy Analysis",
		"Analysis shall calculate total vehicle energy and vehicle enery per distance for executing model",
		Optional.empty(),
		Optional.of(Vehicle.class),
		List.of(),
		List.of(),
		List.of(),
		false,
		List.of(),
		RequirementCategoryEnum.Performance,
		List.of(),
		List.of(SysMLVerificationMethodKind.Analysis),
		List.of(),
		List.of());


	static
	{
		validate(MotorInWheelSystemAnalysisRequirements.class);
		
		setComposedOfs();
	}

	protected static void setComposedOfs()
	{
		wheelEnergyAnalysisObjective.composedOf = List.of();
		vehicleEnergyAnalysisObjective.composedOf = List.of();
	}
}
