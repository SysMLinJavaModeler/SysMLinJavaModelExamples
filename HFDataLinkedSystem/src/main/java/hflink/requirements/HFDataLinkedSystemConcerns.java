package hflink.requirements;

import java.util.List;
import java.util.Optional;

import hflink.components.modemradio.ModemRadio;
import hflink.domain.HFLinkDomain;
import hflink.stakeholders.AcquirerStakeholder;
import hflink.stakeholders.ArchitectStakeholder;
import hflink.stakeholders.DeveloperStakeholder;
import hflink.stakeholders.OperatorStakeholder;
import hflink.systems.c2system.CommandControlSystem;
import hflink.systems.gps.GPS;
import hflink.tests.HFDataLinkDomainVerificationCase;
import sysmlinjava.javaannotations.requirements.Concern;
import sysmlinjava.requirements.RequirementCategoryEnum;
import sysmlinjava.requirements.SysMLConcern;
import sysmlinjava.requirements.SysMLConcernsCollection;
import sysmlinjava.requirements.SysMLVerificationMethodKind;

/**
 * Collection of the concerns for the HF Data-Link System
 */
public class HFDataLinkedSystemConcerns extends SysMLConcernsCollection
{

	/**
	 * Concern for the architecture
	 */
	@Concern
	public static SysMLConcern architectureConcern = new SysMLConcern(
	"architectureConcern",
	"Architectural Concern",
	"Model provides comprehensive specification of the system",
	Optional.empty(),
	Optional.of(HFLinkDomain.class),
	List.of(), List.of(), List.of(),
	RequirementCategoryEnum.System,
	List.of(), List.of(), List.of(), List.of());

	/**
	 * Concern for the system development
	 */
	@Concern
	public static SysMLConcern developerConcern = new SysMLConcern(
	"developerConcern",
	"Developer Concern",
	"Model provides comprehensive specification of the system to include structure, behavior, and development constraints",
	Optional.empty(),
	Optional.of(HFLinkDomain.class),
	List.of(), List.of(), List.of(),
	RequirementCategoryEnum.System,
	List.of(), List.of(), List.of(), List.of());

	/**
	 * Concern for the system operation
	 */
	@Concern
	public static SysMLConcern opsConcern = new SysMLConcern(
	"opsConcern",
	"Operational Concern",
	"System defined by model meets the ops's (operator's) needs",
	Optional.empty(),
	Optional.of(HFLinkDomain.class),
	List.of(), List.of(), List.of(),
	RequirementCategoryEnum.Suitability,
	List.of(), List.of(), List.of(), List.of());

	/**
	 * Concern for the system acquisition
	 */
	@Concern
	public static SysMLConcern acquisitionConcern = new SysMLConcern(
	"acquisitionConcern",
	"Acquisition Concern",
	"System defined by model is affordable and achievable",
	Optional.empty(),
	Optional.of(HFLinkDomain.class),
	List.of(), List.of(), List.of(),
	RequirementCategoryEnum.Cost,
	List.of(), List.of(), List.of(), List.of());

	/**
	 * Concern for the modem-radio requirements
	 */
	@Concern
	public static SysMLConcern requirementsConcern = new SysMLConcern(
	"requirementsConcern",
	"Requirements Concern",
	"Requirements of unique modem-radio component are fully specified",
	Optional.empty(),
	Optional.of(ModemRadio.class),
	List.of(), List.of(), List.of(),
	RequirementCategoryEnum.Component,
	List.of(), List.of(), List.of(), List.of());

	/**
	 * Concern for the system availability
	 */
	@Concern
	public static SysMLConcern subsystemAvailabilityConcern = new SysMLConcern(
	"subsystemAvailabilityConcern",
	"Subsystems available",
	"Subsystems shall be available in time for inclusion in system development",
	Optional.empty(),
	Optional.of(CommandControlSystem.class),
	List.of(AcquirerStakeholder.class),
	List.of(GPS.class),
	List.of(),
	RequirementCategoryEnum.System,
	List.of(),
	List.of(SysMLVerificationMethodKind.Inspection),
	List.of(HFDataLinkDomainVerificationCase.class),
	List.of(HFDataLinkedSystemHyperlinks.systemSpec));
	
	/**
	 * Concern for system affordability
	 */
	@Concern
	public static SysMLConcern affordabilityConcern = new SysMLConcern(
	"affordabilityConcern",
	"System affordable",
	"System shall be affordable per budgeted amount",
	Optional.empty(),
	Optional.of(HFLinkDomain.class),
	List.of(AcquirerStakeholder.class),
	List.of(GPS.class),
	List.of(),
	RequirementCategoryEnum.Cost,
	List.of(),
	List.of(SysMLVerificationMethodKind.Analysis),
	List.of(HFDataLinkDomainVerificationCase.class),
	List.of(HFDataLinkedSystemHyperlinks.systemSpec));
	
