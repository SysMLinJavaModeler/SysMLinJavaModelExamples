package electriccircuit;

import java.util.List;
import java.util.Optional;

import sysmlinjava.analysis.ParametricAnalysisCase;
import sysmlinjava.annotations.SysMLDocumentation;
import sysmlinjava.attributetypes.BBoolean;
import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.attributetypes.Voltage;
import sysmlinjava.constraint.SysMLConstraint;
import sysmlinjava.constraint.SysMLConstraintFunction;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.analysis.AnalysisResult;
import sysmlinjava.javaannotations.analysis.parametrics.Parameter;
import sysmlinjava.javaannotations.annotations.Documentation;
import sysmlinjava.javaannotations.constraint.Constraint;

/**
 * {@code GroundConstraint} is the SysMLinJava model of a constraint block for
 * the {@code Ground} component of the {@code ElectricCircuit} system. The
 * constraint block is implemented as a type of {@code ParametricAnalysisCase}.
 * The model is a SysMLinJava implementation of the constraint block described
 * in "SysML Extension for Physical Interaction and Signal Flow Simulation",
 * Object Management Group, Inc., 2018. The {@code GroundConstraint} is an
 * extension of the {@code BinaryElectricalComponentConstraint} with a
 * specialized constraint parameter for the voltage on the positive pin for the
 * ground.
 * <p>
 * The {@code GroundConstraint} model includes a constraint for the voltage of
 * the ground, i.e. {@code v = 0}. The {@code GroundConstraint} block is used to
 * validate/verify the voltage source model's execution.
 * <p>
 * Unlike the other components in the {@code ElectricCircuit} model, the
 * {@code Gound} component is not a "two pin" element, i.e. ground has only a
 * single interface/port for the zero voltage "sink". Consequently, the
 * {@code GroundConstraint} is unable to inherit the corresponding constraint
 * block for two pin compoents and must implement propeties that are unique to
 * the {@code Ground} component.
 * 
 * @see <a href="https://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * @author ModelerOne
 */
public class GroundConstraint extends ParametricAnalysisCase
{
	/**
	 * Functional interface for the constraint function of the ground component
	 */
	@FunctionalInterface
	public interface ComponentConstraintFunction extends SysMLConstraintFunction
	{
		/**
		 * Applies the constraint function
		 * 
		 * @return whether the constraint is satisfied
		 */
		BBoolean apply();
	}

	/**
	 * Constraint function of the constraint on the ground component
	 */
	@Constraint
	protected SysMLConstraintFunction constraintFunction;
	/**
	 * Constraint text of the constraint on the ground component
	 */
	@Documentation
	protected SysMLDocumentation constraintText;
	/**
	 * Constraint on the ground component
	 */
	@Constraint
	protected SysMLConstraint constraint;
	/**
	 * Result of the analysis case, i.e. whether the constraint is satisfied
	 */
	@AnalysisResult
	protected BBoolean result;

	/**
	 * Constraint parameter for the ground component, i.e. the constraint for which
	 * asserts ground voltage is always zero
	 */
	@Parameter
	public Voltage posV;
	/**
	 * Constraint parameter for the time of the CurrentAmps voltage
	 */
	@Parameter
	public DurationSeconds time;

	/**
	 * Constructor
	 * 
	 * @param name unique name of the constraint block
	 */
	public GroundConstraint(String name)
	{
		super(Optional.empty(), name, 0L);
	}

	@Action
	@Override
	public void perform()
	{
		result.setValue(((ComponentConstraintFunction) constraint.function.get()).apply());
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.empty();
	}

	@Override
	protected void createParameters()
	{
		posV = new Voltage(0);
	}

	@Override
	protected void createConstraintFunctions()
	{
		constraintFunction = (ComponentConstraintFunction) () ->
		{
			BBoolean result = BBoolean.False;
			if (posV.value == 0)
				result = BBoolean.True;
			else
				System.out.println("posV != 0");
			return result;
		};
	}

	@Override
	protected void createDocumentations()
	{
		constraintText = new SysMLDocumentation("""
			assert posV.value == 0;
		""");
	}

	@Override
	protected void createConstraints()
	{
		constraint = new SysMLConstraint(Optional.of(constraintFunction), constraintText, "Ground component constraint", 0L);
	}

	@Override
	protected void createResult()
	{
		result = new BBoolean(false);
	}

	@Override
	protected void createSubject()
	{
		subject = Optional.of(Ground.class);
	}

	@Override
	protected void createObjective()
	{
		objective = ElectricCircuitAnalysisRequirements.performConstraintsOnGroundComponent;
	}

	@Override
	protected void createActors()
	{
		actors = List.of();

	}
}
