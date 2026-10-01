package electriccircuit;

import java.util.Optional;

import sysmlinjava.annotations.SysMLDocumentation;
import sysmlinjava.attributetypes.BBoolean;
import sysmlinjava.attributetypes.CurrentAmps;
import sysmlinjava.attributetypes.InductanceHenrys;
import sysmlinjava.attributetypes.SysMLAttributeType;
import sysmlinjava.constraint.SysMLConstraint;
import sysmlinjava.constraint.SysMLConstraintFunction;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.analysis.parametrics.Parameter;
import sysmlinjava.javaannotations.annotations.Documentation;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.constraint.Constraint;
import sysmlinjava.javaannotations.constraint.ConstraintFunction;

/**
 * {@code InductorConstraint} is the SysMLinJava model of a constraint block for
 * the {@code Inductor} component of the {@code ElectricCircuit} system. The
 * model is a SysMLinJava implementation of the constraint block described in
 * "SysML Extension for Physical Interaction and Signal Flow Simulation", Object
 * Management Group, Inc., 2018. The {@code InductorConstraint} is an extension
 * of the {@code BinaryElectricalComponentConstraint} with a specialized
 * constraint parameter for the inductance value of the inductor.
 * <p>
 * The {@code InductorConstraint} model includes a constraint for the voltage
 * across the inductor which is the standard equation for the inductor, i.e.
 * {@code L * di/dt = v}. This constraint of the {@code InductorConstraint}
 * block is used to validate/verify the voltage source model's execution.
 * 
 * @see <a href="https://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @author ModelerOne
 *
 */
public class InductorConstraint extends BinaryElectricalComponentConstraint
{
	/**
	 * Function of the constraint on the inductor
	 */
	@ConstraintFunction
	protected SysMLConstraintFunction inductorConstraintFunction;
	/**
	 * Text of the constraint on the inductor
	 */
	@Documentation
	protected SysMLDocumentation inductorConstraintText;
	/**
	 * Constraint on the inductor
	 */
	@Constraint
	protected SysMLConstraint inductorConstraint;

	/**
	 * Constraint parameter for the inductance of the inductor
	 */
	@Parameter
	InductanceHenrys l;

	/**
	 * Value for the previous CurrentAmps value used to estimate the derivative of the
	 * CurrentAmps flow.
	 */
	@Attribute
	CurrentAmps iPrevious;

	/**
	 * Constructor
	 * 
	 * @param name unique name for the inductor
	 */
	public InductorConstraint(String name)
	{
		super(Optional.empty(), name);
	}

	@Override
	protected void createAttributes()
	{
		super.createAttributes();
		iPrevious = new CurrentAmps(0);
	}

	@Override
	protected void onParameterChange(String paramID, SysMLAttributeType paramValue)
	{
		if (paramID.equals(ParamsEnum.l.toString()))
			l.value = ((InductanceHenrys)paramValue).value;
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
				result.setValue(((ComponentConstraintFunction)inductorConstraint.function.get()).apply());
			iPrevious.value = i.value;
		}
	}

	@Override
	protected void createParameters()
	{
		super.createParameters();
		l = new InductanceHenrys(0);
	}

	@Override
	protected void createConstraintFunctions()
	{
		super.createConstraintFunctions();
		inductorConstraintFunction = (ComponentConstraintFunction)() ->
		{
			BBoolean result = BBoolean.False;
			double di = i.value - iPrevious.value;
			if(l.value * di == v.value)
				result = BBoolean.True;
			else
				System.out.println("l * di != v");
			return result;
		};
	}

	@Override
	protected void createDocumentations()
	{
		super.createDocumentations();
		inductorConstraintText = new SysMLDocumentation(
		"""
			double di = i.value - iPrevious.value;
			assert l.value * di == v.value : "l * di != v";
		""");
	}

	@Override
	protected void createConstraints()
	{
		super.createConstraints();
		inductorConstraint = new SysMLConstraint(Optional.of(inductorConstraintFunction), inductorConstraintText, "Inductor constraint", 0L);
	}
}
