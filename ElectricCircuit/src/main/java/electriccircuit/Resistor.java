package electriccircuit;

import sysmlinjava.analysis.bom.annotations.BOMLineItemValue;
import sysmlinjava.annotations.Value;
import sysmlinjava.valuetypes.ResistanceOhms;

/**
 * {@code Resistor} is the SysMLinJava model of a resistor component of an RC or
 * RL serie circuit as part of the {@code Circuit} system. The model is a
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
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @see electriccircuit.Resistor
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
	@Value
	public ResistanceOhms resistance;

	/**
	 * Max voltage tolerated across the resistor
	 */
	@BOMLineItemValue
	@Value
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
