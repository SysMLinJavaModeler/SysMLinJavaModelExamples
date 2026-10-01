package electriccircuit;

import sysmlinjava.annotations.SysMLComment;
import sysmlinjava.attributetypes.ResistanceOhms;
import sysmlinjava.attributetypes.Voltage;
import sysmlinjava.javaannotations.actions.Action;

/**
 * {@code ResistorRC} is the SysMLinJava model of a resistor component of an RC
 * circuit as part of the {@code Circuit} system. The model is a SysMLinJava
 * implementation of the resistor model described in "SysML Extension for
 * Physical Interaction and Signal Flow Simulation", Object Management Group,
 * Inc., 2018. The {@code ResistorRC} is a {@code Resistor}
 * characterized by its resistance.
 * <p>
 * The {@code ResistorRC} model is as specified by its constraints which are
 * declared in the {@code ResistorConstraint} block. The
 * {@code ResistorConstraint} block is used to validate/verify the resistor
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
public class ResistorRC extends Resistor
{
	/**
	 * Constructor
	 * 
	 * @param name unique name
	 */
	public ResistorRC(String name)
	{
		super(name);
	}

	/**
	 * Updates (recalculates) the new voltage across the resistor for the specified
	 * voltage drops.
	 * 
	 * @param vin    input voltage across the series circuit
	 * @param cVDrop voltage drop across the capacitor that is in series with the
	 *               resistor
	 */
	@Action
	public void updateVDrop(Voltage vin, Voltage cVDrop)
	{
		// double s = sin(2.0 * PI * t.value);
		// vDrop.setValue((r.value * c.c.value * s)/(1.0 + r.value * c.c.value * s) *
		// vin.value);
		vDrop.setValue(vin.value - cVDrop.value);
	}

	/**
	 * Updates (recalculates) the new CurrentAmps through, as well as other derived
	 * properties of, the resistor for the specified voltage across the RC circuit
	 * 
	 * @param vRC input voltage across the RC circuit
	 */
	@Action
	public void updateIThru(Voltage vRC)
	{
		iThru.setValue(vDrop.value / resistance.value);
		p.cF.i.setValue(iThru.value);
		p.cF.v.setValue(vRC.value);
		n.cF.i.setValue(iThru.value);
		n.cF.v.setValue(vRC.value - vDrop.value);
	}

	@Override
	protected void createAttributes()
	{
		super.createAttributes();
		resistance = new ResistanceOhms(10.0);
		maxVoltage = new Voltage(220.0);
	}

	@Override
	protected void createComments()
	{
		super.createComments();
		comment = new SysMLComment("Description:Resistor in RC series circuit\nSource:AceResistors.com");
	}
}
