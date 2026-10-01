package electriccircuit;

import sysmlinjava.attributetypes.ResistanceOhms;
import sysmlinjava.attributetypes.Voltage;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.bom.annotations.BOMLineItemValue;

/**
 * {@code Resistor} is the SysMLinJava model of a resistor component of an RC or
 * RL series circuit as part of the {@code Circuit} system. The model is a
 * SysMLinJava implementation of the resistor model described in "SysML
 * Extension for Physical Interaction and Signal Flow Simulation", Object
 * Management Group, Inc., 2018. The {@code Resistor} is a
 * {@code TwoPinElectricalComponent} characterized by its resistance. It has two
 * pins, a positive and a negative, that interface with other circuit
 * components.
 * <p>
 * The {@code Resistor} model is as specified by its constraints which are
 * declared in the {@code ResistorConstraint} block. The
 * {@code ResistorConstraint} block is used to validate/verify the capacitor
 * model's execution.
 * 
 * @implNote The {@code Resistor} is annotated for use in generating a bill-of-materials.
 * 
 * @see <a href="https://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @author ModelerOne
 *
 */
public class Resistor extends TwoPinElectricalComponent
{
	/**
	 * Resistance of the resistor
	 */
	@BOMLineItemValue
	@Attribute
	public ResistanceOhms resistance;

	/**
	 * Max voltage tolerated across the resistor
	 */
	@BOMLineItemValue
	@Attribute
	public Voltage maxVoltage;
	
	/**
	 * Constructor
	 * 
	 * @param name unique name of the resistor
	 */
	public Resistor(String name)
	{
		super(name);
	}
}
