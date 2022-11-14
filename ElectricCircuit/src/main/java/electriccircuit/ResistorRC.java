package electriccircuit;

import sysmlinjava.annotations.Operation;
import sysmlinjava.comments.SysMLComment;
import sysmlinjava.valuetypes.ResistanceOhms;

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
	@Operation
	public void updateVDrop(Voltage vin, Voltage cVDrop)
	{
		// double s = sin(2.0 * PI * t.value);
		// vDrop.setValue((r.value * c.c.value * s)/(1.0 + r.value * c.c.value * s) *
		// vin.value);
		vDrop.setValue(vin.value - cVDrop.value);
	}

	/**
	 * Updates (recalculates) the new current through, as well as other derived
	 * properties of, the resistor for the specified voltage across the RC circuit
	 * 
	 * @param vRC input voltage across the RC circuit
	 */
	@Operation
	public void updateIThru(Voltage vRC)
	{
		iThru.setValue(vDrop.value / resistance.value);
		p.cF.i.setValue(iThru.value);
		p.cF.v.setValue(vRC.value);
		n.cF.i.setValue(iThru.value);
		n.cF.v.setValue(vRC.value - vDrop.value);
	}

	@Override
	protected void createValues()
	{
		super.createValues();
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
