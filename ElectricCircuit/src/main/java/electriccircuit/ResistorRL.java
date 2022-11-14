package electriccircuit;

import static java.lang.Math.PI;
import static java.lang.Math.sin;
import sysmlinjava.annotations.Operation;
import sysmlinjava.comments.SysMLComment;
import sysmlinjava.valuetypes.DurationSeconds;
import sysmlinjava.valuetypes.ResistanceOhms;

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
	 * Updates (recalculates) the new current across the resistor for the specified
	 * time and induct that is in series with the resistor.
	 * 
	 * @param l   inductor that is in series with the resistor in the RL circuit
	 * @param vin input voltage across the RL circuit
	 * @param t   time (into the model execution) of the new voltage
	 */
	@Operation
	public void updateVDrop(InductorRL l, Voltage vin, DurationSeconds t)
	{
		double s = sin(2.0 * PI * t.value);
		vDrop.setValue(vin.value * (resistance.value / (resistance.value + l.inductance.value * s)));
	}

	/**
	 * Updates (recalculates) the new current through, as well as other derived
	 * properties of, the resistor for the specified voltage across the RL circuit
	 * 
	 * @param vRL input voltage across the RL circuit
	 */
	@Operation
	public void updateIThru(Voltage vRL)
	{
		iThru.setValue(vDrop.value / resistance.value);
		p.cF.i.setValue(iThru.value);
		p.cF.v.setValue(vRL.value);
		n.cF.i.setValue(iThru.value);
		n.cF.v.setValue(vRL.value - vDrop.value);
	}

	@Override
	protected void createValues()
	{
		super.createValues();
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
