package electriccircuit;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

import java.util.Optional;

import sysmlinjava.annotations.SysMLDocumentation;
import sysmlinjava.attributetypes.BBoolean;
import sysmlinjava.attributetypes.SysMLAttributeType;
import sysmlinjava.attributetypes.Voltage;
import sysmlinjava.constraint.SysMLConstraint;
import sysmlinjava.constraint.SysMLConstraintFunction;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.analysis.parametrics.Parameter;
import sysmlinjava.javaannotations.annotations.Documentation;
import sysmlinjava.javaannotations.constraint.Constraint;
import sysmlinjava.javaannotations.constraint.ConstraintFunction;

/**
 * {@code VoltageSourceConstraint} is the SysMLinJava model of a constraint
 * block for the {@code VoltageSource} component of the {@code ElectricCircuit}
 * system. The model is a SysMLinJava implementation of the constraint block
 * described in "SysML Extension for Physical Interaction and Signal Flow
 * Simulation", Object Management Group, Inc., 2018. The
 * {@code VoltageSourceConstraint} is an extension of the
 * {@code BinaryElectricalComponentConstraint} with a specialized constraint
 * parameter for the voltage source value of it input voltage to the circuit.
 * <p>
 * The {@code VoltageSourceConstraint} model includes a constraint for the
 * voltage input to the circuit which a simple sinusoidal wave form, i.e.
 * {@code vin = amp * sin(2 * PI * time)}. The {@code VoltageSourceConstraint}
 * block is used to validate/verify the voltage source model's execution.
 * 
 * @see <a href="https://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @author ModelerOne
 *
 */
public class VoltageSourceConstraint extends BinaryElectricalComponentConstraint
{
	/**
	 * Function of the constraint on the voltage source
	 */
	@ConstraintFunction
	protected SysMLConstraintFunction voltageSourceConstraintFunction;
	/**
	 * Text of the constraint on the voltage source
	 */
	@Documentation
	protected SysMLDocumentation voltageSourceConstraintText;
	/**
	 * Constraint on the voltage source
	 */
	@Constraint
	protected SysMLConstraint voltageSourceConstraint;
	/**
	 * Constraint parameter for the amplitude voltage of the voltage source
	 */
	@Parameter
	Voltage amp;

	/**
	 * Constraint for the amplitude voltage of the voltage source
	 */
	@Constraint
	SysMLConstraint ampConstraint;
	/**
	 * Constructor
	 * @param name unique name for the voltage source
	 */
	public VoltageSourceConstraint(String name)
	{
		super(Optional.empty(), name);
	}

	@Override
	protected void onParameterChange(String paramID, SysMLAttributeType paramValue)
	{
		if (paramID.equals(ParamsEnum.amp.toString()))
			amp.value = ((Voltage)paramValue).value;
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
			if(result.isTrue())
				result.setValue(((ComponentConstraintFunction)voltageSourceConstraint.function.get()).apply());
		}
	}

	@Override
	protected void createParameters()
	{
		super.createParameters();
		amp = new Voltage(0);
	}

	@Override
	protected void createConstraintFunctions()
	{
		super.createConstraintFunctions();
		voltageSourceConstraintFunction = (ComponentConstraintFunction)() ->
		{
			BBoolean result = BBoolean.False;
			if(v.value == amp.value * sin(2.0 * PI * time.value))
				result = BBoolean.True;
			else
				System.out.println("v != amp * sin(2 * PI * time");
			return result;
		};
	}

	@Override
	protected void createDocumentations()
	{
		super.createDocumentations();
		voltageSourceConstraintText = new SysMLDocumentation(
		"""
			assert v.value == amp.value * sin(2.0 * PI * time.value);
		""");
	}

	@Override
	protected void createConstraints()
	{
		super.createConstraints();
		voltageSourceConstraint = new SysMLConstraint(Optional.of(voltageSourceConstraintFunction), voltageSourceConstraintText, "Voltage source constraint", 0L);
	}
}