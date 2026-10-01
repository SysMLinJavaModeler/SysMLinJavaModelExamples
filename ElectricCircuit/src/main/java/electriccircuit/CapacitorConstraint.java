package electriccircuit;

import java.util.Optional;

import sysmlinjava.annotations.SysMLDocumentation;
import sysmlinjava.attributetypes.BBoolean;
import sysmlinjava.attributetypes.CapacitanceFarads;
import sysmlinjava.attributetypes.SysMLAttributeType;
import sysmlinjava.attributetypes.Voltage;
import sysmlinjava.constraint.SysMLConstraint;
import sysmlinjava.constraint.SysMLConstraintFunction;
import sysmlinjava.javaannotations.actions.Calculation;
import sysmlinjava.javaannotations.analysis.parametrics.Parameter;
import sysmlinjava.javaannotations.annotations.Documentation;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.constraint.Constraint;
import sysmlinjava.javaannotations.constraint.ConstraintFunction;

/**
 * {@code CapacitorConstraint} is the SysMLinJava model of a parametric analysis
 * for the {@code Capacitor} component of the {@code ElectricCircuit} system.
 * The model is a SysMLinJava implementation of the parametric analysis
 * described in "SysML Extension for Physical Interaction and Signal Flow
 * Simulation", Object Management Group, Inc., 2018. The
 * {@code CapacitorConstraint} is an extension of the
 * {@code BinaryElectricalComponentConstraint} with a specialized constraint
 * parameter for the capacitance value of the capacitor.
 * <p>
 * The {@code CapacitorConstraint} model includes a constraint for the current
 * flowing thru the capacitor which is the standard equation for the capacitor,
 * i.e. {@code C * dv/dt = i}. This constraint of the
 * {@code CapacitorConstraint} is used to analyze/validate the voltage source
 * model's execution.
 * 
 * @see <a href="https://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * @author ModelerOne
 */
public class CapacitorConstraint extends BinaryElectricalComponentConstraint
{
	/**
	 * Function of the constraint on the capacitor
	 */
	@ConstraintFunction
	protected SysMLConstraintFunction capacitorConstraintFunction;
	/**
	 * Text of the constraint on the capacitor
	 */
	@Documentation
	protected SysMLDocumentation capacitorConstraintText;
	/**
	 * Constraint on the capacitor
	 */
	@Constraint
	protected SysMLConstraint capacitorConstraint;

	/**
	 * Constraint parameter for the capacitance of the capacitor
	 */
	@Parameter
	CapacitanceFarads c;

	/**
	 * Value for previous voltage used to estimate the derivative of voltage across
	 * the capacitor
	 */
	@Attribute
	Voltage vPrevious;

	/**
	 * Constraint for the capacitor
	 */
	@Constraint
	SysMLConstraint cConstraint;

	/**
	 * Constructor
	 * 
	 * @param name unique name of the capacitor constraint block
	 */
	public CapacitorConstraint(String name)
	{
		super(Optional.empty(), name);
	}

	@Override
	protected void createAttributes()
	{
		super.createAttributes();
		vPrevious = new Voltage(0);
	}

	@Override
	protected void onParameterChange(String paramID, SysMLAttributeType paramValue)
	{
		if (paramID.equals(ParamsEnum.c.toString()))
			c.value = ((CapacitanceFarads) paramValue).value;
		else
			super.onParameterChange(paramID, paramValue);
	}

	@Calculation
	@Override
	public void perform()
	{
		// Constraints are applied only if/when the "last" parameter (time)
		// is received which is when all parameters have been updated to their
		// next/updated value.
		if (currentParamID.isPresent() && currentParamID.get().equals(ParamsEnum.time.toString()))
		{
			super.perform();
			if (result.isTrue())
				result.setValue(((ComponentConstraintFunction) capacitorConstraint.function.get()).apply());
			vPrevious.value = v.value;
		}
	}

	@Override
	protected void createParameters()
	{
		super.createParameters();
		c = new CapacitanceFarads(0);
	}

	@Override
	protected void createConstraintFunctions()
	{
		super.createConstraintFunctions();
		capacitorConstraintFunction = (ComponentConstraintFunction) () ->
		{
			BBoolean result = BBoolean.False;
			double dv = v.value - vPrevious.value;
			if (c.value * dv == i.value)
				result = BBoolean.True;
			else
				System.out.println("c * dv/dt != i");
			return result;
		};
	}

	@Override
	protected void createDocumentations()
	{
		super.createDocumentations();
		capacitorConstraintText = new SysMLDocumentation("""
			double dv = v.value - vPrevious.value;
			assert(c.value * dv == i.value)
		""");
	}

	@Override
	protected void createConstraints()
	{
		super.createConstraints();
		capacitorConstraint = new SysMLConstraint(Optional.of(capacitorConstraintFunction), capacitorConstraintText, "Capacitor constraint", 0L);
	}
}
