package dbssystem.controller;

import java.util.List;
import java.util.Optional;

import sysmlinjava.javaannotations.requirements.Requirement;
import sysmlinjava.requirements.RequirementCategoryEnum;
import sysmlinjava.requirements.SysMLRequirement;
import sysmlinjava.requirements.SysMLRequirementsCollection;
import sysmlinjava.requirements.SysMLVerificationMethodKind;

/**
 * Collection of requirements for the DBS System analysis.
 */
public class DBSControllerAnalysisRequirements extends SysMLRequirementsCollection
{
	/**
	 * Requirement for objective of the DBS System analysis, i.e. simulate controller use of the DBS ANN
	 */
	@Requirement
	public static SysMLRequirement dbsControllerAnalysisObjective = new SysMLRequirement(
	"1",
	"DBS System Analysis",
	"Analysis shall simulate the controller use of the ANN for DBS",
	Optional.empty(),
	Optional.of(DBSController.class),
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
	Optional.of(DBSController.class),
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
		validate(DBSControllerAnalysisRequirements.class);
		
		setComposedOfs();
	}

	protected static void setComposedOfs()
	{
		dbsControllerAnalysisObjective.composedOf = List.of();
	}
}
