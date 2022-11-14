package electriccircuit;

import sysmlinjava.analysis.bom.annotations.BOMLineItemValue;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Value;
import sysmlinjava.comments.SysMLComment;
import sysmlinjava.valuetypes.CapacitanceFarads;
import sysmlinjava.valuetypes.DurationSeconds;

/**
 * {@code CapacitorRC} is the SysMLinJava model of a capacitor component of an RC
 * circuit as part of the {@code ElectricCircuit} system. The model is a
 * SysMLinJava implementation of the capacitor model described in "SysML
 * Extension for Physical Interaction and Signal Flow Simulation", Object
 * Management Group, Inc., 2018. The {@code Capacitor} is a
 * {@code TwoPinElectricalComponent} characterized by its capacitance. It has
 * two pins, a positive and a negative, that interface with other circuit
 * components.
 * <p>
 * The {@code Capacitor} model is as specified by its constraints which are
 * declared in the {@code CapacitorConstraint} block. The
 * {@code CapacitorConstraint} block is used to validate/verify the capacitor
 * model's execution.
 * 
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @see electriccircuit.CapacitorRC
 * 
 * @author ModelerOne
 *
 */
public class CapacitorRC extends TwoPinElectricalComponent
{
	/**
	 * Capacitance of the capacitor
	 */
	@BOMLineItemValue
	@Value
	CapacitanceFarads capacitance;

	/**
	 * Constructor
	 * 
	 * @param name unique name for the capacitor
	 */
	public CapacitorRC(String name)
	{
		super(name);
	}

	/**
	 * Updates (recalculates) the new voltage across the capacitor for the specified
	 * time and resistor that is in series with the capacitor.
	 * 
	 * @param rc       resistor that is in series with capacitor
	 * @param vin      input voltage across the RC circuit
	 * @param dvdt     derivative (rate of change) of the input voltage
	 * @param duration time (into the model execution) of the new voltage
	 */
	@Operation
	public void updateVDrop(Resistor rc, Voltage vin, Voltage dvdt, DurationSeconds duration)
	{
		vDrop.setValue(vin.value - (rc.resistance.value * capacitance.value * dvdt.value));
	}

	/**
	 * Updates (recalculates) the new current through, as well as other derived
	 * properties of, the capacitor for the specified time and resistor that is in
	 * series with the capacitor
	 * 
	 * @param rc  resistor that is in series with capacitor
	 * @param vin input voltage across the RC circuit
	 */
	@Operation
	public void updateIthru(Resistor rc, Voltage vin)
	{
		iThru.setValue(rc.iThru);
		p.cF.i.setValue(iThru);
		p.cF.v.setValue(vin.value - rc.vDrop.value);
		n.cF.i.setValue(iThru);
		n.cF.v.setValue(p.cF.v.value - vDrop.value);
	}

	@Override
	protected void createValues()
	{
		super.createValues();
		capacitance = new CapacitanceFarads(0.01);
	}

	@Override
	protected void createComments()
	{
		super.createComments();
		comment = new SysMLComment("Description:Capacitor in RC series circuit\nSource:AceCapacitors.com");
	}
}
