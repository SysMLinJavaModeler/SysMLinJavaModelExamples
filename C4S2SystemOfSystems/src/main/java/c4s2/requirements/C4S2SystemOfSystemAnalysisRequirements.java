package c4s2.requirements;

import java.util.List;
import java.util.Optional;

import c4s2.domain.C4S2Domain;
import sysmlinjava.javaannotations.requirements.Requirement;
import sysmlinjava.requirements.RequirementCategoryEnum;
import sysmlinjava.requirements.SysMLRequirement;
import sysmlinjava.requirements.SysMLRequirementsCollection;
import sysmlinjava.requirements.SysMLVerificationMethodKind;

/**
 * Collection of requirements for the C4S2 System-of-Systems analyses.
 */
public class C4S2SystemOfSystemAnalysisRequirements extends SysMLRequirementsCollection
{
	/**
	 * Requirement for objective of the C4S2 Execution analysis, i.e. provide a
	 * graphical animation of the C4S2 system executable model
	 */
	@Requirement
	public static SysMLRequirement c4s2ExecutionAnalysisObjective = new SysMLRequirement(
	"1",
	"C4S2 Execution Analysis",
	"Analysis shall provide a graphical animation of the C4S2 system executable model",
	Optional.empty(),
	Optional.of(C4S2Domain.class),
	List.of(),
	List.of(),
	List.of(),
	false,
	List.of(),
	RequirementCategoryEnum.System,
	List.of(),
	List.of(SysMLVerificationMethodKind.Analysis),
	List.of(),
	List.of());

	/**
	 * Requirement for objective of the SWAPC analysis, i.e. calculate system size, weight, power, cooling of the C4S2 system
	 */
	@Requirement
	public static SysMLRequirement swapcAnalysisObjective = new SysMLRequirement(
		"1",
		"C4S2 Execution Analysis",
		"Analysis shall calculate system size, weight, and power, cooling (SWAPC) sums",
		Optional.empty(),
		Optional.of(C4S2Domain.class),
		List.of(),
		List.of(),
		List.of(),
		false,
		List.of(),
		RequirementCategoryEnum.System,
		List.of(),
		List.of(SysMLVerificationMethodKind.Analysis),
		List.of(),
		List.of());

	static
	{
		validate(C4S2SystemOfSystemAnalysisRequirements.class);
		
		setComposedOfs();
	}

	protected static void setComposedOfs()
	{
		c4s2ExecutionAnalysisObjective.composedOf = List.of();
		swapcAnalysisObjective.composedOf = List.of();
	}
}
