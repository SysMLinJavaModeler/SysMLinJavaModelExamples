package h2ostates;

import java.util.List;
import java.util.Optional;

import sysmlinjava.annotations.SysMLDocumentation;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.javaannotations.annotations.Documentation;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.requirements.Requirement;
import sysmlinjava.requirements.RequirementCategoryEnum;
import sysmlinjava.requirements.SysMLRequirement;
import sysmlinjava.requirements.SysMLRequirementsCollection;
import sysmlinjava.requirements.SysMLVerificationMethodKind;

/**
 * Collection of requirements for the verificvation/test of the H2O States model
 */
public class H2OStatesVerificationRequirements extends SysMLRequirementsCollection
{
	/**
	 * Documentation for the text-based constraint for the objective of the verification
	 */
	@Documentation
	public static SysMLDocumentation objectiveDoc = new SysMLDocumentation("Test shall verify state machine definition of H2O is correct");

	/**
	 * Hyperlink to the book that prescribes the H2O States model
	 */
	@Hyperlink
	public static SysMLHyperlink friedenthalBook = new SysMLHyperlink("A Practical Guide to SysML - The Systems Modeling Language, 3rd edition, by Sanford Friedenthal, et al; Object Management Group; Morgan Kaufman publisher", "https://shop.elsevier.com/books/a-practical-guide-to-sysml/friedenthal/978-0-12-800202-5");

	/**
	 * Requirement/objective for the verification case
	 */
	@Requirement
	public static SysMLRequirement objective = new SysMLRequirement(
	"1",
	"H2O States Test Objective",
	"Test shall verify state machine definition of H2O is correct",
	Optional.empty(),
	Optional.of(H2O.class),
	List.of(),
	List.of(),
	List.of(),
	false,
	List.of(),
	RequirementCategoryEnum.StatesTransitions,
	List.of(),
	List.of(SysMLVerificationMethodKind.Test),
	List.of(H2OStatesVerificationCase.class),
	List.of(friedenthalBook));
	
	static
	{
		validate(H2OStatesVerificationRequirements.class);
	}
}
