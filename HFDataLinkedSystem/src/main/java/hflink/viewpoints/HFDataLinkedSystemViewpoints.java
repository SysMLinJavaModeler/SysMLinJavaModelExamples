package hflink.viewpoints;

import java.util.List;
import java.util.Optional;

import hflink.domain.HFLinkDomain;
import hflink.requirements.HFDataLinkedSystemConcerns;
import hflink.stakeholders.AcquirerStakeholder;
import hflink.stakeholders.ArchitectStakeholder;
import hflink.stakeholders.DeveloperStakeholder;
import hflink.stakeholders.OperatorStakeholder;
import hflink.tests.HFDataLinkDomainVerificationCase;
import hflink.views.BillOfMaterialsView;
import hflink.views.FrequencyView;
import hflink.views.InteractionsView;
import hflink.views.OperatorInterfaceView;
import hflink.views.PartsView;
import hflink.views.RequirementsView;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.metadata.Issue;
import sysmlinjava.javaannotations.viewpoints.Viewpoint;
import sysmlinjava.metadata.SysMLIssue;
import sysmlinjava.requirements.RequirementCategoryEnum;
import sysmlinjava.requirements.SysMLVerificationMethodKind;
import sysmlinjava.viewpoints.SysMLViewpoint;
import sysmlinjava.viewpoints.SysMLViewpointsCollection;

/**
 * Viewpoints for the system defined by the SysMLinJava model
 * 
 * @author ModelerOne
 *
 */
public class HFDataLinkedSystemViewpoints extends SysMLViewpointsCollection
{
	/**
	 * Issue for missing stakeholder
	 */
	@Issue
	public static SysMLIssue missingStakeholder = new SysMLIssue("Need to add stakeholder, i.e. contractor who will develop the unique modem-radio.  Contractor should have access to architecture, acquirer, and requirements views", "missingContractorStakeholder", 0L);
	/**
	 * Link to documentation explaining SysMLinJava modeling
	 */
	@Hyperlink
	public static SysMLHyperlink sysmlinjavaDocs = new SysMLHyperlink("SysMLinJava Concepts", "https://SysMLinJava.com/SysMLinJava/Concept");
	/**
	 * Link to documentation explaining SysMLinJava model development, testing, and
	 * demonstration using TaskMaster&trade;
	 */
	@Hyperlink
	public static SysMLHyperlink taskMasterDocs = new SysMLHyperlink("SysMLinJava TaskMaster Concepts", "https://SysMLinJava.com/TaskMaster/Concept");

	/**
	 * Viewpoint for the architecture
	 */
	@Viewpoint
	public static SysMLViewpoint architectureViewpoint = new SysMLViewpoint(
	"1",
	"Architecture Viewpoint",
	"Architecture Viewpoint shall provide all SysML graphical displays for system structure and behavior as well as requirements and BOM",
	Optional.empty(),
	Optional.of(HFLinkDomain.class),
	List.of(ArchitectStakeholder.class),
	List.of(HFDataLinkedSystemConcerns.architectureConcern),
	List.of(),
	List.of(),
	RequirementCategoryEnum.Design,
	List.of(PartsView.class, InteractionsView.class, RequirementsView.class, FrequencyView.class, BillOfMaterialsView.class),
	List.of(SysMLVerificationMethodKind.Inspection),
	List.of(HFDataLinkDomainVerificationCase.class),
	List.of(sysmlinjavaDocs, taskMasterDocs));

	/**
	 * Viewpoint for the architecture
	 */
	@Viewpoint
	public static SysMLViewpoint developerViewpoint = new SysMLViewpoint(
	"1",
	"Developer Viewpoint",
	"Developer Viewpoint shall provide all SysML graphical displays for system structure and behavior as well as requirements and BOM",
	Optional.empty(),
	Optional.of(HFLinkDomain.class),
	List.of(DeveloperStakeholder.class),
	List.of(HFDataLinkedSystemConcerns.developerConcern),
	List.of(),
	List.of(),
	RequirementCategoryEnum.Design,
	List.of(PartsView.class, InteractionsView.class, RequirementsView.class, FrequencyView.class, BillOfMaterialsView.class),
	List.of(SysMLVerificationMethodKind.Inspection),
	List.of(HFDataLinkDomainVerificationCase.class),
	List.of(sysmlinjavaDocs, taskMasterDocs));

	/**
	 * Viewpoint for the ops
	 */
	@Viewpoint
	public static SysMLViewpoint opsViewpoint = new SysMLViewpoint(
	"2",
	"Operator Viewpoint",
	"Operator Viewpoint shall provide interactive displays of the operators interface",
	Optional.empty(),
	Optional.of(HFLinkDomain.class),
	List.of(OperatorStakeholder.class),
	List.of(HFDataLinkedSystemConcerns.opsConcern),
	List.of(),
	List.of(),
	RequirementCategoryEnum.Interface,
	List.of(OperatorInterfaceView.class),
	List.of(SysMLVerificationMethodKind.Demonstration),
	List.of(HFDataLinkDomainVerificationCase.class),
	List.of());

	/**
	 * Viewpoint for the acquisition
	 */
	@Viewpoint
	public static SysMLViewpoint acquisitionViewpoint = new SysMLViewpoint(
	"3",
	"Acquisitions Viewpoint",
	"Acquisition viewpoint shall provide tabular display of all system requirements and tabular display of all components estimated costs as well as requirements and BOM",
	Optional.empty(),
	Optional.of(HFLinkDomain.class),
	List.of(AcquirerStakeholder.class),
	List.of(HFDataLinkedSystemConcerns.opsConcern),
	List.of(),
	List.of(),
	RequirementCategoryEnum.Interface,
	List.of(RequirementsView.class, BillOfMaterialsView.class),
	List.of(SysMLVerificationMethodKind.Demonstration),
	List.of(HFDataLinkDomainVerificationCase.class),
	List.of());

	/**
	 * Viewpoint for the requirements
	 */
	@Viewpoint
	public static SysMLViewpoint requirementsViewpoint = new SysMLViewpoint(
	"4",
	"Requirements Viewpoint",
	"Requirements viewpoint shall provide tabular display of all system requirements",
	Optional.empty(),
	Optional.of(HFLinkDomain.class),
	List.of(AcquirerStakeholder.class, OperatorStakeholder.class),
	List.of(HFDataLinkedSystemConcerns.requirementsConcern),
	List.of(),
	List.of(),
	RequirementCategoryEnum.Cost,
	List.of(OperatorInterfaceView.class),
	List.of(SysMLVerificationMethodKind.Analysis),
	List.of(HFDataLinkDomainVerificationCase.class),
	List.of());

}
