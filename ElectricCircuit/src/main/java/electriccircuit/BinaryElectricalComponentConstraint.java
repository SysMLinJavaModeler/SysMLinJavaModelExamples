package electriccircuit;

import java.util.List;
import java.util.Optional;

import sysmlinjava.analysis.ParametricAnalysisCase;
import sysmlinjava.annotations.SysMLDocumentation;
import sysmlinjava.attributetypes.BBoolean;
import sysmlinjava.attributetypes.CurrentAmps;
import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.attributetypes.SysMLAttributeType;
import sysmlinjava.attributetypes.Voltage;
import sysmlinjava.constraint.SysMLConstraint;
import sysmlinjava.constraint.SysMLConstraintFunction;
import sysmlinjava.javaannotations.actions.Calculation;
import sysmlinjava.javaannotations.analysis.AnalysisResult;
import sysmlinjava.javaannotations.analysis.parametrics.Parameter;
import sysmlinjava.javaannotations.annotations.Documentation;
import sysmlinjava.javaannotations.constraint.Constraint;
import sysmlinjava.javaannotations.constraint.ConstraintFunction;

/**
 * {@code BinaryElectricalComponentConstraint} is the SysMLinJava model of a
 * constraint block common to the constraint blocks for the components of the
 * {@code ElectricCircuit} system. The model is a SysMLinJava implementation of
 * the constraint block described in "SysML Extension for Physical Interaction
 * and Signal Flow Simulation", Object Management Group, Inc., 2018. The
 * constraint block is implemented as a type of {@code ParametricAnalysisCase}.
 * The {@code BinaryElectricalComponentConstraint} is a
 * {@code SysMLConstraintBlock} characterized by its constraint parameters,
 * which are the values of the {@code TwoPinElectricalComponent}.
 * <p>
 * The {@code BinaryElectricalComponentConstraint} model iincludes a series of
 * constraints which are declared in the constraint block. The
 * {@code BinaryElectricalComponentConstraint} block is used to validate/verify
 * the capacitor model's execution.
 * 
 * @see <a href="https://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * @author ModelerOne
 */
public class BinaryElectricalComponentConstraint extends ParametricAnalysisCase
{
	/**
	 * Functional interface for the constraint function for two-pin element
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
	 * Constraint function of the constraint on the component
	 */
	@ConstraintFunction
	protected SysMLConstraintFunction constraintFunction;
	/**
	 * Constraint text of the constraint on the component
	 */
	@Documentation
	protected SysMLDocumentation constraintText;
	/**
	 * Constraint on the component
	 */
	@Constraint
	protected SysMLConstraint constraint;

	/**
	 * Result of the parametric analysis, i.e. whether the constraint is satisfied
	 */
	@AnalysisResult
	protected BBoolean result;

	/**
	 * Parameter for the CurrentAmps through the component
	 */
	@Parameter
	public CurrentAmps i;
	/**
	 * Parameter for the CurrentAmps through the negative pin of the
	 * component
	 */
	@Parameter
	public CurrentAmps negI;
	/**
	 * Parameter for the CurrentAmps through the positive pin of the
	 * component
	 */
	@Parameter
	public CurrentAmps posI;
	/**
	 * Parameter for the voltage across the component
	 */
	@Parameter
	public Voltage v;
	/**
	 * Parameter for the voltage at the negative pin of the component
	 */
	@Parameter
	public Voltage negV;
	/**
	 * Parameter for the voltage at the posative pin of the component
	 */
	@Parameter
	public Voltage posV;
	/**
	 * Parameter for the time of the CurrentAmps values of the other
	 * constraint parameters
	 */
	@Parameter
	public DurationSeconds time;

	/**
	 * Constructor
	 * 
	 * @param parent parent constraint block, if any, to this constraint block
	 * @param name   unique name of the constraint block
	 */
	public BinaryElectricalComponentConstraint(Optional<? extends ParametricAnalysisCase> parent, String name)
	{
		super(parent, name, 0L);
	}

	@Override
	protected void onParameterChange(String paramID, SysMLAttributeType paramValue)
	{
		ParamsEnum paramEnum = ParamsEnum.valueOf(paramID);
		if (paramEnum != null)
			switch (paramEnum)
			{
			case i:
				i.value = ((CurrentAmps) paramValue).value;
				break;
			case negI:
				negI.value = ((CurrentAmps) paramValue).value;
				break;
			case negV:
				negV.value = ((Voltage) paramValue).value;
				break;
			case posI:
				posI.value = ((CurrentAmps) paramValue).value;
				break;
			case posV:
				posV.value = ((Voltage) paramValue).value;
				break;
			case time:
				time.value = ((DurationSeconds) paramValue).value;
				break;
			case v:
				v.value = ((Voltage) paramValue).value;
				break;
			case amp:
			case g:
			case l:
			case rc:
			case rl:
			default:
				logger.severe("unrecognized parameter enum for constraint parameters: " + paramEnum);
				break;
			}
		else
			logger.severe("paramID has no mapping to any constraint parameter: " + paramID);
	}
		@Calculation
	@Override
	public void perform()
	{
		result.setValue(((ComponentConstraintFunction) constraint.function.get()).apply());
	}

	/**
	 * State machine not used for this constraint block as this is a fully
	 * synchronous model, i.e. all electronic components operate sequentially in
	 * same execution thread. Default for {@code SysMLConstraintBlock} is
	 * asynchronous execution in multi-threaded model, so need to override this
	 * operation to NOT create state machine.
	 */
	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.empty();
	}

	@Override
	protected void createParameters()
	{
		i = new CurrentAmps(0);
		negI = new CurrentAmps(0);
		posI = new CurrentAmps(0);
		v = new Voltage(0);
		negV = new Voltage(0);
		posV = new Voltage(0);
		time = new DurationSeconds(0);
	}

	@Override
	protected void createConstraintFunctions()
	{
		constraintFunction = (ComponentConstraintFunction) () ->
		{
			BBoolean result = BBoolean.False;
			if (posI.value + negI.value == 0)
				if (posV.value - negV.value == v.value)
					if (i == posI)
						result = BBoolean.True;
					else
						System.out.println("i != posI");
				else
					System.out.println("posV - negV != 0");
			else
				System.out.println("posI + negI != 0");
			return result;
		};
	}

	@Override
	protected void createDocumentations()
	{
		constraintText = new SysMLDocumentation("""
			assert posI.value + negI.value == 0;
			assert posV.value - negV.value == v.value;"
			assert i == posI;
		""");
	}

	@Override
	protected void createConstraints()
	{
		constraint = new SysMLConstraint(Optional.of(constraintFunction), constraintText, "Component constraint", 0L);
	}

	@Override
	protected void createResult()
	{
		result = new BBoolean(false);
	}

	@Override
	protected void createSubject()
	{
		subject = Optional.of(TwoPinElectricalComponent.class);
	}

	@Override
	protected void createObjective()
	{
		objective = ElectricCircuitAnalysisRequirements.performConstraintsOnTwoPinComponents;
	}

	@Override
	protected void createActors()
	{
		actors = List.of();
	}
}
