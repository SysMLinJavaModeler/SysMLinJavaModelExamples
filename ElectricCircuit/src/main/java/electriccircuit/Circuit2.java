package electriccircuit;

import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.analysis.parametrics.ParametricAnalysis;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.parts.SysMLPart;

public class Circuit2 extends SysMLPart
{
	/**
	* Voltage source (AC) for the circuit
	*/
	@Part
	public VoltageSource voltageSource;

	/**
	* Ground for the circuit
	*/
	@Part
	public Ground ground;

	/**
	* Resistor for RC branch of the circuit
	*/
	@Part
	public ResistorRC resistorRC;

	/**
	* Resistor for RL branch of the circuit
	*/
	@Part
	public ResistorRL resistorRL;

	/**
	* &lt;comment&gt;
	*/
	@ParametricAnalysis
	public VoltageSourceConstraint voltageSourceConstraint;

	/**
	 * Constructor
	 *
	 * @param name name of this instance
	 * @param id   unique identifier of this instance
	 */
	public Circuit2(String name, Long id)
	{
		super(name, id);
	}

	/**
	* Operation to execute the model and thereby simulate the circuit operation
	*/
	@Action
	public void operate(DurationSeconds deltaDuration, DurationSeconds maxDuration)
	{
		//TODO: enter method block statements
	}

	@Override
	public void createParts()
	{
			voltageSource = null; //TODO: enter initializer: = new VoltageSource("<arguments>");
		ground = null; //TODO: enter initializer: = new Ground("<arguments>");
		resistorRC = null; //TODO: enter initializer: = new ResistorRC("<arguments>");
		resistorRL = null; //TODO: enter initializer: = new ResistorRL("<arguments>");
	}


	@Override
	public void createParametricAnalysisCases()
	{
			voltageSourceConstraint = null; //TODO: enter initializer: = new VoltageSourceConstraint("<arguments>");
	}


}
