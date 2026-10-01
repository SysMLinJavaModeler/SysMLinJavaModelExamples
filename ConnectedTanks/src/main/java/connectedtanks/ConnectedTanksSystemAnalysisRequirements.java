package connectedtanks;

import java.util.List;
import java.util.Optional;

import sysmlinjava.javaannotations.requirements.Requirement;
import sysmlinjava.requirements.RequirementCategoryEnum;
import sysmlinjava.requirements.SysMLRequirement;
import sysmlinjava.requirements.SysMLRequirementsCollection;
import sysmlinjava.requirements.SysMLVerificationMethodKind;

/**
 * Collection of requirements for the Connected Tanks system analysis.
 */
public class ConnectedTanksSystemAnalysisRequirements extends SysMLRequirementsCollection
{
	/**
	 * Requirement for objective of the connected tanks pipe analysis
	 */
	@Requirement
	public static SysMLRequirement pipeAnalysisObjective = new SysMLRequirement(
	"1",
	"Constrain pipe component",
	"Analysis shall perform constraint on the attributes of the pipe component of connected tanks system",
	Optional.empty(),
	Optional.of(ConnectedTanks.class),
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

	/**
	 * Requirement for objective of the connected tanks pipe analysis
	 */
	@Requirement
	public static SysMLRequirement tankAnalysisObjective = new SysMLRequirement(
	"2",
	"Constrain tank component",
	"Analysis shall perform constraint on the attributes of the tank component of connected tanks system",
	Optional.empty(),
	Optional.of(ConnectedTanks.class),
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
		validate(ConnectedTanksSystemAnalysisRequirements.class);
		
		setComposedOfs();
	}

	protected static void setComposedOfs()
	{
		pipeAnalysisObjective.composedOf = List.of();
		tankAnalysisObjective.composedOf = List.of();
	}
}
