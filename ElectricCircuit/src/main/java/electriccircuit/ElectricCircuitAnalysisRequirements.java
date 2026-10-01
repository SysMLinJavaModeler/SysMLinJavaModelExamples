package electriccircuit;

import java.util.List;
import java.util.Optional;

import sysmlinjava.javaannotations.requirements.Requirement;
import sysmlinjava.requirements.RequirementCategoryEnum;
import sysmlinjava.requirements.SysMLRequirement;
import sysmlinjava.requirements.SysMLRequirementsCollection;
import sysmlinjava.requirements.SysMLVerificationMethodKind;

/**
 * Collection of requirements for the Electric Circuit analysis.
 */
public class ElectricCircuitAnalysisRequirements extends SysMLRequirementsCollection
{
	/**
	 * Requirement for objective of the electric circuit analysis, i.e. perform
	 * constraints on the two-pin components of the circuit
	 */
	@Requirement
	public static SysMLRequirement performConstraintsOnTwoPinComponents = new SysMLRequirement(
	"1",
	"Two-Pin Component Analysis",
	"Analysis shall apply constraints to voltage and CurrentAmps of two-pin components of electrical circuit",
	Optional.empty(),
	Optional.of(Circuit.class),
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
	 * Requirement for objective of the electric circuit analysis, i.e. perform
	 * constraint on the ground components of the circuit
	 */
	@Requirement
	public static SysMLRequirement performConstraintsOnGroundComponent = new SysMLRequirement(
	"1",
	"Ground Component Analysis",
	"Analysis shall apply constraint to voltage of ground component of electrical circuit",
	Optional.empty(),
	Optional.of(Circuit.class),
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
		validate(ElectricCircuitAnalysisRequirements.class);
		
		setComposedOfs();
	}

	protected static void setComposedOfs()
	{
		performConstraintsOnTwoPinComponents.composedOf = List.of();
		performConstraintsOnGroundComponent.composedOf = List.of();
	}
}
