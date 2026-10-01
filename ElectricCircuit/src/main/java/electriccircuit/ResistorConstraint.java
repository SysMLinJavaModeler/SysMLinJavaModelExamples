package electriccircuit;

import java.util.Optional;

import sysmlinjava.annotations.SysMLDocumentation;
import sysmlinjava.attributetypes.BBoolean;
import sysmlinjava.attributetypes.ResistanceOhms;
import sysmlinjava.attributetypes.SysMLAttributeType;
import sysmlinjava.constraint.SysMLConstraint;
import sysmlinjava.constraint.SysMLConstraintFunction;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.analysis.parametrics.Parameter;
import sysmlinjava.javaannotations.annotations.Documentation;
import sysmlinjava.javaannotations.constraint.Constraint;
import sysmlinjava.javaannotations.constraint.ConstraintFunction;

/**
 * {@code ResistorConstraint} is the SysMLinJava model of a constraint block for
 * the {@code Resistor} component of the {@code ElectricCircuit} system. The
 * model is a SysMLinJava implementation of the constraint block described in
 * "SysML Extension for Physical Interaction and Signal Flow Simulation", Object
 * Management Group, Inc., 2018. The {@code ResistorConstraint} is an extension
 * of the {@code BinaryElectricalComponentConstraint} with a specialized
 * constraint parameter for the resistance value of the resistor.
 * <p>
 * The {@code ResistorConstraint} model includes a constraint for the voltage
 * across the resistor which is the standard equation for the resistor, i.e.
 * {@code R * i = v}. This constraint of the {@code ResistorConstraint} block is
 * used to validate/verify the voltage source model's execution.
 * 
 * @see <a href="https://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * @author ModelerOne
 */
public class ResistorConstraint extends BinaryElectricalComponentConstraint
{
	/**
	 * Function of the constraint on the resistor
	 */
	@ConstraintFunction
	protected SysMLConstraintFunction resistorConstraintFunction;
	/**
	 * Text of the constraint on the resistor
	 */
	@Documentation
	protected SysMLDocumentation resistorConstraintText;
	/**
	 * Constraint on the resistor
	 */
	@Constraint
	protected SysMLConstraint resistorConstraint;
	/**
	 * Constraint parameter for the resistance of the resistor
	 */
	@Parameter
	ResistanceOhms r;

	/**
	 * Constructor
	 * 
	 * @param name unique name for the resistor
	 */
	public ResistorConstraint(String name)
	{
		super(Optional.empty(), name);
	}

	@Override
	protected void onParameterChange(String paramID, SysMLAttributeType paramValue)
	{
		if (paramID.equals(ParamsEnum.rc.toString()) || paramID.equals(ParamsEnum.rl.toString()))
			r.value = ((ResistanceOhms) paramValue).value;
		else
			super.onParameterChange(paramID, paramValue);
	}

	@Action
	@Override
	public void perform()
	{
		// Constraints are applied only if/when the "last" parameter (time)
		// is received which is when all parameters have been updated to their
		// next/updated value.
		if (currentParamID.isPresent() && currentParamID.get().equals(ParamsEnum.time.toString()))
		{
			super.perform();
			if (result.value)
				result.setValue(((ComponentConstraintFunction) resistorConstraint.function.get()).apply());
		}
	}

	@Override
	protected void createParameters()
	{
		super.createParameters();
		r = new ResistanceOhms(0);
	}

	@Override
	protected void createConstraintFunctions()
	{
		super.createConstraintFunctions();
		resistorConstraintFunction = (ComponentConstraintFunction) () ->
		{
			BBoolean result = BBoolean.False;
			if (r.value * i.value == v.value)
				result = BBoolean.True;
			else
				System.out.println("r.value * i.value != v.value");
			return result;
		};
	}

	@Override
	protected void createDocumentations()
	{
		super.createDocumentations();
		resistorConstraintText = new SysMLDocumentation("""
			assert r.value * i.value == v.value;
		""");
	}

	@Override
	protected void createConstraints()
	{
		super.createConstraints();
		resistorConstraint = new SysMLConstraint(Optional.of(resistorConstraintFunction), resistorConstraintText, "Resistor constraint", 0L);
	}
}
