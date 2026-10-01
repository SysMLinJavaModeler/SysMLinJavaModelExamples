package electriccircuit;

import java.util.Optional;

import sysmlinjava.annotations.SysMLComment;
import sysmlinjava.javaannotations.annotations.Comment;
import sysmlinjava.javaannotations.ports.ProxyPort;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.views.bom.annotations.BOMLineItemComment;

/**
 * {@code Ground} is the SysMLinJava model of a grounding component of the
 * {@code ElectricCircuit} system. The model is a SysMLinJava implementation of
 * the ground model described in "SysML Extension for Physical Interaction and
 * Signal Flow Simulation", Object Management Group, Inc., 2018. The
 * {@code Ground} is not a {@code TwoPinElectricalComponent} and is therefore
 * characterized by its single interface, i.e. its zero voltage pin/port
 * interface. It has no other properties.
 * <p>
 * The {@code Ground} model is as specified by its constraints which are
 * declared in the {@code GroundConstraint} block. The {@code GroundConstraint}
 * block is used to validate/verify the ground model's execution, i.e. that it
 * is always zero volts at its pin.
 * 
 * @implNote The {@code Ground} is annotated for use in generating a bill-of-materials.
 * 
 * @see <a href="https://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @author ModelerOne
 *
 */
public class Ground extends SysMLPart
{
	/**
	 * Procy port for the single pin interface
	 */
	@ProxyPort
	ChargeFlowElement p;

	/**
	 * Comment for bill-of-materials
	 */
	@BOMLineItemComment
	@Comment
	SysMLComment comment;

	/**
	 * Constructor
	 * 
	 * @param name unique name for the ground
	 */
	public Ground(String name)
	{
		super(name, 0L);
	}

	@Override
	protected void createPorts()
	{
		p = new ChargeFlowElement(this, Optional.of(this), 0L);
	}

	@Override
	protected void createComments()
	{
		super.createComments();
		comment = new SysMLComment("Description:Ground for entire circuit\nSource:n/a");
	}
}
