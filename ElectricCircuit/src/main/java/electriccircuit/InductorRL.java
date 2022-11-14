package electriccircuit;

import static java.lang.Math.PI;
import static java.lang.Math.sin;
import sysmlinjava.analysis.bom.annotations.BOMLineItemValue;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Value;
import sysmlinjava.comments.SysMLComment;
import sysmlinjava.valuetypes.DurationSeconds;
import sysmlinjava.valuetypes.InductanceHenrys;

/**
 * {@code InductorRL} is the SysMLinJava model of an inductor component of an RL
 * circuit as part of the {@code Circuit} system. The model is a SysMLinJava
 * implementation of the inductor model described in "SysML Extension for
 * Physical Interaction and Signal Flow Simulation", Object Management Group,
 * Inc., 2018. The {@code InductorRL} is a {@code TwoPinElectricalComponent}
 * characterized by its inductance. It has two pins, a positive and a negative,
 * that interface with other circuit components.
 * <p>
 * The {@code InductorRL} model is as specified by its constraints which are
 * declared in the {@code InductorConstraint} block. The
 * {@code InductorConstraint} block is used to validate/verify the capacitor
 * model's execution.
 * 
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @see electriccircuit.InductorRL
 * 
 * @author ModelerOne
 *
 */
public class InductorRL extends TwoPinElectricalComponent
{
	/**
	 * Inductance of the inductor
	 */
	@BOMLineItemValue
	@Value
	InductanceHenrys inductance;

	/**
	 * Constructor
	 * 
	 * @param name unique name for the inductor
	 */
	public InductorRL(String name)
	{
		super(name);
	}

	/**
	 * Updates (recalculates) the new voltage across the inductor for the specified
	 * time and resistor that is in series with the inductor.
	 * 
	 * @param rl  resistor that is in series with inductor
	 * @param vin input voltage across the RL circuit
	 * @param t   time (into the model execution) of the new voltage
	 */
	@Operation
	public void updateVDrop(Resistor rl, Voltage vin, DurationSeconds t)
	{
		double s = sin(2.0 * PI * t.value);
		vDrop.setValue(vin.value * (((inductance.value * s) / (rl.resistance.value + inductance.value * s))));
	}

	/**
	 * Updates (recalculates) the new current through, as well as other derived
	 * properties of, the inductor for the specified time and resistor that is in
	 * series with the inductor
	 * 
	 * @param rl  resistor that is in series with inductor
	 * @param vin input voltage across the RL circuit
	 */
	@Operation
	public void updateIthru(Resistor rl, Voltage vin)
	{
		iThru.setValue(rl.iThru);
		p.cF.i.setValue(iThru);
		p.cF.v.setValue(vin.value - rl.vDrop.value);
		n.cF.i.setValue(iThru);
		n.cF.v.setValue(p.cF.v.value - vDrop.value);
	}

	@Override
	protected void createValues()
	{
		super.createValues();
		inductance = new InductanceHenrys(0.1);
	}

	@Override
	protected void createComments()
	{
		super.createComments();
		comment = new SysMLComment("Description:Inductor in RL series circuit\nSource:AceInductors.com");
	}
}
