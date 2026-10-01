package electriccircuit;

import java.util.Optional;

import sysmlinjava.annotations.SysMLComment;
import sysmlinjava.attributetypes.CurrentAmps;
import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.attributetypes.Voltage;
import sysmlinjava.javaannotations.annotations.Comment;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.ports.ProxyPort;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.views.bom.annotations.BOMLineItemComment;

/**
 * {@code TwoPinElectricalComponent} is the SysMLinJava model of a common
 * two-pin, positive and negative, electrical component such as a resistor,
 * capacitor, or inductor, etc. as part of the {@code ElectricCircuit} system.
 * The {@code TwoPinElectricalComponent} is specialized/extended for each of
 * these types of components.
 * <p>
 * The {@code TwoPinElectricalComponent} model is as specified by its
 * constraints which are declared in the
 * {@code BinaryElectricalComponentConstraint} block. The
 * {@code BinaryElectricalComponentConstraint} block is used to validate/verify
 * the {@code TwoPinElectricalComponent} model's execution.
 * 
 * @see <a href="https://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @author ModelerOne
 *
 */
public class TwoPinElectricalComponent extends SysMLPart
{
	/**
	 * Port that represents the negative pin and its voltage and CurrentAmps flow into
	 * the component
	 */
	@ProxyPort
	ChargeFlowElement n;
	/**
	 * Port that represents the positive pin and its voltage and CurrentAmps flow into
	 * the component
	 */
	@ProxyPort
	ChargeFlowElement p;

	/**
	 * Attribute for flow of CurrentAmps through the component
	 */
	@Attribute
	CurrentAmps iThru;
	/**
	 * Voltage across the component
	 */
	@Attribute
	Voltage vDrop;
	/**
	 * Time of the CurrentAmps values of the component
	 */
	@Attribute
	DurationSeconds time;

	/**
	 * Comment(s) on the resistor for use in a BOM
	 */
	@BOMLineItemComment
	@Comment
	public SysMLComment comment;

	/**
	 * Constructor
	 * 
	 * @param name unique name of the component
	 */
	public TwoPinElectricalComponent(String name)
	{
		super(name, 0L);
	}

	@Override
	protected void createAttributes()
	{
		vDrop = new Voltage(0);
		time = new DurationSeconds(0);
		iThru = new CurrentAmps(0);
	}

	@Override
	protected void createPorts()
	{
		p = new ChargeFlowElement(this, Optional.of(this), 0L);
		n = new ChargeFlowElement(this, Optional.of(this), 1L);
	}
}
