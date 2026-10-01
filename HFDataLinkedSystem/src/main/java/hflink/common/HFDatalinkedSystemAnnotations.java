package hflink.common;

import sysmlinjava.annotations.SysMLAnnotationsCollection;
import sysmlinjava.annotations.SysMLComment;
import sysmlinjava.annotations.SysMLDocumentation;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.annotations.SysMLTextualRepresentation;
import sysmlinjava.javaannotations.annotations.Comment;
import sysmlinjava.javaannotations.annotations.Documentation;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.annotations.TextualRepresentation;

/**
 * Collection of annotations referenced by elements of the system model
 */
public class HFDatalinkedSystemAnnotations extends SysMLAnnotationsCollection
{
	/**
	 * Comment about credit
	 */
	@Comment
	public SysMLComment credits = new SysMLComment("Credit given where credit is due");
	/**
	 * Documenation on justification
	 */
	@Documentation
	public SysMLDocumentation justification = new SysMLDocumentation("Justification is: modeler said so");
	/**
	 * Text Representation of process logic
	 */
	@TextualRepresentation
	public SysMLTextualRepresentation processLogic = new SysMLTextualRepresentation("natural", "As long as the container is not full, add liquid in 0.5 liter increments");
	/**
	 * Hyperlink to some specification
	 */
	@Hyperlink
	public SysMLHyperlink somespec = new SysMLHyperlink("Some Specification", "http://ProgramWebServer.OurCompany.com/specs/SomeSpec.pdf");

	static
	{
		validate(HFDatalinkedSystemAnnotations.class);
	}
}
