package connectedtanks;

import static java.lang.Math.PI;
import static java.lang.Math.pow;

import java.util.List;
import java.util.Optional;

import sysmlinjava.analysis.ParametricAnalysisCase;
import sysmlinjava.annotations.SysMLDocumentation;
import sysmlinjava.attributetypes.BBoolean;
import sysmlinjava.attributetypes.DistanceMeters;
import sysmlinjava.attributetypes.PressureNewtonsPerMeterSquare;
import sysmlinjava.attributetypes.SysMLAttributeType;
import sysmlinjava.attributetypes.ViscosityPascalSecond;
import sysmlinjava.attributetypes.ViscousResistanceNewtons;
import sysmlinjava.attributetypes.VolumeFlowMetersCubicPerSecond;
import sysmlinjava.constraint.SysMLConstraint;
import sysmlinjava.constraint.SysMLConstraintFunction;
import sysmlinjava.javaannotations.analysis.AnalysisResult;
import sysmlinjava.javaannotations.analysis.parametrics.Parameter;
import sysmlinjava.javaannotations.annotations.Documentation;
import sysmlinjava.javaannotations.constraint.Constraint;
import sysmlinjava.javaannotations.constraint.ConstraintFunction;

/**
 * Parameteric analysis of the {@code Pipe} of the {@code ConnectedTanks}
 * system. {@code PipeAnalysis} includes parameters for dynamic and static
 * values of the pipe and specifies a set of constraints on the parameters as
 * specified in the OMG document from which this example SysMLinJava model was
 * derived, i.e. "SysML Extension for Physical Interaction and Signal Flow
 * Simulation", Object Management Group, Inc., 2018.
 * 
 * @author ModelerOne
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 */
public class PipeAnalysis extends ParametricAnalysisCase
{
	/**
	 * Function of the constraint on the pipe
	 */
	@ConstraintFunction
	protected SysMLConstraintFunction pipeConstraintFunction;
	/**
	 * Text of the constraint on the pipe
	 */
	@Documentation
	protected SysMLDocumentation pipeConstraintText;
	/**
	 * Constraint on the pipe
	 */
	@Constraint
	protected SysMLConstraint pipeConstraint;
	/**
	 * Result of the constraint application, i.e. whether the constraint is
	 * satisfied
	 */
	@AnalysisResult
	protected BBoolean result;

	/**
	 * Pressure at the opening to tank 1
	 */
	@Parameter
	PressureNewtonsPerMeterSquare opening1Pressure;
	/**
	 * Pressure at the opening to tank 2
	 */
	@Parameter
	PressureNewtonsPerMeterSquare opening2Pressure;
	/**
	 * Rate of flow of fluid through opening to tank 1
	 */
	@Parameter
	VolumeFlowMetersCubicPerSecond opening1FluidFlow;
	/**
	 * Rate of flow of fluid through opening to tank 1
	 */
	@Parameter
	VolumeFlowMetersCubicPerSecond opening2FluidFlow;
	/**
	 * Radius of pipe
	 */
	@Parameter
	DistanceMeters radius;
	/**
	 * Length of pipe
	 */
	@Parameter
	DistanceMeters length;
	/**
	 * Rate of flow of fluid through pipe
	 */
	@Parameter
	VolumeFlowMetersCubicPerSecond fluidFlow;
	/**
	 * Pressure differential across pipe
	 */
	@Parameter
	PressureNewtonsPerMeterSquare pressureDiff;
	/**
	 * Viscosity of fluid in the pipe
	 */
	@Parameter
	ViscosityPascalSecond viscosity;
	/**
	 * Resistance of pipe to fluid
	 */
	@Parameter
	ViscousResistanceNewtons resistance;

	/**
	 * Constructor
	 * 
	 * @param name unique name
	 */
	public PipeAnalysis(String name)
	{
		super(Optional.empty(), "pipeAnalysis", 0L);
	}

	@Override
	public void perform()
	{
		// Constraints are applied only if/when the "last" parameter (opening2FluidFlow)
		// is received which is when all parameters have been updated to their
		// next/updated value.
		if (currentParamID.isPresent() && currentParamID.get().equals(PipeParams.opening2FluidFlow.toString()))
		{
			result.setValue(((ComponentConstraintFunction) pipeConstraint.function.get()).apply());
			logger.info(String.format("pipe: pressureDiff=%4.4f fluidFlow=%2.5f", pressureDiff.value, fluidFlow.value));
		}
	}

