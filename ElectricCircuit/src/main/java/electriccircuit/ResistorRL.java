package electriccircuit;

import static java.lang.Math.PI;
import static java.lang.Math.sin;
import sysmlinjava.annotations.SysMLComment;
import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.attributetypes.ResistanceOhms;
import sysmlinjava.attributetypes.Voltage;
import sysmlinjava.javaannotations.actions.Action;

/**
 * {@code ResistorRL} is the SysMLinJava model of a resistor component of an RL
 * circuit as part of the {@code Circuit} system. The model is a SysMLinJava
 * implementation of the resistor model described in "SysML Extension for
 * Physical Interaction and Signal Flow Simulation", Object Management Group,
 * Inc., 2018. The {@code ResistorRC} is a {@code Resistor}
 * characterized by its resistance.
 * <p>
 * The {@code ResistorRL} model is as specified by its constraints which are
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
public class ResistorRL extends Resistor
{

	/**
	 * Constructor
	 * 
	 * @param name unique name
	 */
	public ResistorRL(String name)
	{
		super(name);
	}

	/**
	 * Updates (recalculates) the new CurrentAmps across the resistor for the specified
	 * time and induct that is in series with the resistor.
	 * 
	 * @param l   inductor that is in series with the resistor in the RL circuit
	 * @param vin input voltage across the RL circuit
	 * @param t   time (into the model execution) of the new voltage
	 */
	@Action
	public void updateVDrop(InductorRL l, Voltage vin, DurationSeconds t)
	{
		double s = sin(2.0 * PI * t.value);
		vDrop.setValue(vin.value * (resistance.value / (resistance.value + l.inductance.value * s)));
	}

	/**
	 * Updates (recalculates) the new CurrentAmps through, as well as other derived
	 * properties of, the resistor for the specified voltage across the RL circuit
	 * 
	 * @param vRL input voltage across the RL circuit
	 */
	@Action
	public void updateIThru(Voltage vRL)
	{
		iThru.setValue(vDrop.value / resistance.value);
		p.cF.i.setValue(iThru.value);
		p.cF.v.setValue(vRL.value);
		n.cF.i.setValue(iThru.value);
		n.cF.v.setValue(vRL.value - vDrop.value);
	}

	@Override
	protected void createAttributes()
	{
		super.createAttributes();
		resistance = new ResistanceOhms(20.0);
		maxVoltage = new Voltage(220.0);
	}

	@Override
	protected void createComments()
	{
		super.createComments();
		comment = new SysMLComment("Description:Resistor in RL series circuit\nSource:AceResistors.com");
	}
}
