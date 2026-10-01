package hflink.requirements;

import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.annotations.SysMLHyperlinksCollection;
import sysmlinjava.javaannotations.annotations.Hyperlink;

/**
 * Collection of HF Data Link System's supporting information (hyper)links
 */
public class HFDataLinkedSystemHyperlinks extends SysMLHyperlinksCollection
{
	/**
	 * Hyperlink for the system requirements specification
	 */
	@Hyperlink
	public static SysMLHyperlink systemSpec = new SysMLHyperlink(
	"HF Datalink System Specification",
	"http://AllSpecs.com/SRSServer/HF Datalink System Specification.pdf", "systemSpec", 0L);

	/**
	 * Hyperlink to C2 Subsystem's requirements specification
	 */
	@Hyperlink
	public static SysMLHyperlink c2SubsystemSpec = new SysMLHyperlink("C2 Subsystem Requirements Specification",
	"https://SpecServer.com/RequirementSpecs/C2 Subsystem Requirements Specification.pdf", "c2SubsystemSpec", 0L);

	/**
	 * Hyperlink to Deployed Subsystem's requirements specification
	 */
	@Hyperlink
	public static SysMLHyperlink deployedSubsystemSpec = new SysMLHyperlink("Deployed Subsystem Requirements Specification",
	"https://SpecServer.com/RequirementSpecs/Deployed Subsystem Requirements Specification.pdf", "deployedSubsystemSpec", 0L);

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

	static
	{
		validate(HFDataLinkedSystemHyperlinks.class);
	}
}