	@Override
	protected void onParameterChange(String paramID, SysMLAttributeType paramValue)
	{
		PipeParams paramEnum = PipeParams.valueOf(paramID);
		if (paramEnum != null)
			switch (paramEnum)
			{
			case fluidFlow:
				fluidFlow.value = ((VolumeFlowMetersCubicPerSecond) paramValue).value;
				break;
			case length:
				length.value = ((DistanceMeters) paramValue).value;
				break;
			case opening1FluidFlow:
				opening1FluidFlow.value = ((VolumeFlowMetersCubicPerSecond) paramValue).value;
				break;
			case opening1Pressure:
				opening1Pressure.value = ((PressureNewtonsPerMeterSquare) paramValue).value;
				break;
			case opening2FluidFlow:
				opening2FluidFlow.value = ((VolumeFlowMetersCubicPerSecond) paramValue).value;
				break;
			case opening2Pressure:
				opening2Pressure.value = ((PressureNewtonsPerMeterSquare) paramValue).value;
				break;
			case pressureDiff:
				pressureDiff.value = ((PressureNewtonsPerMeterSquare) paramValue).value;
				break;
			case radius:
				radius.value = ((DistanceMeters) paramValue).value;
				break;
			case resistance:
				resistance.value = ((ViscousResistanceNewtons) paramValue).value;
				break;
			case viscosity:
				viscosity.value = ((ViscosityPascalSecond) paramValue).value;
				break;
			default:
				break;
			}
		else
			logger.severe("unrecognized parameter ID for parameter to be retrieved: " + paramID);
	}

	/**
	 * Functional interface for the pipe or tank constraint function
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
	 * No state machine as pipe analysis is synchronous with model execution
	 */
	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.empty();
	}

	@Override
	protected void createParameters()
	{
		/**
		 * All params initialized to zero when first created. Will be set to analysis
		 * values by {@code updateBoundParam()} method when invoked by binding
		 * connectors
		 */
		opening1Pressure = new PressureNewtonsPerMeterSquare(0);
		opening2Pressure = new PressureNewtonsPerMeterSquare(0);
		opening1FluidFlow = new VolumeFlowMetersCubicPerSecond(0);
		opening2FluidFlow = new VolumeFlowMetersCubicPerSecond(0);
		radius = new DistanceMeters(0);
		length = new DistanceMeters(0);
		fluidFlow = new VolumeFlowMetersCubicPerSecond(0);
		pressureDiff = new PressureNewtonsPerMeterSquare(0);
		viscosity = new ViscosityPascalSecond(0);
		resistance = new ViscousResistanceNewtons(0);
	}

	@Override
	protected void createConstraintFunctions()
	{
		super.createConstraintFunctions();
		pipeConstraintFunction = (ComponentConstraintFunction) () ->
		{
			BBoolean result = BBoolean.False;
			if (resistance.value == (8 * viscosity.value * length.value) / (PI * pow(radius.value, 4.0)))
				if (pressureDiff.value == opening2Pressure.value - opening1Pressure.value)
					if (fluidFlow.value == pressureDiff.value / resistance.value)
						if (opening1FluidFlow.value == fluidFlow.value)
							if (opening2FluidFlow.value == -opening1FluidFlow.value)
								result = BBoolean.True;
							else
								logger.warning("opening2FluidFlow != opening1FluidFlow");
						else
							logger.warning("opening1FluidFlow != fluidFlow");
					else
						logger.warning("fluidFlow != pressureDiff / resistance");
				else
					logger.warning("pressureDiff != opening2Pressure - opening1Pressure");
			else
				logger.warning("resistance != (8 * viscosity * length) / (PI * pow(radius, 4.0))");
			return result;
		};
	}

	@Override
	protected void createDocumentations()
	{
		super.createConstraints();
		pipeConstraintText = new SysMLDocumentation("""
			assert resistance.value == (8 * viscosity.value * length.value) / (PI * pow(radius.value, 2.0));
			assert pressureDiff.value == opening2Pressure.value - opening1Pressure.value;
			assert fluidFlow.value == pressureDiff.value / resistance.value;
			assert opening1FluidFlow.value == fluidFlow.value;
			assert opening2FluidFlow.value == opening1FluidFlow.value;
		""");
	}

	@Override
	protected void createConstraints()
	{
		pipeConstraint = new SysMLConstraint(Optional.of(pipeConstraintFunction), pipeConstraintText, "Pipe constraint", 0L);
	}

	@Override
	protected void createResult()
	{
		result = new BBoolean(false);
	}

	@Override
	protected void createSubject()
	{
		subject = Optional.of(Pipe.class);
	}

	@Override
	protected void createObjective()
	{
		objective = ConnectedTanksSystemAnalysisRequirements.pipeAnalysisObjective;
	}

	@Override
	protected void createActors()
	{
		actors = List.of(Tank.class);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("PipeConstraint [name=");
		builder.append(name);
		builder.append(", id=");
		builder.append(id);
		builder.append(", opening1Pressure=");
		builder.append(opening1Pressure);
		builder.append(", opening2Pressure=");
		builder.append(opening2Pressure);
		builder.append(", opening1FluidFlow=");
		builder.append(opening1FluidFlow);
		builder.append(", opening2FluidFlow=");
		builder.append(opening2FluidFlow);
		builder.append(", radius=");
		builder.append(radius);
		builder.append(", length=");
		builder.append(length);
		builder.append(", fluidFlow=");
		builder.append(fluidFlow);
		builder.append(", pressureDiff=");
		builder.append(pressureDiff);
		builder.append(", viscosity=");
		builder.append(viscosity);
		builder.append(", resistance=");
		builder.append(resistance);
		builder.append("]");
		return builder.toString();
	}
}