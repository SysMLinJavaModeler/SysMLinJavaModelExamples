package cablestayedbridge;

import java.util.List;
import java.util.Optional;

import sysmlinjava.javaannotations.requirements.Requirement;
import sysmlinjava.requirements.RequirementCategoryEnum;
import sysmlinjava.requirements.SysMLRequirement;
import sysmlinjava.requirements.SysMLRequirementsCollection;
import sysmlinjava.requirements.SysMLVerificationMethodKind;

/**
 * Collection of requirements for the Cable-Stayed Bridge analyses.
 */
public class CableStayedBridgeAnalysisRequirements extends SysMLRequirementsCollection
{
	/**
	 * Requirement for objective of the electric circuit analysis, i.e. perform
	 * constraints on the two-pin components of the circuit
	 */
	@Requirement
	public static SysMLRequirement bridgeDeckLoadsAnalysisObjective = new SysMLRequirement(
	"1",
	"Bridge Deck Loads Analysis",
	"Analysis shall provide display of loads on cables at bridge deck during simulated bridge operations",
	Optional.empty(),
	Optional.of(CableStayedBridge.class),
	List.of(),
	List.of(),
	List.of(),
	false,
	List.of(),
	RequirementCategoryEnum.System,
	List.of(),
	List.of(SysMLVerificationMethodKind.Analysis),
	List.of(),
	List.of()
	);

	static
	{
		validate(CableStayedBridgeAnalysisRequirements.class);
		
		setComposedOfs();
	}

	protected static void setComposedOfs()
	{
		bridgeDeckLoadsAnalysisObjective.composedOf = List.of();
	}
}
