package hflink.common;

import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.annotations.SysMLHyperlinksCollection;
import sysmlinjava.javaannotations.annotations.Hyperlink;

/**
 * Collection of hyperlinks referenced by elements of the system model
 */
public class HFDatalinkedSystemHyperlinks extends SysMLHyperlinksCollection
{
	/**
	 * Hyperlink to the system specification
	 */
	@Hyperlink
	public static final SysMLHyperlink systemSpec = new SysMLHyperlink("HF Datalink System Specification", "http://AllSpecs.com/SRSServer/HF Datalink System Specification.pdf");
}