	/**
	 * Concern for system development feasability
	 */
	@Concern
	public static SysMLConcern feasibilityConcern = new SysMLConcern(
	"feasibilityConcern",
	"System feasability",
	"System development shall be feasable",
	Optional.empty(),
	Optional.of(HFLinkDomain.class),
	List.of(OperatorStakeholder.class, ArchitectStakeholder.class),
	List.of(GPS.class),
	List.of(),
	RequirementCategoryEnum.System,
	List.of(),
	List.of(SysMLVerificationMethodKind.Analysis),
	List.of(HFDataLinkDomainVerificationCase.class),
	List.of(HFDataLinkedSystemHyperlinks.systemSpec));

	/**
	 * Concern for system usability
	 */
	@Concern
	public static SysMLConcern usabilityConcern = new SysMLConcern(
	"usabilityConcern",
	"User concern for usability",
	"System is usable",
	Optional.empty(),
	Optional.of(HFLinkDomain.class),
	List.of(OperatorStakeholder.class, DeveloperStakeholder.class),
	List.of(GPS.class),
	List.of(),
	RequirementCategoryEnum.User,
	List.of(),
	List.of(SysMLVerificationMethodKind.Test),
	List.of(HFDataLinkDomainVerificationCase.class),
	List.of(HFDataLinkedSystemHyperlinks.systemSpec));
	
	/**
	 * Concern for system user trainability
	 */
	@Concern
	public static SysMLConcern trainabilityConcern = new SysMLConcern(
	"trainabilityConcern",
	"User concern for trainability",
	"System use is trainable",
	Optional.empty(),
	Optional.of(HFLinkDomain.class),
	List.of(OperatorStakeholder.class, DeveloperStakeholder.class),
	List.of(GPS.class),
	List.of(),
	RequirementCategoryEnum.Training,
	List.of(),
	List.of(SysMLVerificationMethodKind.Test),
	List.of(HFDataLinkDomainVerificationCase.class),
	List.of(HFDataLinkedSystemHyperlinks.systemSpec));

	/**
	 * Concern for specified susbsystem not yet developed for use in system
	 */
	@Concern
	public static SysMLConcern tbdSubsystem = new SysMLConcern(
	"c1",
	"TBD Subsystem",
	"Specified subsystem is yet to be developed",
	Optional.empty(),
	Optional.of(CommandControlSystem.class),
	List.of(AcquirerStakeholder.class),
	List.of(),
	List.of(),
	RequirementCategoryEnum.Subsystem,
	List.of(),
	List.of(),
	List.of(),
	List.of());

	/**
	 * Concern for capability of operator to review the system's process result
	 */
	@Concern
	public static SysMLConcern reviewCapabilityIncluded = new SysMLConcern(
	"c2",
	"Review Capability",
	"Capability to review process result must be included",
	Optional.empty(),
	Optional.of(HFLinkDomain.class),
	List.of(OperatorStakeholder.class),
	List.of(),
	List.of(),
	RequirementCategoryEnum.Capability,
	List.of(),
	List.of(),
	List.of(),
	List.of());
	
	/**
	 * Static operation that should be overridden/hidden to invoke the operation to
	 * set the {@code setComposedOfs} method:
	 *
	 * <pre>
		public class MyConcernsCollection extends SysMLConcernCollection
		{
				:
			static void createConcernsList()
			{
				:
			}
	
			static void setComposedOfs()
			{
				:
			}
	
			static void setDerivedRquirements()
			{
				:
			}
	
			static void setDerivedFromRquirement()
			{
				:
			}
	
			static
			{
				createConcernsList();
				setComposedOfs();
				setDerivedRquirements();
				setDerivedFromRquirement();
			}
		}
	 * </pre>
	 */
	static
	{
		validate(HFDataLinkedSystemConcerns.class);
		
		setComposedOfs();
		setDerivedConcerns();
		setDerivedFromConcern();
	}

	/**
	 * Sets no decompositions of concerns
	 */
	protected static void setComposedOfs()
	{
	}

	/**
	 * Sets no derived concerns
	 */
	public static void setDerivedConcerns()
	{
	}
	
	/**
	 * Static operation that may be overridden/hidden to set the value of the
	 * {@code derivedFromConcern} variable of each of the declared concerns. This
	 * operation may be used in lieu of setting the {@code derivedFromConcern} value
	 * in the {@code SysMLConcern}'s constructor when declarations of the derived
	 * concerns are not yet visible in code. An example follows:
	 *
	 * <pre>
		public class MyConcernsCollection extends SysMLConcernCollection
		{
			protected static void setDerivedFromRquirement()
			{
					:
				req2_1.derivedFromConcern = Optional.of(req3_1);
					:
			}
				:
			static
			{
				createConcernsList();
				setComposedOfs();
				setDerivedConcerns();
				setDerivedFromConcern();
			}
		}
	 * </pre>
	 */
	public static void setDerivedFromConcern()
	{
	}
}
